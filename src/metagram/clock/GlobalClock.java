/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.clock;

import java.util.ArrayList;

/**
 *
 * @author z
 */
public class GlobalClock extends Thread {
    
    private long period; 
    

    private ArrayList<ClockTicker> tickers = new ArrayList<>();
    
    public GlobalClock(long period) {
        this.period = period;
    }
    
    public void addTicker(ClockTicker ct) {
        synchronized(this) {
            tickers.add(ct);
        }
    }
    
    public void init() {
        start();
    }
    
    public void run() {
        try {
            for(;;) {
                Thread.sleep(period);
                synchronized(this) {
                    for (ClockTicker ct : tickers) {
                        ct.tick();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPeriod(long period) {
        this.period = period;
    }
    
}
