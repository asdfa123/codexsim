/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.util.ArrayList;
import java.util.List;
import metagram.metaitem.Item;
import metagram.metaitem.ItemImpl;
import metagram.metaitem.ItemStorage;
import metagram.Metagram;

/**
 *
 * @author z
 */
public class FoodSource extends Static implements JobWait {
    
    public int quantity;
    public int ticksToHarvest = 10;
    
    public FoodSource() {
        quantity = 10;
    }
    
    public int harvest(int qty) {
        if (qty < quantity) {
            quantity -= qty;            
        }
        else {
            qty = quantity;
            quantity = 0;
        }
        if (quantity <= 0) {
            Metagram.instance.destroy(this);
        }
        return qty;
    }

    @Override
    public int ticksToWait(int mode) {
        return ticksToHarvest;
    }

    @Override
    public int jobType() {
        return 1;
    }

    @Override
    public List<ItemStorage> jobResult(int param1) {
        ArrayList<ItemStorage> l = new ArrayList<>();
        Item i = ItemImpl.createFood();
        ItemStorage its = new ItemStorage();
        its.item = i;
        its.quantity = harvest(param1);
        l.add(its);        
        return l;
    }

    @Override
    public String[] desc() {
        String[] ss = new String[1];
        ss[0] = "available "+quantity;
        return ss;
    }

    @Override
    public String getName() {
        return "FoodSource";
    }

}
