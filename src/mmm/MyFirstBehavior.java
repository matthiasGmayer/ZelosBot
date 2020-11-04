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

	int tick=-1;
	@Override
	void execute() {
		tick++;
		Enemy pastEnemy=null;
		EnemyBullets enemyBullets=null;
		if(tick==0) enemyBullets=new EnemyBullets(getGunCoolingRate(),getBattleFieldHeight(),getBattleFieldWidth());
		Enemy enemy=null; int scanned=0;
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
			shooting.onScan(enemy,tick);
			move.onScan(enemy);
			for (var r: getHitByBulletEvents()){
				enemy.update(r);
			}
			for (var r: getBulletHitEvents()){
				enemy.update(r);
			}
			for (var r: getHitRobotEvents()){
				if (scanned!=0) enemy.update(r);
			}
			for (var r: getBulletHitBulletEvents()){
				Point catchedPoint=new Point(r.getBullet().getX(),r.getBullet().getY());

//				enemyBullets.shotByWhichBullet(catchedPoint,r.getHitBullet().getPower(),tick,r.getHitBullet().getHeading(),r.getHitBullet().getVelocity());
			}
			if(scanned>1&&enemyBullets.enemyShotBullet(enemy,pastEnemy.getEnergy())){
				double bulletSize=enemyBullets.getEnemyShotBulletSize();
				enemyBullets.addBullet(tick,20-3*bulletSize,bulletSize,enemy.position,getPoint(),getHeading(),getVelocity());
			}pastEnemy=enemy;
		}
		for (var r: getHitByBulletEvents()){
			EnemyBullets.EnemyBullet enemyBullet=enemyBullets.shotByWhichBullet(getPoint(),r.getPower(),tick,r.getBearing(),r.getVelocity());
		}
		for (var r: getHitWallEvents()){
			move.onHitWall(r);
		}

	}
	public void turnGunTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getGunHeading()+angle));
	}
	public void turnScannerTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getRadarHeading()+angle));
	}
}
