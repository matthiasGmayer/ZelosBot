package mmm;

public class Scan {
    MyFirstBehavior robot;
    public Scan(MyFirstBehavior behavior){
        this.robot=behavior;
    }
    public void onScan(Enemy e, int tick){
        robot.turnRadar(Utils.normalRelativeAngle(-robot.getRadarHeading()+robot.getHeading()+e.getBearing()));
    }
    public void onNoScan(){
        robot.turnRadar(720);
    }

    public void start() {
    }
}
