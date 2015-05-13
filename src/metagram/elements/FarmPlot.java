/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import metagram.metaitem.Item;
import metagram.metaitem.ItemImpl;
import metagram.metaitem.ItemStorage;

/**
 *
 * @author z
 */
public class FarmPlot extends Static implements JobWait {
    public int glRate = 0; 
    
    public static final int JOB_GROW = 0;
    public static final int JOB_HARVEST = 1;
    
    public int ticksToWait(int mode) {
        switch (mode) {
            case JOB_GROW:
                return 30;
            case JOB_HARVEST:
                return 50;
        }        
        return 1;
        
    }
    
    public List<ItemStorage> jobResult(int param1) {
        if (param1 == JOB_GROW) {
            glRate += 10;
        }
        else if (param1 == JOB_HARVEST) {
            ArrayList<ItemStorage> l = new ArrayList<>();
            ItemStorage its = new ItemStorage();
            its.item = ItemImpl.createFood();
            its.quantity = 30;
            glRate = 0;
            l.add(its);
            return l;
        }
        return null;
    }

    @Override
    public int jobType() {
        return 1|2;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public String[] desc() {
        String[] ss = new String[1];
        int k=0;
        ss[k++] = "Growth="+glRate;
        
        return ss;
    }

    @Override
    public int getShape() {
        return 2;
    }

    @Override
    public String getName() {
        return "Farm";
    }
    
}
