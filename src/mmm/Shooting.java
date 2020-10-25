package mmm;

public class Shooting {
    MyFirstBehavior robot;
    Tactics tactics;
    public Shooting(MyFirstBehavior behavior){
        this.robot=behavior;
    }

    public void onScan(Enemy enemy, int tick) {
        tactics.update(robot.position,enemy.position,enemy.getHeading(),tick);
        var best = tactics.getBest();
        double gunHeat = best.a;
        double angle = best.b.a;
        double firePower = best.b.b;
        robot.turnGunTo(angle);
        if(gunHeat==0){
            robot.fireBullet(firePower);
        }
    }
    public void start(){
        tactics = new Tactics(robot.getBattleFieldWidth(),robot.getBattleFieldHeight(),robot.getGunCoolingRate());
        tactics.add(this::shootLeft,0.);
        tactics.add(this::shootToEnemy,0.);
    }
    public Pair<Double,Double> shootToEnemy(){
        return new Pair<Double, Double>(robot.enemy.relativePosition.angle(),3.);
    }
    public Pair<Double, Double> shootLeft(){
        return new Pair<>(0.,3.);
    }
}
