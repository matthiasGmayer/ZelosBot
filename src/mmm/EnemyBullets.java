package Markus;

public class EnemyBullets {
    public double battleFieldWidth;
    public double battleFieldHeight;
    public static EnemyBullet[] enemyBullets;
    public Point[] corners;
    public EnemyBullets(double battleFieldHeight,double battleFieldWidth){
       this.battleFieldHeight=battleFieldHeight;
       this.battleFieldWidth=battleFieldWidth;
       setVariables();
    }

    private void setVariables() {
        corners[0]=new Point(0,0);
        corners[1]=new Point(battleFieldWidth,0);
        corners[2]=new Point(0,battleFieldHeight);
        corners[3]=new Point(battleFieldWidth,battleFieldHeight);

    }

    public int maxBulletFlyTime(double bulletVelocity,Point enemyPosition){
        double maxDistance=0;
        for (int i = 0; i < 4; i++) {
            maxDistance=Math.max(maxDistance,corners[i].distance(enemyPosition));
        }
        return (int)Math.ceil(maxDistance/bulletVelocity);
    }
    public void addBullet(int starttick,double bulletVelocity,double power,Point enemyPosition,Point ourPosition,double heading,double ourVelocity) {
        for (int i = 0; i < enemyBullets.length; i++) {
            if()
        }
    }
    private void update(int tick){
        for (int i = 0; i < enemyBullets.length; i++) {
            enemyBullets[i]
        }
    }
    public class EnemyBullet {
        boolean disabled=false;
        double bulletVelocity,ourVelocity,power,heading;
        Point enemyPosition,ourPositon;
        int starttick;
        public EnemyBullet(int starttick,double bulletVelocity,double power,Point enemyPosition,Point ourPosition,double heading,double ourVelocity) {
            this.bulletVelocity=bulletVelocity;
            this.enemyPosition=enemyPosition;
            this.heading=heading;
            this.ourVelocity=ourVelocity;
            this.power=power;
            this.starttick=starttick;
            this.ourPositon=ourPosition;
        }
        public EnemyBullet shotByWhichBullet(Point position,double power,int tick){

        }
    }
}
