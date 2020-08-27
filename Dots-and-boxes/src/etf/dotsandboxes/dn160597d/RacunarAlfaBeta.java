package etf.dotsandboxes.dn160597d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RacunarAlfaBeta extends Racunar{

    public RacunarRezultat getResult(Tabla tabla, int color) {
        mojaBoja = color ;
        
        ArrayList<Linija> moves = tabla.dohvatiMogucePoteze();
        int moveCount = moves.size();
        int value[] = new int[moveCount];
        
        for(int i=0;i<moveCount;i++) {
        	value[i] = tabla.pocetnikPotezHeuristika(moves.get(i));
        }
        System.out.println();

        int maxValueIndex=0;
        System.out.println(moves);
        
        boolean canFormBox = false;
        for(int i=0;i<moveCount;i++){
        	System.out.format("%3d,", value[i]);
        	if(value[i]>value[maxValueIndex]) {
        		canFormBox = true;
        		maxValueIndex=i;
        	}
        }
        Linija best = moves.get(maxValueIndex);
        System.out.println();
        
        if(!canFormBox) {
	        best = alfaBeta(tabla, color, Integer.MIN_VALUE, Integer.MAX_VALUE, depth).getLinija();
	        System.out.println();
        }
        
        
        RacunarRezultat result = new RacunarRezultat();
        result.setBestMove(best);
        return result;
    }
    
    protected int heuristika(final Tabla tabla, int color) {
        int value;
        if(mojaBoja == Tabla.CRVENA)
            value = tabla.getCrveniRezultat() - tabla.getPlaviRezultat();
        else
            value =  tabla.getPlaviRezultat() - tabla.getCrveniRezultat();
        return value;
    }
    
    
    LinijaIHeuristika alfaBeta(Tabla tabla, int color, int a, int b, int dubina) {
    	if (dubina<=0 || tabla.dohvatiMogucePoteze().size()==0)
    		return new LinijaIHeuristika(null, heuristika(tabla, color));
    	
    	
        ArrayList<Linija> moves = tabla.dohvatiMogucePoteze();
        int size = moves.size();
        
        Collections.shuffle(moves);
         
        LinijaIHeuristika[] neighbours = new LinijaIHeuristika[size] ;
        for(int i=0 ; i<size ; i++) {
            Tabla newBoard = tabla.novaTablaSaLinijom(moves.get(i), color);
            //neighbours[i] = new LinijaIHeuristika(moves.get(i),heuristika(newBoard, (newBoard.getScore(color) > tabla.getScore(color) ? color : Tabla.toggleColor(color))));
            neighbours[i] = new LinijaIHeuristika(moves.get(i),newBoard.getRezultat(color));
        }
        
        igra.displayHeuristic(neighbours);
        
        Arrays.sort(neighbours);
        if(mojaBoja == color)
        	Collections.reverse(Arrays.asList(neighbours));
        
        if (color == mojaBoja) {
            LinijaIHeuristika newPair = new LinijaIHeuristika(null, Integer.MIN_VALUE);

            for (int i = 0; i < size; i++) {
                Tabla dete = tabla.novaTablaSaLinijom(neighbours[i].getLinija(), color);
                LinijaIHeuristika linijaIHeuristika;
                int childScore = dete.getRezultat(color), currentScore = tabla.getRezultat(color);
                boolean flag = false;
                if (childScore == currentScore) {
                    linijaIHeuristika = alfaBeta(dete, Tabla.promeniBoju(color), a, b, dubina-1);
                    flag = true;
                } else
                    linijaIHeuristika = alfaBeta(dete, color, a, b, dubina-1);

                int childUtility = linijaIHeuristika.getHeuristika();
                if (newPair.getHeuristika() < childUtility) {
                    newPair.setHeuristika(childUtility);
                    newPair.setLinija(neighbours[i].getLinija());
                }
                if (flag)
                    if (childUtility >= b)
                        return newPair;

                a = Math.max(a, newPair.getHeuristika());
            }
            return newPair;
        }
        else {
            LinijaIHeuristika newPair = new LinijaIHeuristika(null, Integer.MAX_VALUE);

            for (int i = 0; i < size; i++) {
                Tabla dete = tabla.novaTablaSaLinijom(neighbours[i].getLinija(), color);
                LinijaIHeuristika linijaIHeuristika;
                int childScore = dete.getRezultat(color), currentScore = tabla.getRezultat(color);
                boolean flag = false;
                if (childScore == currentScore) {
                    linijaIHeuristika = alfaBeta(dete, Tabla.promeniBoju(color), a, b, dubina-1);
                    flag = true;
                } else
                    linijaIHeuristika = alfaBeta(dete, color, a, b, dubina-1);

                int childUtility = linijaIHeuristika.getHeuristika();
                if (newPair.getHeuristika() > childUtility) {
                    newPair.setHeuristika(childUtility);
                    newPair.setLinija(neighbours[i].getLinija());
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
