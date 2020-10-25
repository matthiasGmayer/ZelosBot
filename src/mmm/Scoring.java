package mmm;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.jar.JarOutputStream;

public class Scoring {
    private static final int keepTicks=100;
    private static final int pointsToSwitch=2;
    static ArrayList<Double[]> damage = new ArrayList<>();
    static ArrayList<Double[]> cost = new ArrayList<>();
    static ArrayList<Double> scores = new ArrayList<>();
    static int best=0;
    public void tick(int tick){
        for (int i = 0; i< damage.size(); i++) {
            var d=damage.get(i)[tick%keepTicks];
            if(d>0){
                scores.set(i, scores.get(i)-d);
            }
            var c=cost.get(i)[tick%keepTicks];
            if(c>0){
                scores.set(i, scores.get(i)+c);
            }
            damage.get(i)[tick%keepTicks] = 0. ;
            cost.get(i)[tick%keepTicks] = 0. ;
        }
    }
    public static void damage(int tactic, int tick, double damage){
        while(Scoring.damage.size()<=tactic) {
            Scoring.damage.add(new Double[keepTicks]);
            Scoring.cost.add(new Double[keepTicks]);
            scores.add(0.);
        }
        int i =tick%keepTicks;
        Scoring.damage.get(tactic)[i]=damage;
        scores.set(tactic, scores.get(tactic)+damage);
        System.out.println("HIT "+tactic);
    }
    public static void cost(int tactic, int tick, double cost){
        while(Scoring.cost.size()<=tactic) {
            Scoring.cost.add(new Double[keepTicks]);
            Scoring.damage.add(new Double[keepTicks]);
            scores.add(0.);
        }
        int i =tick%keepTicks;
        Scoring.cost.get(tactic)[i]=cost;
        scores.set(tactic, scores.get(tactic)-cost);
    }
    public static int getBestTactic(){
        for (int i = 1; i < scores.size(); i++) {
            if(scores.get(i)> scores.get(best)+pointsToSwitch){
                best=i;
            }
        }
        return best;
    }
}
