package etf.dotsandboxes.dn160597d;
public class Linija {

    private int x, y;
    private boolean horizontalno;

    Linija() {
    }

    Linija(int x, int y, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.horizontalno = horizontal;
    }

    public boolean isHorizontalno() {
        return horizontalno;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
    	String potez = "";
    	if (horizontalno) {
    		potez+=(char)('0'+x);
    		potez+=(char)('A'+y);
    	}else {
    		potez+=(char)('A'+x);
    		potez+=(char)('0'+y);
    	}
        return potez;
    }

}
