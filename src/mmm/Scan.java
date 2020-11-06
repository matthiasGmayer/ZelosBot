package mmm;

public class Scan {
    MyFirstBehavior robot;
    public Scan(MyFirstBehavior behavior){
        this.robot=behavior;
    }
    public void onScan(Enemy e, int tick){
        double angle=Utils.normalRelativeAngle(-robot.getRadarHeading()+robot.getHeading()+e.getBearing());
        angle += angle>0?20:-20;
        robot.turnRadar(angle);
    }
    public void onNoScan(){
        robot.turnRadar(720);
    }

    public void start() {
    }
}
