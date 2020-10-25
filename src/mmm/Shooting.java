package mmm;

public class Shooting {
    MyFirstBehavior robot;
    Tactics tactics = new Tactics();
    public Shooting(MyFirstBehavior behavior){
        this.robot=behavior;
        tactics.add(this::shootToEnemy);
    }

    public void onScan(Enemy enemy, int tick) {
        tactics.update(robot.position,enemy.position,enemy.getHeading(),tick);
        var best = tactics.getBest();
        int cooldown = best.a;
        double angle = best.b.a;
        double firePower = best.b.b;
        robot.turnGunTo(angle);
        if(cooldown==0){
            robot.fireBullet(firePower);
        }
    }
    public void start(){
    }
    public Pair<Double,Double> shootToEnemy(){
        return new Pair<Double, Double>(robot.enemy.relativePosition.angle(),3.);
    }
}
