package mmm;

public class EnemyBullets {
    public double battleFieldWidth;
    public double battleFieldHeight;
    public static EnemyBullet[] enemyBullets;
    public Point[] corners;
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
        double maxBulletAmout=(Calc.maxBulletFlyTime(3,new Point(0,0))/gunCoolingRate);
        enemyBullets=new EnemyBullet[(int)Math.ceil(maxBulletAmout)];
    }
    public void addBullet(int starttick,double bulletVelocity,double power,Point enemyPosition,Point ourPosition,double heading,double ourVelocity) {
        for (int i = 0; i < enemyBullets.length; i++) {
            if(enemyBullets[i]==null||enemyBullets[i].disabled){
                enemyBullets[i]=new EnemyBullet(starttick,bulletVelocity,power,enemyPosition,ourPosition,heading,ourVelocity);
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
            Point currentBulletPosition=enemyBullets[i].enemyPosition.add(Point.fromPolarCoordinates(enemyBullets[i].heading,enemyBullets[i].bulletVelocity*(tick-enemyBullets[i].starttick)));
            if(outOfZone(currentBulletPosition))enemyBullets[i].disabled=true;
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
            this.starttick=starttick+1;
            this.ourPositon=ourPosition;
        }
        public EnemyBullet[] shotByWhichBullet(Point position,double power,int tick,double degree){
            int Relevant=0;
            int[] relevantIndex=new int[enemyBullets.length];
            for (int i = 0; i < enemyBullets.length; i++) {
                if(enemyBullets[0]==null||enemyBullets[0].disabled){
                    //if(Math.abs())
                }
            }
            return null;
        }
    }
}
