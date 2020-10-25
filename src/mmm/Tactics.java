package mmm;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Tactics {
    public static double coolingRate = 0.1;
    ArrayList<Supplier<Pair<Double, Double>>> tactics = new ArrayList<>();
    ArrayList<FlyingBullets> flyingBullets = new ArrayList<>();
    ArrayList<Integer> coolDown = new ArrayList<>();
    private int size;
    public void add(Supplier<Pair<Double,Double>> function){
        tactics.add(function);
        flyingBullets.add(new FlyingBullets());
        coolDown.add(0);
        size++;
    }
    int getCooldown(double firePower){
        return (int)Math.ceil((1+firePower/5)/coolingRate);
    }
    public void update(Point position, Point enemyPosition, double enemyHeading,int tick){
        for (int i = 0; i < size; i++) {
            if(coolDown.get(i)==0){
                var p = tactics.get(i).get();
                flyingBullets.get(i).update(p.a,p.b,position,tick);
                coolDown.set(i,getCooldown(p.b));
            }
        }
        for (int i = 0; i < size; i++) {
            flyingBullets.get(i).update(enemyPosition,enemyHeading,tick);
        }
    }
    public Pair<Integer, Pair<Double,Double>> getBest(){
        int i = Scoring.getBestTactic();
        int coolTick = coolDown.get(i);
        return new Pair<>(coolTick, tactics.get(i).get());
    }

}
