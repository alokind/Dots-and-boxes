package etf.dotsandboxes.dn160597d;
public class LinijaIHeuristika implements Comparable<LinijaIHeuristika>{
    private int heuristika;
    private Linija linija;

    LinijaIHeuristika(Linija linija, int utility) {
        this.linija = linija;
        this.heuristika = utility;
    }

    int getHeuristika() {
        return heuristika;
    }

    Linija getLinija() {
        return this.linija;
    }

    void setLinija(Linija linija) {
        this.linija = linija;
    }
    void setHeuristika(int utility) {
        this.heuristika = utility;
    }

    @Override
    public int compareTo(LinijaIHeuristika linijaIHeuristika) {
        return this.heuristika - linijaIHeuristika.heuristika;
    }
}
