/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import metagram.clock.ClockTicker;
import metagram.clock.GlobalClock;
import metagram.elements.*;
import metagram.market.Market;
import metagram.metaitem.Item;
import metagram.metaitem.Item.ItemType;

/**
 *
 * @author z
 */
public class Metagram implements ClockTicker {
    
    static GlobalClock clock = new GlobalClock(200);
    
    public TiledGrid grid;
    
    Frame frame;
    
    private ArrayList<WorldObject> objs = new ArrayList<>();
    private ArrayList<WorldObject> delayed = new ArrayList<>();
    private ArrayList<WorldObject> toDestroy = new ArrayList<>();
    
    public static Metagram instance;
    
    HashMap<Loc,WorldObject> stashes = new HashMap<>();
    HashMap<Loc,Element> owners = new HashMap<>();
    
    List<Market> markets = new ArrayList<>();
    
    public Metagram() {
        instance = this;
    }
    
    public void destroy(WorldObject obj) {
        synchronized(this) {
            toDestroy.add(obj);
        }
    }
    
    private void removeObject(WorldObject e) {
        objs.remove(e);
        grid.remove(e);
    }
    
    private void addObject(WorldObject e) {
        objs.add(e);
        grid.addObject(e);
    }
    
    public static Random random = new Random(3445);
    
    public void init() {
        int i;
        for(i=0;i<4;i++) {
            Element e = new Element();
            e.getPos().x = random.nextInt(30);
            e.getPos().y = random.nextInt(30);
            e.skillTable[0] = 1;
            addObject(e);
        }
        
        
        for(i=0;i<5;i++) {
            FoodSource fs = new FoodSource();
            fs.setPos(new Loc(random.nextInt(20),random.nextInt(20)));
            addObject(fs);
        }
        
        Woods w = new Woods();
        w.setPos(new Loc(3,14));
        addObject(w);
        
        clock.addTicker(this);
        
       // ((Element)e).startMove(new Loc(10,10));
    }

    public WorldObject setStash(Loc l) {
        WorldObject wo;
        if (stashes.containsKey(l)) {
            return null;
        }
        if (owners.containsKey(l)) {
            return null;
        }
        wo = new Stash();
        wo.setPos(l);
        stashes.put(l,wo);
        delayed.add(wo);
        return wo;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        clock.init();
        Metagram metaGram = new Metagram();
        
        Frame f = new Frame();
        f.run();
        metaGram.frame = f;
        
        metaGram.grid = f.getGrid();
        metaGram.init();
        
        synchronized(metaGram) {
            metaGram.wait();
        }
    }
    
    public void pause() {
        synchronized(this) {
            paused = !paused;
        }
    }
    
    boolean paused = false;
    
    private long globalTime = 0;

    @Override
    public void tick() {
        synchronized(this) {
            if (paused) {
                return;
            }
            globalTime++;
            for (WorldObject wo : objs) {
                wo.tick();
            }
            for (WorldObject wo : delayed) {
                addObject(wo);
            }
            delayed.clear();
            for (WorldObject wo : toDestroy) {
                removeObject(wo);
            }
            toDestroy.clear();
            frame.repaint();
            
        }
        
    }
    
    public List<WorldObject> lookForItem(Item it) {
        ArrayList<WorldObject> l = new ArrayList<>();
        if (it.getType() == ItemType.FOOD) {
            for(WorldObject obj : objs) {
                if (obj instanceof FoodSource && ((FoodSource)obj).quantity > 0) {
                    l.add(obj);
                }
//                if (obj instanceof Stash) {
//                    if (((Stash)obj).getQuantity(it) > 0) {
//                        l.add(obj);
//                    }
//                }
            }
        }
        
        return l;
    }
    
    public Loc claimPlot(Loc base, Element e) {
        int i = base.x+1;
        int j = base.y+1;
        int phase = -1;
        int xl = 0;
        int k = 0;
        int cc = 2;
        do {
            for(;k<cc;k++) {
                Loc l = new Loc(i,j);
                if (!owners.containsKey(l)) {
                    owners.put(l,e);
                    return l;
                }
                i += phase;
            }
            if (xl == 0) {
                xl = 1;
            }
            else {
                if (phase == 1) {
                    i++;
                    cc++;
                }
                phase *= -1;
                xl = 0;
            }
            k = 0;
        } while(true);
    }
    
    public FarmPlot deployPlot(Loc l) {
        FarmPlot fp = new FarmPlot();
        fp.setPos(l);
        delayed.add(fp);
        return fp;
    }
    
    public List<WorldObject> getTileObjs(Loc l) {
        ArrayList<WorldObject> wl = new ArrayList<>();
        synchronized(this) {
            for (WorldObject wo : objs) {
                if (wo.getPos().equals(l)) {
                    wl.add(wo);
                }
            }
        }
        return wl;
    }
}
