package mmm;

public class Move {
    MyFirstBehavior robot;
    public Move(MyFirstBehavior behavior){
        this.robot=behavior;
    }
    double orbitDistance =300;
    double reverse = 1;
    void onScan(Enemy enemy){
        // This is a attack on this code:
        robot.turn(200);
        double dif = orbitDistance - enemy.getDistance();
            double angle = Math.min(25, Math.max(dif * 2,-25));
            robot.turn(90 + reverse * angle + enemy.getBearing());
            if (Math.random() < 0.1) reverse *= -1;
            robot.ahead(reverse * Math.random() * 40);
    }
    public void onHitWall(HitWallEvent e) {
        reverse *= -1;
    }
}
