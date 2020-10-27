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
		Enemy pastEnemy=null;
		EnemyBullets enemyBullets=null;
		if(tick==0) enemyBullets=new EnemyBullets(getGunCoolingRate(),getBattleFieldHeight(),getBattleFieldWidth());
		Enemy enemy=new Enemy(null,null,0); boolean scanned=false;
		position = new Point(getX(),getY());
		ScannedRobotEvent e = null;
		for (var r : getScannedRobotEvents()) {
			scanned=true;
			e=r;
		}
		if(e==null){
			scan.onNoScan();
		}else{
			enemy = new Enemy(e,position,getHeading());
			scan.onScan(enemy,tick);
			shooting.onScan(enemy,tick);
		}
		for (var r: getHitByBulletEvents()){
			enemy.update(r);
		}
		for (var r: getBulletHitEvents()){
			enemy.update(r);
		}
		for (var r: getHitRobotEvents()){
			if (scanned) enemy.update(r);
		}
		for (var r: getBulletHitBulletEvents()){
			Point catchedPoint=new Point(r.getBullet().getX(),r.getBullet().getY());

			enemyBullets.shotByWhichBullet(catchedPoint,r.getHitBullet().getPower(),tick,r.getHitBullet().getHeading(),r.getHitBullet().getVelocity());
		}
		if(scanned&&enemyBullets.enemyShotBullet(enemy,pastEnemy.getEnergy())){
			double bulletSize=enemyBullets.getEnemyShotBulletSize();
			enemyBullets.addBullet(tick,20-3*bulletSize,bulletSize,enemy.position,getPoint(),getHeading(),getVelocity());
		}
		for (var r: getHitByBulletEvents()){
			EnemyBullets.EnemyBullet enemyBullet=enemyBullets.shotByWhichBullet(getPoint(),r.getPower(),tick,r.getBearing(),r.getVelocity());
		}
		pastEnemy=enemy;
	}
	public void turnGunTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getGunHeading()+angle));
	}
	public void turnScannerTo(double angle){
		turnGun(Utils.normalRelativeAngle(-getRadarHeading()+angle));
	}
}
