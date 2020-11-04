package mmm;

import java.awt.*;
import java.util.*;

public class MyFirstBehavior extends SimpleRobotBehavior {
	Scan scan;
	Shooting shooting;
	Move move;
	Point position;
	public MyFirstBehavior(SimpleRobot  robot) {
		super(robot);
	}
	@Override
	public void start() {
		setColors(Color.magenta,Color.CYAN,Color.red,Color.yellow,Color.BLACK);
		position = new Point(getX(),getY());
		scan = new Scan(this);
		shooting = new Shooting(this);
		move = new Move(this);
		scan.start();
		shooting.start();
	}
	Enemy pastEnemy=null;
	Point lastPoint=null;
	EnemyBullets enemyBullets=null;
	int tick=-1;
	@Override
	void execute() {
		tick++;
		if(tick==0) enemyBullets=new EnemyBullets(getGunCoolingRate(),getBattleFieldHeight(),getBattleFieldWidth());
		Enemy enemy=null; int scanned=0;
		Scoring.tick(tick);
		System.out.println(tick+"tick");
		position = new Point(getX(),getY());
		ScannedRobotEvent e = null;
		for (var r : getScannedRobotEvents()) {
			scanned++;
			e=r;
		}
		if(e==null){
			scan.onNoScan();
		}else{
			enemy = new Enemy(e,position,getHeading());
			scan.onScan(enemy,tick);
			shooting.onScan(enemy,tick);
			move.onScan(enemy);
			for (var r: getHitByBulletEvents()){
				enemy.update(r);
				System.out.println("hehel");
				EnemyBullets.EnemyBullet enemyBullet=enemyBullets.shotByWhichBullet(lastPoint,r.getPower(),tick,r.getHeading());
				System.out.println("kugel auf tick "+enemyBullet.starttick);
			}
			for (var r: getBulletHitEvents()){
				enemy.update(r);
			}
			for (var r: getHitRobotEvents()){
				if (scanned!=0) enemy.update(r);
			}
			for (var r: getBulletHitBulletEvents()){
				Point catchedPoint=new Point(r.getBullet().getX(),r.getBullet().getY());
				System.out.println(catchedPoint);
			}
			if(scanned>1&&enemyBullets.enemyShotBullet(enemy,pastEnemy.getEnergy())){
				System.out.println("ADDDDD");
				double bulletSize=enemyBullets.getEnemyShotBulletSize();
				enemyBullets.addBullet(tick,20-3*bulletSize,bulletSize,enemy.position,lastPoint,getHeading(),getVelocity());//hier noch letzes heading adden
			}pastEnemy=enemy;
		}
		for (var r: getHitWallEvents()){
			move.onHitWall(r);
		}
		lastPoint=getPoint();
	}
	public void turnGunTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getGunHeading()+angle));
	}
	public void turnScannerTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getRadarHeading()+angle));
	}
}
