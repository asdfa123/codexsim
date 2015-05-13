/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.market;

import metagram.metaitem.Item;

/**
 *
 * @author z
 */
public interface Agent {
    
    public int available(Item it);
    
    public void execute(int mode, Item it, int quantity);
}
