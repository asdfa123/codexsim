/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import metagram.Loc;
import metagram.*;
import metagram.clock.ClockTicker;
import metagram.metaitem.Item;
import metagram.metaitem.Item.ItemType;
import metagram.metaitem.ItemImpl;
import metagram.metaitem.ItemStorage;

/**
 *
 * @author z
 */
public class Element extends WorldObject implements ClockTicker {
 
    private double energy = 100;
    private int ffill = 245;
    int health = 100;
    
    private Stash stash;
    private Loc target;
        
    int twait = 0;
    
    private List<FarmPlot> plots = new ArrayList<>();
    private FarmPlot jobPlot;
    
    public int[] skillTable = new int[16];
    
    private ArrayList<ItemStorage> inventory = new ArrayList<ItemStorage>();
    private HashMap<Item,Double> values = new HashMap<>();
    
    enum liveState {
        ST_SLEEP,
        ST_IDLE,
        ST_MOVE,
        ST_JWAIT,
        ST_DEAD,
    }
    
    enum liveMode {
        FEED,
        IDLE,
        PLOT,
    }
    
    private liveMode mode = liveMode.IDLE;
    
    public Element() {
    
    }
    
    private JobWait jobTarget;
    private int jobArg = 0;
    
    public void jobWaitStart(JobWait target, int mode, int arg) {
        jobTarget = target;
        jobArg = arg;
        state = liveState.ST_JWAIT;
        twait = target.ticksToWait(mode);
    }
    
    private void jobFinish() {
        List<ItemStorage> result = jobTarget.jobResult(jobArg);
        
        if (result != null) {
            for (ItemStorage its : result) {
                if (its.quantity > 0) {
                    inventory.add(its);
                }
            }
        }
        state = liveState.ST_IDLE;
    }
    
    private liveState state = liveState.ST_IDLE;
    
    private void feed() {
        mode = liveMode.FEED;
        Iterator<ItemStorage> it = inventory.iterator();
        boolean result = false;
        while (it.hasNext()) {
            ItemStorage its = it.next();
            
            if (its.item.getType() == ItemType.FOOD) {
                if (ffill < 220) {
                    its.quantity--;
                    ffill += 100;                    
                }
                else {
                    if (stash.pos.equals(pos)) {
                        stash.store(its);
                        its.quantity = 0;
                    }
                    else {
                        startMove(stash.pos);
                    }
                }
                result = true;
            }
            if (its.quantity == 0) {
                it.remove();
            }
            if (result) {
                mode = liveMode.IDLE;
                return;
            }
        }
        
        if (ffill < 220) {
            if (stash != null && stash.getQuantity(ItemImpl.createFood()) > 0) {
                if (stash.pos.equals(pos)) {
                    ItemStorage its = stash.getItem(ItemImpl.createFood());
                    if (its != null && its.quantity > 0) {
                        its.quantity--;
                        ffill += 250;
                        mode = liveMode.IDLE;
                        return;
                    }
                }
                else {
                    startMove(stash.pos);
                }
            }
        }
        
        List<WorldObject> l = Metagram.instance.lookForItem(ItemImpl.createFood());
        
        if (!l.isEmpty()) {
            WorldObject lwo = null;
            double dist = Double.MAX_VALUE;
            for (WorldObject wo : l) {
                if (wo.pos.equals(pos)) {
                    if (wo instanceof FoodSource) {
                        jobWaitStart((JobWait)wo,0,1);
                        return;
                    }                    
                }
                double d = (wo.pos.x-pos.x)*(wo.pos.x-pos.x)+(wo.pos.y-pos.y)*(wo.pos.y-pos.y);
                if (d < dist) {
                    dist = d;
                    lwo = wo;
                }
            }
            startMove(lwo.pos);
        }
    }
    
    private void doPlotJob() {
        mode = liveMode.PLOT;
        boolean found = false;
        if (jobPlot != null && jobPlot.glRate < 100) {
            found = true;
        }
        else {        
            for (FarmPlot fj : plots) {
                if (fj.glRate >= 100) {
                    jobPlot = fj;
                    found = true;
                }
            }
            if (!found) {
                for (FarmPlot fj : plots) {
                    if (fj.glRate < 100) {
                        jobPlot = fj;
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            Loc l = Metagram.instance.claimPlot(stash.pos,this);
            FarmPlot fp = Metagram.instance.deployPlot(l);
            plots.add(fp);
            jobPlot = fp;
        }
        if (!jobPlot.getPos().equals(pos)) {
            startMove(jobPlot.pos);
            return;
        }
        
        if (jobPlot.glRate >= 100) {
            jobWaitStart(jobPlot,1,FarmPlot.JOB_HARVEST);
        }
        else {
            jobWaitStart(jobPlot,0,FarmPlot.JOB_GROW);
        }
        
    }
    
    private void findJob() {
        int r = Metagram.instance.random.nextInt(1000);
       
        if (r < 600) {
            if (skillTable[0] > 0) {
                doPlotJob();
            }
        }
        else {
            feed();
        }
    }
    
    private void decide() {
        
        if (ffill < 220 && mode != liveMode.FEED) {
            feed();
        }
        else {
            switch (mode) {
                case FEED:
                    feed();
                    break;
                case IDLE:
                    findJob();
                    break;
                case PLOT:
                    doPlotJob();
                    break;
            }
        }
    }
    
    
        
    public void tick() {
        if (health == 0) return;
        
        if (stash == null) {
            stash = (Stash)Metagram.instance.setStash(pos);
        }
        switch (state) {
            case ST_SLEEP:
                energy += 2;
                if (energy > 200) {
                    state = liveState.ST_IDLE;
                }
                if (health < 100) {
                    health++;
                }
                break;
            case ST_MOVE:
                move();
                break;
            case ST_JWAIT:
                twait--;
                if (twait == 0) {
                    jobFinish();
                }
                break;
            case ST_IDLE:
                decide();
                break;
        }
        
        if (state != liveState.ST_SLEEP) {
            energy--;
        }
        if (energy <= 0) {
            state = liveState.ST_SLEEP;
        }
        ffill--;
        if (ffill <= 0) {
            ffill = 0;
            health--;
        }
        
        if (health == 0) {
            state = liveState.ST_DEAD;
        }
    }
    
    private void move() {
        int x = target.x-pos.x;
        int y = target.y-pos.y;
        
        double c = Math.abs(x/(Math.sqrt(x*x+y*y)));
        if (c < 0.5) {
            pos.y = (y > 0 ? pos.y+1 : pos.y-1);
        }
        else if (c < 0.866) {
            pos.x = (x > 0 ? pos.x+1 : pos.x-1);
            pos.y = (y > 0 ? pos.y+1 : pos.y-1);
        }
        else {
            pos.x = (x > 0 ? pos.x+1 : pos.x-1);
        }
        
        if (target.equals(pos)) {
            state = liveState.ST_IDLE;
        }
        
    }
    
    public void startMove(Loc target) {
        this.target = target;
        state = liveState.ST_MOVE;
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }
    

    @Override
    public Color getSecColor() {
        if (state == liveState.ST_DEAD) {
            return Color.black;
        }
        else {
            return super.getSecColor();
        }
    }

    @Override
    public String[] desc() {
        String[] ss = new String[64];
        int k = 0;
        ss[k++] = "energy="+energy;
        ss[k++] = "fill="+ffill;
        switch (state) {
            case ST_IDLE:
                ss[k++] = "state=idle";
                break;
            case ST_MOVE:
                ss[k++] = "state=move";
                break;
            case ST_JWAIT:
                ss[k++] = "state=jwait";
                ss[k++] = "twait="+twait;
                break;
            case ST_SLEEP:
                ss[k++] = "state=sleep";
                break;
        }
        
        ss[k++] = "mode="+mode;
        
        String[] ss2 = new String[k];
        int i;
        for(i=0;i<k;i++) {
            ss2[i] = ss[i];
        }
        return ss2;
        
    }
    
    public double perceivedValue(Item it) {
        if (!values.containsKey(it)) {
            values.put(it,100.0);
        }
        
        return values.get(it);
    }

    @Override
    public String getName() {
        return "Element";
    }

    
}
