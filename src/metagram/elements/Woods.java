/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram.elements;

import java.awt.Color;

/**
 *
 * @author z
 */
public class Woods extends Static {
    int quantity=100;

    @Override
    public Color getColor() {
        return new Color(10,80,10);
    }

    @Override
    public Color getSecColor() {
        return Color.green;
    }

    @Override
    public int getShape() {
        return 1;
    }

    @Override
    public String getName() {
        return "Trees";
    }

}
