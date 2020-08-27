package etf.dotsandboxes.dn160597d;
public abstract class Racunar {

	protected int depth;
	
	protected Igra igra;
	
    protected int mojaBoja;

    public abstract RacunarRezultat getResult(final Tabla tabla, int color);
    
    public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
