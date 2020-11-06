package mmm;

import java.util.LinkedList;
import java.util.List;

public class EnemyBullets {
    public double battleFieldWidth;
    public double battleFieldHeight;
    public static EnemyBullet[] enemyBullets;
    public Point[] corners=new Point[4];
    public double roboterdurchmesser=Math.sqrt(2*Math.pow(18,2));
    public double gunCoolingRate;
    public EnemyBullets(double gunCoolingRate,double battleFieldHeight,double battleFieldWidth){
        this.battleFieldHeight=battleFieldHeight;
        this.battleFieldWidth=battleFieldWidth;
        this.gunCoolingRate=gunCoolingRate;
        setVariables();
    }

    public void setVariables() {
        corners[0]=new Point(0,0);
        corners[1]=new Point(battleFieldWidth,0);
        corners[2]=new Point(0,battleFieldHeight);
        corners[3]=new Point(battleFieldWidth,battleFieldHeight);
        double maxBulletAmount=(Calc.maxBulletFlyTime(3,new Point(0,0),corners)*gunCoolingRate);
        enemyBullets=new EnemyBullet[(int)Math.ceil(maxBulletAmount)];
    }
    public void addBullet(int starttick,double bulletVelocity,double power,Point enemyPosition,Point ourPosition,double heading,double ourVelocity) {
        update(starttick);
        for (int i = 0; i < enemyBullets.length; i++) {
            if(enemyBullets[i]==null||enemyBullets[i].disabled){
                enemyBullets[i]=new EnemyBullet(starttick,bulletVelocity,power,enemyPosition,ourPosition,heading,ourVelocity);
//                System.out.println("addet "+i);
                break;
            }
        }
    }
    public boolean outOfZone(Point point){
        if(point.getX()<0||point.getX()>battleFieldWidth) return true;
        if(point.getY()<0||point.getY()>battleFieldHeight) return true;
        return false;
    }
    private void update(int tick){
        for (int i = 0; i < enemyBullets.length; i++) {
            if(enemyBullets[i]!=null) {
                if (Calc.maxBulletFlyTime(enemyBullets[i].bulletVelocity,enemyBullets[i].enemyPosition,corners)<(tick-enemyBullets[i].starttick)){
                    enemyBullets[i].disabled=true;
                }
            }
        }
    }
    public class EnemyBullet {
        boolean disabled = false;
        double bulletVelocity, ourVelocity, power, heading;
        Point enemyPosition, ourPositon;
        int starttick;

        public EnemyBullet(int starttick, double bulletVelocity, double power, Point enemyPosition, Point ourPosition, double heading, double ourVelocity) {
            this.bulletVelocity = bulletVelocity;
            this.enemyPosition = enemyPosition;
            this.heading = heading;
            this.ourVelocity = ourVelocity;
            this.power = power;
            this.starttick = starttick;
            this.ourPositon = ourPosition;
        }
    }
    double enemyShotBulletSize=0;
    public double getEnemyShotBulletSize(){
        return enemyShotBulletSize;         //nur aufrufen wenn enemyShotBullet gleich true
    }
    //TODO: WIRD NICHT RICHTIG AUFGERUFEN, GIBT IMMER FALSE ZURÜCK
    public boolean enemyShotBullet(Enemy enemy,double energyOfPastTick){
        double energy=energyOfPastTick;
        if (enemy.crashedRobo()) {energy-=0.6;}
      //  if (enemy.hitWall()) energy-=enemy.getVelocityWithWallHit()*0.5-2; //da man nicht sicher sein kann kann man verschiedene Varianten simulieren
        if (enemy.gotHitByBullet()){energy-=enemy.getHitbyBulletSize()*4+Math.max (0, 2 * (enemy.getHitbyBulletSize() - 1));}
        if (enemy.bulletHit()){
            energy+=3*enemy.getBulletHitSize();}
        if (energy!=enemy.getEnergy()){
            enemyShotBulletSize=Math.max(0.1,Math.min(energy-enemy.getEnergy(),3));
            return true;
        }
        return false;
    }

    public EnemyBullet shotByWhichBullet(Point preposition,double power,int tick,double degree,Point postposition){
        List<Integer> relevantIndex=new LinkedList<>();
        int c=0;
        for (int i = 0; i < enemyBullets.length; i++) {
            if(enemyBullets[i]!=null&&!enemyBullets[i].disabled){
                System.out.println("i"+i);
//                System.out.println("wir suchen");
                Point attackPosition=enemyBullets[i].enemyPosition;
                System.out.println(preposition.distance(attackPosition)+" angeblich dist"+(20-3*power)*(tick-enemyBullets[i].starttick));
                double predistance=(20-3*power)*(tick-1-enemyBullets[i].starttick);
//                predistance=0;
                double distance=(20-3*power)*(tick-enemyBullets[i].starttick);
                if(Calc.isShootable(preposition,attackPosition,distance,predistance)){ //wir erwarten geschwindigkeit der realen kugel nicht virtuel
                    relevantIndex.add(i);
//                    System.out.println("addet"+i);
                    c++;
                }
//                else if(Calc.isShootable(postposition,attackPosition,distance,predistance)){ //wir erwarten geschwindigkeit der realen kugel nicht virtuel
//                    relevantIndex.add(i);
//                    System.out.println("addet"+i);
//                    c++;
//                }
            }
        }
        System.out.println("relevantlistlenght"+c);
        return enemyBullets[relevantIndex.get(0)];
//        double powerdif=3;int bestIndex=-1;
//        for (int i:relevantIndex) {
//            Point attackPosition=enemyBullets[i].enemyPosition;
//            double distance=(20-3*power)*(tick-enemyBullets[i].starttick);
//            if(Calc.couldThisBulletHitUs(distance,degree,preposition,attackPosition)){
//                System.out.println("hi ich lebe noch");
//                if(Math.abs(power-enemyBullets[i].power)<powerdif){
//                    bestIndex=i;
//                    powerdif=Math.abs(power-enemyBullets[i].power);
//                }
//            }
//
//        }
//        enemyBullets[bestIndex].disabled=true;
//        return enemyBullets[bestIndex];

    }
}
