package mmm;

import java.util.*;

public class MyFirstBehavior extends SimpleRobotBehavior {
	Scan scan;
	Shooting shooting;
	Point position;
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

	int tick=-1;
	@Override
	void execute() {
		tick++;
		Scoring.tick(tick);
		position = new Point(getX(),getY());
		ScannedRobotEvent e = null;
		for (var r : getScannedRobotEvents()) {
			e=r;
		}
		if(e==null){
			scan.onNoScan();
		}else{

			var enemy = new Enemy(e,position,getHeading());
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
