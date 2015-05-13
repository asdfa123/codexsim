/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.util.List;
import metagram.metaitem.Item;
import metagram.metaitem.ItemStorage;

/**
 *
 * @author z
 */
public interface JobWait {
    
    public int ticksToWait(int mode);
    
    public int jobType();
    
    public List<ItemStorage> jobResult(int param1);
}
