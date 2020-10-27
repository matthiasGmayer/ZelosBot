package mmm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Shooting {
    MyFirstBehavior robot;
    Tactics tactics;
    public Shooting(MyFirstBehavior behavior){
        this.robot=behavior;
    }
    Enemy enemy;
    List<Enemy> pastList = new LinkedList<>();
    int keepPast = 10;
    public void onScan(Enemy enemy, int tick) {
        this.enemy =enemy;
        pastList.add(0,enemy);
        if(pastList.size()>keepPast)pastList.remove(pastList.size()-1);
        tactics.update(robot.position,enemy.position,enemy.getHeading(),tick);
        var best = tactics.getBest();
        double gunHeat = best.a;
        double angle = best.b.a;
        double firePower = best.b.b;
        robot.turnGunTo(angle);
        if(gunHeat<=0.1){
            robot.fireBullet(firePower);
            System.out.println("shotBullet");
            System.out.println("Actual Heading: " + Utils.normalRelativeAngle(robot.getGunHeading()));
        }
        for (var r : robot.getBulletHitEvents()){
            System.out.println("ActualHit: "+tick);
        }
    }
    public void start(){
        tactics = new Tactics(robot.getBattleFieldWidth(),robot.getBattleFieldHeight(),robot.getGunCoolingRate());
//        for (int i = 0; i < 16; i++) {
//        tactics.add(this::shootToEnemy,(double)i);
//        }
        tactics.add(() ->shootToEnemy(),0.);
//        tactics.add(()->shootAtAverage(10),0.);
//        tactics.add(()->shootPredicted(0),0.);

    }
    public Pair<Double,Double> shootToEnemy(){
        return new Pair<Double, Double>(enemy.relativePosition.angle(),3.);
    }
    public Pair<Double,Double> shootAtAverage(int usePast){
        Point p = new Point(0,0);
        int size = Math.min(pastList.size(),usePast);
        for (int i = 0; i < size; i++) {
            var e = pastList.get(i);
            p=p.add(e.position);
        }
        Point f = p.multiply(1.0/size);
        return new Pair<>(f.angleFrom(robot.position),3.);
    }
    int maxDis=400;
    int minDis=100;
    public Pair<Double,Double> shootPredicted(int forcePower){
        List<Point> futurePredictions = getFuturesLin(5,false,false,pastList);
        double targetPower = enemy.getDistance()-minDis;
        targetPower/=maxDis+minDis;
        targetPower= Math.max(0,Math.min(1,targetPower));
        targetPower=1-targetPower;
        targetPower *= 2.9;
        targetPower+=0.1;
        if(forcePower>0)targetPower=forcePower;
        double power = 0.1;
        List<Double> futureFirePower = new LinkedList<>();
        for (int i = 0; i < futurePredictions.size(); i++) {
            double d = Math.max(futurePredictions.get(i).distance(robot.position),1);
            futureFirePower.add((20-d/(i+1))/3);
        }
        int i;
        for (i = 0; i < futurePredictions.size(); i++) {
            power =futureFirePower.get(i);
            if(power >= 0.1 && Math.abs(targetPower-power)<0.3) break;
        }
        if(i==futurePredictions.size()){
            i=0;
            System.out.println("NOT PREDICTED");
        }
        if(forcePower>0)power=forcePower;

        var futurePosition=futurePredictions.get(i);
        double angle = futurePosition.angleFrom(robot.position);
        return new Pair<>(angle,power);
    }
    final int ticksIntoFuture=40;
    final int maxVel = 8;
    public List<Point> getFuturesLin(int usePast, boolean noAcc, boolean cap, List<Enemy> pastList){
        List<Point> futurePredictions = new LinkedList<>();
        double[] velDif = new double[usePast];
        double[] turnDif = new double[usePast];
        int lastIndex = Math.min(pastList.size()-1,usePast-1);
        for (int i = lastIndex; i >1 ; i--) {
            Enemy a = pastList.get(i), b= pastList.get(i-1);
            velDif[lastIndex-i]= b.getVelocity()-a.getVelocity();
            turnDif[lastIndex-i]= Utils.normalRelativeAngle(b.getHeading()-a.getHeading());
        }
        double[] futureTurn = new double[ticksIntoFuture];
        double turn = pastList.get(0).getHeading();
        double avgT = Arrays.stream(turnDif).average().getAsDouble();
        double[] futureVels = new double[ticksIntoFuture];
        double vel = pastList.get(0).getVelocity();
        boolean pos = vel >0;
        double avg = Arrays.stream(velDif).average().getAsDouble();
        for (int i = 0; i < futureVels.length; i++) {
            if(noAcc) vel = avg;
            else vel += avg;
            if(cap) if(pos){
                vel = Math.max(0,vel);
            }else{
                vel = Math.max(0,-vel);
            }
            vel = Utils.max(Utils.min(vel, maxVel),-maxVel);
            futureVels[i]=vel;
        }
        for (int i = 0; i < futureTurn.length; i++) {
            double toTurn = avgT;
            double maxTurn = (10 - 0.75 * Utils.abs(futureVels[i]));
            toTurn = Utils.max(Utils.min(toTurn, maxTurn),-maxTurn);
            if(i==0){
                futureTurn[i]=turn+toTurn;
            }else
                futureTurn[i]=futureTurn[i-1]+toTurn;
        }
        futurePredictions.add(enemy.position);
        for (int i = 0; i < ticksIntoFuture-1; i++) {
            Point point = futurePredictions.get(futurePredictions.size()-1);
            point = point.add(Point.fromPolarCoordinates(futureTurn[i],futureVels[i]));
            futurePredictions.add(point);
        }
        futurePredictions.remove(0);
        return futurePredictions;
    }
}
