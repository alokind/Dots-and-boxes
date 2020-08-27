package etf.dotsandboxes.dn160597d;
import java.awt.Point;
import java.util.ArrayList;

public class Tabla implements Cloneable {
	

    private int crveniRezultat, plaviRezultat;
    private int n, m;
    
    private int[][] polja;
    private int[][] hLinije;
    private int[][] vLinije;
    
    static int CRVENA = 0;
    static int PLAVA = 1;
    static int CRNA = 2;
    static int PRAZNO = 3;
    
    public Tabla(int m, int n, ArrayList<Linija> potezi) {
        hLinije = new int[m+1][n];
        vLinije = new int[m][n+1];
        polja = new int[m][n];
        
        setSve(hLinije,PRAZNO);
        setSve(vLinije,PRAZNO);
        setSve(polja,PRAZNO);
        
        for (Linija potez : potezi) {
      	  if (potez.isHorizontalno()) {
      		  hLinije[potez.getX()][potez.getY()] = CRNA;
      	  }
      	  else{
      		  vLinije[potez.getX()][potez.getY()] = CRNA;
      	  }
        }
        
        this.n = n;
        this.m = m;
        crveniRezultat = plaviRezultat = 0;
    }
    
    public Tabla(int m, int n) {
        hLinije = new int[m+1][n];
        vLinije = new int[m][n+1];
        polja = new int[m][n];
    	
        setSve(hLinije,PRAZNO);
        setSve(vLinije,PRAZNO);
        setSve(polja,PRAZNO);
        this.n = n;
        this.m = m;
        crveniRezultat = plaviRezultat = 0;
    }
    
    

    public void sethLinije(int[][] hLinije) {
        for(int i=0; i<m+1; i++)
            for(int j=0; j<n; j++)
                this.hLinije[i][j] = hLinije[i][j];
	}

	public void setvLinije(int[][] vLinije) {
        for(int i=0; i<m; i++)
            for(int j=0; j<n+1; j++)
                this.vLinije[i][j] = vLinije[i][j];
	}
	
	public void setPolja(int[][] polje) {
        for(int i=0; i<m; i++)
            for(int j=0; j<n; j++)
                this.polja[i][j] = polja[i][j];
	}
	
	public Tabla clone() {
        Tabla cloned = new Tabla(m, n);
        
        cloned.crveniRezultat = crveniRezultat;
        cloned.plaviRezultat = plaviRezultat;
        
        cloned.sethLinije(hLinije);
        cloned.setvLinije(vLinije);
        cloned.setPolja(polja);

        return cloned;
    }

    private void setSve(int[][] array, int val) {  	
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    }

    public int getSize() { 
    	return n; 
    }

    public int getCrveniRezultat() {
        return crveniRezultat;
    }

    public int getPlaviRezultat() {
        return plaviRezultat;
    }

    public int getRezultat(int color) {
        if(color != PLAVA) return crveniRezultat;
        else return plaviRezultat;
    }

    public static int promeniBoju(int color) {
        if(color != PLAVA)
            return PLAVA;
        else
            return CRVENA;
    }

    public ArrayList<Linija> dohvatiMogucePoteze() {

        ArrayList<Linija> ret = new ArrayList<Linija>();
        for(int i=0; i<m; i++)
            for(int j=0; j<n+1; j++)
                if(vLinije[i][j] == PRAZNO)
                    ret.add(new Linija(i,j,false));
        for(int i=0; i<m+1; i++)
            for(int j=0; j<n; j++)
                if(hLinije[i][j] == PRAZNO)
                    ret.add(new Linija(i,j,true));
        return ret;
    }

    public ArrayList<Point> sethLiniju(int x, int y, int color) {
    	
        hLinije[x][y]=CRNA;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(x>0 && 
        		vLinije[x-1][y]==CRNA && 
        		vLinije[x-1][y+1]==CRNA && 
        		hLinije[x-1][y]==CRNA) {
            polja[x-1][y]=color;
            ret.add(new Point(x-1,y));
            if(color != PLAVA) crveniRezultat++;
            else plaviRezultat++;
        }
        if(x<m && 
        		vLinije[x][y]==CRNA && 
        		vLinije[x][y+1]==CRNA && 
        		hLinije[x+1][y]==CRNA) {
            polja[x][y]=color;
            ret.add(new Point(x,y));
            if(color != PLAVA) crveniRezultat++;
            else plaviRezultat++;
        }
        return ret;
    }

    public ArrayList<Point> setvLiniju(int x, int y, int color) {

    	vLinije[x][y]=CRNA;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(y<n && 
        		hLinije[x][y]==CRNA && 
        		hLinije[x+1][y]==CRNA && 
        		vLinije[x][y+1]==CRNA) {
            polja[x][y]=color;
            ret.add(new Point(x,y));
            if(color != PLAVA) crveniRezultat++;
            else plaviRezultat++;
        }
        if(y>0 && 
        		hLinije[x][y-1]==CRNA && 
        		hLinije[x+1][y-1]==CRNA && 
        		vLinije[x][y-1]==CRNA) {
            polja[x][y-1]=color;
            ret.add(new Point(x,y-1));
            if(color != PLAVA) crveniRezultat++;
            else plaviRezultat++;
        }
        return ret;
    }
    
    public int pocetnikPotezHeuristika(Linija linija) {
    	int x = linija.getX();
    	int y = linija.getY();
    	boolean isHorizontal = linija.isHorizontalno();
    	
    	int ret = 0;
    	
    	if (isHorizontal) {
    		if(x<m && 
            		vLinije[x][y]==CRNA && 
            		vLinije[x][y+1]==CRNA && 
            		hLinije[x+1][y]==CRNA)
                ret++;
            if(x>0 && 
            		vLinije[x-1][y]==CRNA && 
            		vLinije[x-1][y+1]==CRNA && 
            		hLinije[x-1][y]==CRNA) 
                ret++;	
    	}
    	else {
    		if(y<n && 
            		hLinije[x][y]==CRNA && 
            		hLinije[x+1][y]==CRNA && 
            		vLinije[x][y+1]==CRNA) 
    			ret++;
            if(y>0 && 
            		hLinije[x][y-1]==CRNA && 
            		hLinije[x+1][y-1]==CRNA && 
            		vLinije[x][y-1]==CRNA) 
            	ret++;
    	}
    	
    	return ret;
    }

    public boolean isKrajIgre() {
    	if(m*n==crveniRezultat + plaviRezultat)
    		return true;
    	else 
    		return false;
    }

    public int getWinner() {
    	if(crveniRezultat == plaviRezultat) 
    		return PRAZNO;
        if(crveniRezultat > plaviRezultat) 
        	return CRVENA;
        else
        	return PLAVA;
    }

    public Tabla novaTablaSaLinijom(Linija linija, int color) {
        Tabla ret = clone();
        if(!linija.isHorizontalno())
        	ret.setvLiniju(linija.getX(), linija.getY(), color);
        else
        	ret.sethLiniju(linija.getX(), linija.getY(), color);
        return ret;
    }

    public int brojKutijaSaIvicama(int nSides) {

        int count = 0;
        for(int i=0; i<m; i++)
            for(int j=0; j<n; j++) {
                int countIvice = 0;
                if(hLinije[i+1][j] == CRNA) count++;
                if(vLinije[i][j] == CRNA) count++;
                if(hLinije[i][j] == CRNA) count++;
                if(vLinije[i][j+1] == CRNA) count++;
                if(countIvice == nSides)
                    count++;
            }
        return count;
    }

}
