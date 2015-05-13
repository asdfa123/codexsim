/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.market;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import metagram.elements.Element;
import metagram.elements.WorldObject;
import metagram.metaitem.Item;
import metagram.metaitem.ItemStorage;

/**
 *
 * @author z
 */
public class Market extends WorldObject {

    @Override
    public void tick() {
        synchronized(this) {
            if (newOrder) {
                marketPeriod();
            }
            newOrder = false;
        }
    }
    
    boolean newOrder = false;
    
    class Offer {
        int type;
        Item base;
        int quantity;
        Item tradeOff;
        double rate;
        Agent agent;

        public Offer(int type, Item base, int quantity, Item tradeOff, double rate,Agent agent) {
            this.type = type;
            this.base = base;
            this.quantity = quantity;
            this.tradeOff = tradeOff;
            this.rate = rate;
            this.agent = agent;
        }
        
        public Offer(int type, Item base, int quantity, Item tradeOff, double rate) {
            this.type = type;
            this.base = base;
            this.quantity = quantity;
            this.tradeOff = tradeOff;
            this.rate = rate;
        }
        
    }
    
    List<Offer> closedBids = new ArrayList<>();
    List<Offer> closedAsks = new ArrayList<>();
    
    List<Offer> bids = new ArrayList<>();
    List<Offer> asks = new ArrayList<>();
    
    public void bid(int quantity, Item base, Item tradeOff, double rate, Agent agent) {
        synchronized(this) {
            Offer offer = new Offer(1,base,quantity,tradeOff,rate,agent);
            bids.add(offer);
            newOrder = true;
        }
    }
    
    public void ask(int quantity, Item base, Item tradeOff, double rate, Agent agent) {
        synchronized(this) {
            Offer offer = new Offer(0,base,quantity,tradeOff,rate,agent);
            asks.add(offer);
            newOrder = true;
        }
    }
    
    public void marketPeriod() {        
        match();        
    }
    
    public PersonalStorage getStorage(Element element) {
        PersonalStorage ps = localStorage.get(element);
        if (ps == null) {
            ps = new PersonalStorage();
            localStorage.put(element,ps);
        }
        return ps;
    }
    
    private void match() {
        Iterator<Offer> bt,at;
        bt = bids.iterator();
        at = asks.iterator();
        while (bt.hasNext()) {
            Offer b = bt.next();
            boolean bclose = false;
            while (at.hasNext()) {
                Offer a = at.next();
                if (b.base.eqType(a.base) && b.tradeOff.eqType(a.tradeOff)) {
                    if (b.rate >= a.rate) {
                        // order matched
                        int qt = Math.min(a.quantity,b.quantity);
                        int av = b.agent.available(b.tradeOff);
                        
                        int qta = a.agent.available(a.base);
                        if (qta < qt) {
                            qt = qta;
                        }
                            
                        
                        double rate = b.rate;
                        int vflow = (int)(rate*qt);
                        
                        if (vflow > av) {
                            int qt2 = (int)(av/rate);
                            qt = qt2;
                            vflow = av;
                        }
                        
                        Offer offerb = new Offer(1,b.base,qt,b.tradeOff,b.rate);
                        closedBids.add(offerb);
                        
                        Offer offera = new Offer(0,a.base,qt,a.tradeOff,a.rate);
                        closedAsks.add(offera);
                        
                        a.quantity -= qt;
                        b.quantity -= qt;
                        
                        a.agent.execute(1, a.base, qt);
                        a.agent.execute(0, a.tradeOff, vflow);
                        
                        b.agent.execute(0, a.base, qt);
                        b.agent.execute(1, a.tradeOff, vflow);
                        
                        if (a.quantity == 0) {
                            at.remove();
                        }
                        if (b.quantity == 0) {
                            bt.remove();
                            bclose = true;
                        }
                    }
                }
            }
            if (bclose) {
                continue;
            }
        }
    }
    
    HashMap<Element,PersonalStorage> localStorage = new HashMap<>();
    
    public class PersonalStorage implements Agent {
        HashMap<Item,ItemStorage> localStorage = new HashMap<>();

        @Override
        public int available(Item it) {
            return localStorage.get(it).quantity;
        }

        @Override
        public void execute(int mode, Item it, int quantity) {
            if (mode == 0) {
                localStorage.get(it).quantity += quantity;
            }
            else if (mode == 1) {
                localStorage.get(it).quantity -= quantity;
            }
        }
    }

    @Override
    public Color getColor() {
        return Color.white;
    }

    @Override
    public Color getSecColor() {
        return Color.BLACK;
    }

    @Override
    public String getName() {
        return "Market";
    }
    
}
