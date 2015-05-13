/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;
import metagram.elements.Element;
import metagram.elements.WorldObject;

/**
 *
 * @author z
 */
public class TiledGrid extends JPanel implements MouseMotionListener {

    private int centerX, centerY;
    private InfoUI infoUI;
    
    private List<WorldObject> objs = new ArrayList<>();

    Graphics offgc;
    Image offscreen = null;
    public TiledGrid() {
        setSize(600,600);
        this.setPreferredSize(new Dimension(600,600));
        this.setMaximumSize(new Dimension(600,600));
        offscreen = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        offgc = offscreen.getGraphics();
    }
    
    public void setCenter(int x, int y) {
        synchronized(this) {
            centerX = x;
            centerY = y;
        }
    }
    
    int w = 20;
    
    void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        int i,j;
        
        for(i=0;i<600/w;i++) {
            
            for(j=0;j<600/2;j++) {
                g.drawLine(0, j*w, 600, j*w);
                g.drawLine(i*w, 0, i*w, 600);                
            }            
        }
    }
    
    int[][] sshape1 = {{-4,3},{5,3},{0,-4}};
    
    void drawObjs(Graphics g) {
        for (WorldObject wo : objs) {
            int i;
            Loc l = wo.getPos();
            Loc l2 = new Loc();
            l2.x = l.x*(w) - w/2;
            l2.y = l.y*(w) - w/2;
            
            l2.x += centerX*w;
            l2.y += centerY*w;
            
            if (wo instanceof Element) {
                l2.x++;
                l2.y++;
            }
            switch (wo.getShape()) {
                case 0:
                    g.setColor(wo.getSecColor());
                    g.fillRect(l2.x-2,l2.y-2,4,4);
                    g.setColor(wo.getColor());
                    g.drawRect(l2.x-3,l2.y-3,5,5);
                    break;
                case 1:
                    for(i=0;i<3;i++) {
                        g.setColor(wo.getSecColor());
                        g.fillRect(l2.x-2+sshape1[i][0],l2.y-2+sshape1[i][1],4,4);
                        g.setColor(wo.getColor());
                        g.drawRect(l2.x-3+sshape1[i][0],l2.y-3+sshape1[i][1],5,5);
                    }
                    break;
                case 2:
                    g.setColor(wo.getSecColor());
                    g.fillRect(l2.x-6,l2.y-6,12,12);
                    g.setColor(wo.getColor());
                    g.drawRect(l2.x-7,l2.y-7,13,13);
                    break;
            }
            
            
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        synchronized(this) {
            setBackground(new Color(40,128,40));
            //offgc.setColor(new Color(40,128,40));
            offgc.setColor(Color.LIGHT_GRAY);
            offgc.fillRect(0, 0, 600, 600);
            drawGrid(offgc);
            drawObjs(offgc);
            
            g.drawImage(offscreen, 0, 0, this);
        }
        
    }
    
    class Sorter implements Comparator<WorldObject> {

        @Override
        public int compare(WorldObject o1, WorldObject o2) {
            if (o2 instanceof Element) {
                return -1;
            }
            if (o1 instanceof Element) {
                return 1;
            }
            return 0;
        }
        
    }
    
    public void remove(WorldObject wo) {
        
        synchronized(this) {
            Iterator<WorldObject> it = objs.iterator();
            while (it.hasNext()) {
                if (it.next() == wo) {
                    it.remove();
                }
            }
        }
    }
    
    public void addObject(WorldObject wo) {
        synchronized(this) {
            objs.add(wo);
            Collections.sort(objs, new Sorter());
        }
        repaint();
    }

    public void centerUp() {
        centerY++;
    }
    
    public void centerDown() {
        centerY--;
    }
    
    public void centerRight() {
        centerX--;
    }
    
    public void centerLeft() {
        centerX++;
    }
    
    public Loc toWorldCoord(int x, int y) {
        int xb = x/w;
        int yb = y/w;
        return new Loc(xb-centerX+1,yb-centerY+1);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Loc l = toWorldCoord(e.getX()-this.getX(),e.getY()-this.getY());
     //   System.out.println("loc "+l+" "+e.getY());
        infoUI.setData(l);
        infoUI.repaint();
        repaint();
    }

    public void setInfoUI(InfoUI infoUI) {
        this.infoUI = infoUI;
    }
}
