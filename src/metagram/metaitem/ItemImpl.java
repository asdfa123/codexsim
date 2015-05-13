/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.metaitem;

/**
 *
 * @author z
 */
public class ItemImpl implements Item {
    ItemType type;
    
    static Item foodImpl = new ItemImpl(ItemType.FOOD);

    public ItemImpl(ItemType type) {
        this.type = type;
    }
    
    public static Item createFood() {
        return foodImpl;
    }

    @Override
    public ItemType getType() {
        return type;
    }

    @Override
    public boolean eqType(Item it) {
        return type == it.getType();
    }
}
