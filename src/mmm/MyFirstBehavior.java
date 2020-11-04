package mmm;

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
		System.out.println(tick+" " +getEnergy());
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
//			move.onScan(enemy);
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
				System.out.println(catchedPoint);
			}
			if(scanned>1&&enemyBullets.enemyShotBullet(enemy,pastEnemy.getEnergy())){
				double bulletSize=enemyBullets.getEnemyShotBulletSize();
				enemyBullets.addBullet(tick,20-3*bulletSize,bulletSize,enemy.position,getPoint(),getHeading(),getVelocity());
			}pastEnemy=enemy;
		}
		for (var r: getHitByBulletEvents()){
			EnemyBullets.EnemyBullet enemyBullet=enemyBullets.shotByWhichBullet(getPoint(),r.getPower(),tick,r.getBearing());
			System.out.println(enemyBullet.starttick);
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
