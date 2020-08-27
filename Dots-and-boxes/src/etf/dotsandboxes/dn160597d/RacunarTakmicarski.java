package etf.dotsandboxes.dn160597d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RacunarTakmicarski extends Racunar{

    public RacunarRezultat getResult(Tabla tabla, int color) {
        mojaBoja = color ;
        
	    Linija best = alfaBeta(tabla, color, Integer.MIN_VALUE, Integer.MAX_VALUE, depth).getLinija();
	    System.out.println();
        
        RacunarRezultat result = new RacunarRezultat();
        result.setBestMove(best);
        return result;
    }
    
    protected int heuristika(final Tabla tabla, int color) {
        int vrednost;
        int razlika = 20 * tabla.getCrveniRezultat() - tabla.getPlaviRezultat();
        int dodatnaHeuristika = 15 * tabla.brojKutijaSaIvicama(3) - tabla.brojKutijaSaIvicama(2);
        if(mojaBoja == Tabla.CRVENA)
            vrednost = razlika;
        else
            vrednost = -razlika;
        if((mojaBoja == color))
            vrednost +=  dodatnaHeuristika;
        else
            vrednost -=  dodatnaHeuristika;
        return vrednost;
    }
    
    
    LinijaIHeuristika alfaBeta(Tabla tabla, int color, int a, int b, int dubina) {
    	if (dubina<=0 || tabla.dohvatiMogucePoteze().size()==0)
    		return new LinijaIHeuristika(null, heuristika(tabla, color));
    	
        ArrayList<Linija> moves = tabla.dohvatiMogucePoteze();
        int size = moves.size();
        
    	Collections.shuffle(moves);
         
        LinijaIHeuristika[] deca = new LinijaIHeuristika[size] ;
        for(int i=0 ; i<size ; i++) {
            Tabla novaTabla = tabla.novaTablaSaLinijom(moves.get(i), color);
            deca[i] = new LinijaIHeuristika(moves.get(i),heuristika(novaTabla, (novaTabla.getRezultat(color) > tabla.getRezultat(color) ? color : Tabla.promeniBoju(color))));
            //neighbours[i] = new LinijaIHeuristika(moves.get(i),heuristika(newBoard, mojaBoja));
            //neighbours[i] = new LinijaIHeuristika(moves.get(i),newBoard.getScore(color));
        }
        
        igra.displayHeuristic(deca);
        
        Arrays.sort(deca);
        if(mojaBoja == color)
        	Collections.reverse(Arrays.asList(deca));
        
        if (color == mojaBoja) {
            LinijaIHeuristika noviLiH = new LinijaIHeuristika(null, Integer.MIN_VALUE);

            for (int i = 0; i < size; i++) {
                Tabla dete = tabla.novaTablaSaLinijom(deca[i].getLinija(), color);
                LinijaIHeuristika linijaIHeuristika;
                int childScore = dete.getRezultat(color), currentScore = tabla.getRezultat(color);
                boolean flag = false;
                if (childScore == currentScore) {
                    linijaIHeuristika = alfaBeta(dete, Tabla.promeniBoju(color), a, b, dubina-1);
                    flag = true;
                } else
                    linijaIHeuristika = alfaBeta(dete, color, a, b, dubina-1);

                int deteHeuristika = linijaIHeuristika.getHeuristika();
                if (noviLiH.getHeuristika() < deteHeuristika) {
                    noviLiH.setHeuristika(deteHeuristika);
                    noviLiH.setLinija(deca[i].getLinija());
                }
                if (flag)
                    if (deteHeuristika >= b)
                        return noviLiH;

                a = Math.max(a, noviLiH.getHeuristika());
            }
            return noviLiH;
        }
        else {
            LinijaIHeuristika newPair = new LinijaIHeuristika(null, Integer.MAX_VALUE);

            for (int i = 0; i < size; i++) {
                Tabla dete = tabla.novaTablaSaLinijom(deca[i].getLinija(), color);
                LinijaIHeuristika linijaIHeuristika;
                int deteRezultat = dete.getRezultat(color), trenutniRezultat = tabla.getRezultat(color);
                boolean flag = false;
                if (deteRezultat == trenutniRezultat) {
                    linijaIHeuristika = alfaBeta(dete, Tabla.promeniBoju(color), a, b, dubina-1);
                    flag = true;
                } else
                    linijaIHeuristika = alfaBeta(dete, color, a, b, dubina-1);

                int childUtility = linijaIHeuristika.getHeuristika();
                if (newPair.getHeuristika() > childUtility) {
                    newPair.setHeuristika(childUtility);
                    newPair.setLinija(deca[i].getLinija());
                }
                if (flag)
                    if (childUtility <= a)
                        return newPair;

                b = Math.min(b, newPair.getHeuristika());
            }
            return newPair;
        }

        
    	
    }

}
