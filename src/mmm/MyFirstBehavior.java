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
	int scanned=0;
	@Override
	void execute() {
		tick++;
//		System.out.println(tick);
		if(tick==0) enemyBullets=new EnemyBullets(getGunCoolingRate(),getBattleFieldHeight(),getBattleFieldWidth());
		Enemy enemy=null;
		Scoring.tick(tick);
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
//			shooting.onScan(enemy,tick);
			move.onScan(enemy);
			for (var r: getHitByBulletEvents()){
				enemy.update(r);
				EnemyBullets.EnemyBullet enemyBullet=enemyBullets.shotByWhichBullet(lastPoint,r.getBullet().getPower(),tick,r.getHeading(),getPoint());
			}
			for (var r: getBulletHitEvents()){
				enemy.update(r);
			}
			for (var r: getHitRobotEvents()){
				if (scanned!=0) enemy.update(r);
			}
			for (var r: getBulletHitBulletEvents()){
				Point catchedPoint=new Point(r.getBullet().getX(),r.getBullet().getY());
//				System.out.println(catchedPoint);
			}
			if(scanned>1&&enemyBullets.enemyShotBullet(enemy,pastEnemy.getEnergy())){
				double bulletSize=enemyBullets.getEnemyShotBulletSize();
//				System.out.println(bulletSize);
				enemyBullets.addBullet(tick-1,20-3*bulletSize,bulletSize,pastEnemy.position,lastPoint,getHeading(),getVelocity());//hier noch vorletzes heading adden
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
