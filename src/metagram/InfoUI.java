/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;
import metagram.elements.Element;
import metagram.elements.WorldObject;

/**
 *
 * @author z
 */
public class InfoUI extends JPanel {
    
    Loc data = null;

    public void setData(Loc data) {
        this.data = data;
    }
    
    public InfoUI() {
        this.setPreferredSize(new Dimension(300,600));
    }

    @Override
    protected void paintComponent(Graphics g) {
       
        g.setColor(Color.white);
        g.fillRect(0, 0, 300, 600);
        g.setColor(Color.black);

        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        if (data != null) {
            List<WorldObject> l = Metagram.instance.getTileObjs(data);
            //System.out.println("Got "+l.size());
            int j = 100;
            for (WorldObject wo : l) {
                g.drawString("ID="+wo.getId()+" "+wo.getName(), 30, j);
                j += 20;
                for (String s : wo.desc()) {
                    g.drawString(s, 30, j);
                    j += 20;
                }
                
                j += 20;
                g.drawString("-----------", 30, j);
                j += 40;
                
            }            
        }
        
    }

}
