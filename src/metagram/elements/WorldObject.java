/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.awt.Color;
import metagram.Loc;

/**
 *
 * @author z
 */
public abstract class WorldObject {
    
    static int w_id = 1;
    int id;
    
    public WorldObject() {
        id = w_id++;
    }
    
    protected Loc pos = new Loc();
    
    public Color getColor() {
        return Color.BLACK;
    }
    
    public Color getSecColor() {
        return Color.WHITE;
    }

    public Loc getPos() {
        return pos;
    }
    
    public abstract void tick();
    
    public void setPos(Loc l) {
        pos = new Loc(l.x,l.y);
    }

    public int getId() {
        return id;
    }
    
    public String[] desc() {
        return new String[0];
    }

    public int getShape() {
        return 0;
    }
    
    public String getName() {
        return "Object";
    }
}
