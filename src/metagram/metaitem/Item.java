/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.metaitem;

/**
 *
 * @author z
 */
public interface Item {
    public enum ItemType {
        FOOD
    }
    
    public ItemType getType();
    
    public boolean eqType(Item it);
}
