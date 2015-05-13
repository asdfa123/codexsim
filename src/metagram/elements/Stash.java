/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.awt.Color;
import java.util.HashMap;
import metagram.metaitem.Item;
import metagram.metaitem.ItemStorage;

/**
 *
 * @author z
 */
public class Stash extends Static {
    
    HashMap<Item,ItemStorage> storage = new HashMap<>();
    
    @Override
    public Color getColor() {
        return new Color(200,200,200);
    }
    
    public int getQuantity(Item it) {
        if (!storage.containsKey(it)) {
            return 0;
        }
        return storage.get(it).quantity;
    }
    
    public ItemStorage getItem(Item type) {
        return storage.get(type);
    }
    
    public void store(ItemStorage its) {
        ItemStorage its2 = storage.get(its.item);
        if (its2 == null) {
            its2 = new ItemStorage();
            its2.item = its.item;
            its2.quantity = its.quantity;
            storage.put(its.item,its2);
        }
        else {
            its2.quantity += its.quantity;
        }
    }

    @Override
    public String[] desc() {
        String[] ss2 = new String[64];
        int k = 0;
        for (ItemStorage its : storage.values()) {
            ss2[k++] = its.item.getType()+" "+its.quantity;
        }
        String[] ss = new String[k];
        int i;
        for(i=0;i<k;i++) {
            ss[i] = ss2[i];
        }
        
        return ss;
    }

    @Override
    public String getName() {
        return "Stash";
    }

}
