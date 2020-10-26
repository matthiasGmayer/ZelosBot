package mmm;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.jar.JarOutputStream;

public class Scoring {
    private static final int keepTicks=250;
    private static final int pointsToSwitch=2;
    static ArrayList<Double[]> damage = new ArrayList<>();
    static ArrayList<Double[]> cost = new ArrayList<>();
    static ArrayList<Double> scores = new ArrayList<>();
    static int best=0;
    static int startTick =0;
    static int tick;
    public static void tick(int tick){
        if(tick ==0)startTick+=Scoring.tick;
        Scoring.tick=tick;
        for (int i = 0; i< damage.size(); i++) {
            var d=damage.get(i)[tick%keepTicks];
            if(d>0){
                scores.set(i, scores.get(i)-d);
            }
            var c=cost.get(i)[tick%keepTicks];
            if(c>0){
                scores.set(i, scores.get(i)+c);
            }
            damage.get(i)[(tick+startTick)%keepTicks] = 0. ;
            cost.get(i)[(tick+startTick)%keepTicks] = 0. ;
        }
    }
    public static void damage(int tactic, int tick, double damage){
        checkSize(tactic);
        int i =(tick+startTick)%keepTicks;
        Scoring.damage.get(tactic)[i]=damage;
        scores.set(tactic, scores.get(tactic)+damage);
        System.out.println("HypoHit: "+tick);
    }
    public static void cost(int tactic, int tick, double cost){
        checkSize(tactic);
        int i =(tick+startTick)%keepTicks;
        Scoring.cost.get(tactic)[i]=cost;
        scores.set(tactic, scores.get(tactic)-cost);
    }public static void checkSize(int tactic){
        while(Scoring.damage.size()<=tactic) {
            Double[] a = new Double[keepTicks], b= new Double[keepTicks];
            for (int i = 0; i < keepTicks; i++) {
                a[i]=0.;
                b[i]=0.;
            }
            Scoring.damage.add(a);
            Scoring.cost.add(b);
            scores.add(0.);
        }
    }
    public static int getBestTactic(){
        for (int i = 1; i < scores.size(); i++) {
            if(scores.get(i)> scores.get(best)+pointsToSwitch){
                best=i;
            }
        }
//        for (int i = 0; i < scores.size()-1; i++) {
//            System.out.print(scores.get(i) + " | ");
//        }
//        System.out.println(scores.get(scores.size()-1));
        return best;
    }
}
