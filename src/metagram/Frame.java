/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author z
 */
public class Frame extends JFrame implements KeyListener, MouseListener {
    private JPanel mainArea;
    private TiledGrid grid = new TiledGrid();
    private InfoUI infoUI = new InfoUI();
    
    public Frame() {
        grid.setInfoUI(infoUI);
        mainArea = new JPanel(new BorderLayout());
        this.addKeyListener(this);
        grid.addMouseMotionListener(grid);
    }
    
    public void run() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("G1");                
        setResizable(false);        
        setLocation(300,5);
        setSize(600, 600);      

        add(BorderLayout.WEST,mainArea);
        mainArea.add(grid);
        add(BorderLayout.EAST,infoUI);      
        
        pack();
        revalidate();
        setVisible(true);
        
    }

    public TiledGrid getGrid() {
        return grid;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            grid.centerUp();        
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            grid.centerDown();        
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            grid.centerLeft();        
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            grid.centerRight();        
        }
        
        switch (e.getKeyCode()) {
            case ' ':
                Metagram.instance.pause();
                break;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    
    
}
