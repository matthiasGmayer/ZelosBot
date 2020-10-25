package mmm;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Tactics {
    public static double coolingRate = 0.1;
    ArrayList<Supplier<Pair<Double, Double>>> tactics = new ArrayList<>();
    ArrayList<FlyingBullets> flyingBullets = new ArrayList<>();
    ArrayList<Double> gunHeat = new ArrayList<>();
    private int size;
    private double battleFieldWidth,battleFieldHeight,gunCoolDown;


    public Tactics(double battleFieldWidth, double battleFieldHeight, double gunCoolDown){
        this.battleFieldHeight = battleFieldHeight;
        this.battleFieldWidth = battleFieldWidth;
        this.gunCoolDown = gunCoolDown;
    }
    public void add(Supplier<Pair<Double,Double>> function,Double gunHeat){
        tactics.add(function);
        flyingBullets.add(new FlyingBullets(size,battleFieldHeight,battleFieldWidth,gunCoolDown));
        this.gunHeat.add(gunHeat);
        size++;
    }
    double getGunHeat(double firePower){
        return 1+firePower/5;
    }
    public void update(Point position, Point enemyPosition, double enemyHeading,int tick){
        for (int i = 0; i < size; i++) {
            if(gunHeat.get(i)==0){
                var p = tactics.get(i).get();
                flyingBullets.get(i).update(p.a,p.b,position,tick);
                gunHeat.set(i, getGunHeat(p.b));
            }
            gunHeat.set(i,Math.max(0, gunHeat.get(i)-gunCoolDown));
        }
        for (int i = 0; i < size; i++) {
            flyingBullets.get(i).update(enemyPosition,enemyHeading,tick);
        }
    }
    public Pair<Double, Pair<Double,Double>> getBest(){
        int i = Scoring.getBestTactic();
        double coolTick = gunHeat.get(i);
        return new Pair<>(coolTick, tactics.get(i).get());
    }
}
