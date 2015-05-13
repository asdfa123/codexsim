/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metagram;

/**
 *
 * @author z
 */
public class Loc {
    public int x,y;
    
    public Loc() {
        
    }

    public Loc(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Loc other = (Loc) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.x;
        hash = 79 * hash + this.y;
        return hash;
    }

    @Override
    public String toString() {
        return x+" "+y;
    }

    
    
}
