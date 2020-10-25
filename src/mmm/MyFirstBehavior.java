package mmm;

import java.util.*;

public class MyFirstBehavior extends SimpleRobotBehavior {
	Scan scan;
	Shooting shooting;
	Point position;
	Enemy enemy = new Enemy();
	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}
	@Override
	public void start() {
		position = new Point(getX(),getY());
		scan = new Scan(this);
		shooting = new Shooting(this);
		scan.start();
		shooting.start();
	}
	int tick;
	@Override
	void execute() {
		tick++;
		position = new Point(getX(),getY());
		ScannedRobotEvent e = null;
		for (var r : getScannedRobotEvents()) {
			e=r;
		}
		if(e==null){
			scan.onNoScan();
		}else{
			enemy.update(e,position);
			scan.onScan(enemy,tick);
			shooting.onScan(enemy,tick);
		}
	}
	public void turnGunTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getGunHeading()+angle));
	}
	public void turnScannerTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getRadarHeading()+angle));
	}
}
