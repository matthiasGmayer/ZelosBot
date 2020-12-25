package mmm;

import mmm.Util.Calc;
import mmm.Util.Point;
import mmm.Util.Utils;

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
//                Debug.println("addet "+i);
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
//        if (enemy.hitWall()) energy-=enemy.getVelocityWithWallHit()*0.5-2; //da man nicht sicher sein kann kann man verschiedene Varianten simulieren
        if (enemy.gotHitByBullet()){energy-=enemy.getHitbyBulletSize()*4+Math.max (0, 2 * (enemy.getHitbyBulletSize() - 1));}
        if (enemy.bulletHit()){
            energy+=3*enemy.getBulletHitSize();}
        if (energy!=enemy.getEnergy()){
            enemyShotBulletSize=Math.max(0.1,Math.min(energy-enemy.getEnergy(),3));
            return true;
        }
        //  if (enemy.hitWall()) energy-=enemy.getVelocityWithWallHit()*0.5-2; //da man nicht sicher sein kann kann man verschiedene Varianten simulieren
        return false;
    }

    public EnemyBullet shotByWhichBullet(Point preposition,double power,int tick,double degree,Point postposition,Point bulletPoint){
        List<Integer> relevantIndex=new LinkedList<>();
        int c=0;
        System.out.println("degree"+ Utils.normalRelativeAngle(degree+180));
        for (int i = 0; i < 50; i++) {
            System.out.println(bulletPoint.add(Point.fromPolarCoordinates(Utils.normalRelativeAngle(degree+180),i*(20-3*power))));
        }
        for (int i = 0; i < enemyBullets.length; i++) {
            if(enemyBullets[i]!=null&&!enemyBullets[i].disabled){
                Point hypoPosition=enemyBullets[i].enemyPosition.add(Point.fromPolarCoordinates(Utils.normalRelativeAngle(degree),(20-3*power)*(tick-enemyBullets[i].starttick)));
                System.out.println(i +" ab end" + hypoPosition.distance(bulletPoint) );
                System.out.println("hypopos "+hypoPosition);
                System.out.println("tickss "+(tick-enemyBullets[i].starttick));
                System.out.println(enemyBullets[i].enemyPosition);
                if (hypoPosition.distance(bulletPoint)<0.01){
//                    return enemyBullets[i];
                    System.out.println("jjjjj0,4)");
                    relevantIndex.add(i);
                }
//                Debug.println("wir suchen");
            //    double predistance=(20-3*power)*(tick-1-enemyBullets[i].starttick);
//                predistance=0;
                double distance=(20-3*power)*(tick-enemyBullets[i].starttick);
      //          if(Calc.isShootable(preposition,attackPosition,distance,predistance)){ //wir erwarten geschwindigkeit der realen kugel nicht virtuel
 //                   relevantIndex.add(i);
//                    Debug.println("addet"+i);
                    c++;

//                else if(Calc.isShootable(postposition,attackPosition,distance,predistance)){ //wir erwarten geschwindigkeit der realen kugel nicht virtuel
//                    relevantIndex.add(i);
//                    Debug.println("addet"+i);
//                    c++;
//                }
            }
        }
//        int closestindex=0;
//        double sizediff=Math.abs(enemyBullets[relevantIndex.get(0)].power-power);
//        for (int i = 1; i < relevantIndex.size(); i++) {
//            if (Math.abs(enemyBullets[relevantIndex.get(i)].power-power)<sizediff){
//                closestindex=i;
//                sizediff=Math.abs(enemyBullets[relevantIndex.get(i)].power-power);
//            }
//        }
        enemyBullets[relevantIndex.get(0)].power=power;
        return enemyBullets[relevantIndex.get(0)];
//        Debug.println("relevantlistlenght"+c);
////        return enemyBullets[relevantIndex.get(0)];
//        int counk= 0;
//        double powerdif=3;
//        int bestIndex=-1;
//        for (int i:relevantIndex) {
//            Point attackPosition=enemyBullets[i].enemyPosition;
//            double distance=(20-3*power)*(tick-enemyBullets[i].starttick);
//            if(Calc.couldThisBulletHitUs(distance,degree,preposition,attackPosition)){
//                if(powerdif==Math.abs(power-enemyBullets[i].power))
//                    Debug.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS",10);
//                if(Math.abs(power-enemyBullets[i].power)<powerdif){
//                    counk++;
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
