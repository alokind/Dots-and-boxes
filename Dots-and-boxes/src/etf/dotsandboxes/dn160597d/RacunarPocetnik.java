package etf.dotsandboxes.dn160597d;
import java.util.ArrayList;
import java.util.Random;

public class RacunarPocetnik extends Racunar {

	@Override
    public RacunarRezultat getResult(final Tabla tabla, int color) {
        mojaBoja = color;
        ArrayList<Linija> moves = tabla.dohvatiMogucePoteze();
        int moveCount = moves.size();
        int value[] = new int[moveCount];
        
        for(int i=0;i<moveCount;i++) {
        	value[i] = tabla.pocetnikPotezHeuristika(moves.get(i));
        }
        igra.displayHeuristic(moves, value);
        System.out.println();

        int maxValueIndex=0;
        System.out.println(moves);
        
        boolean canFormBox = false;
        for(int i=0;i<moveCount;i++){
        	System.out.format("%3d,", value[i]);
        	if(value[i]>value[maxValueIndex]) {
        		maxValueIndex=i;
        	}
        	if(value[i]>0) {
        		canFormBox= true;
        	}
        }
        System.out.println();
        
        //Ne moze da formira kvadrat
        if (!canFormBox) {
        	Random r = new Random();
        	maxValueIndex = r.nextInt(moveCount);
        }
        RacunarRezultat result = new RacunarRezultat(moves, value, moves.get(maxValueIndex));
        return result;
    }

}
