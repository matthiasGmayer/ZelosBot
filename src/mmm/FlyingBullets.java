package mmm;
public class FlyingBullets {
    double battleFieldHeight;
    double battleFieldWidth;
    public Point[] corners;
    FlyingBullet[] flyingBullets;
    static int idcounter;
    public double gunCoolingRate;
    public final int id;
    public FlyingBullets(int id,double battlefieldheight,double battlefieldwidth,double gunCoolingRate){
        this.battleFieldHeight=battlefieldheight;
        this.battleFieldWidth=battlefieldwidth;
        this.id=id;
        this.gunCoolingRate=gunCoolingRate;
        setVariables();
    }
    public void setVariables() {
        corners = Calc.getRectangle(battleFieldWidth,battleFieldHeight);
        double maxBulletAmout=(Calc.maxBulletFlyTime(3,new Point(0,0),corners)/gunCoolingRate);
        flyingBullets=new FlyingBullet[(int)Math.ceil(maxBulletAmout)];
    }

    public void update(double angle,double power,Point position,int startTick){
        Scoring.cost(id,startTick,power);
        for (int i = 0; i < flyingBullets.length; i++) {
            if(flyingBullets[i]==null||flyingBullets[i].disabled){
                double velocity=20-3*power;
                double damage=4*power+Math.max(0,2*(power-1));
                flyingBullets[i]=new FlyingBullet(power,velocity,angle,position,startTick,damage);
                break;
            }
        }
    }
    public void update(Point enemyPosition,double enemyHeading,int tick){
        for (int i = 0; i < flyingBullets.length; i++) {
            if (flyingBullets[i]!=null&&!flyingBullets[i].disabled&&flyingBullets[i].score(enemyPosition,enemyHeading,tick)){
                Scoring.damage(id,flyingBullets[i].tick,flyingBullets[i].damage);
            }
        }
    }
    private class FlyingBullet{
        double velo,heading;
        double damage,cost;
        boolean disabled=false;
        Point startposition;
        int tick;
        public FlyingBullet(double cost, double velo,double heading,Point position, int startTick, double damage){
            this.velo=velo;
            this.heading=heading;
            this.startposition=position;
            this.tick= startTick;
            this.damage=damage;
            this.cost=cost;
        }
        public boolean score(Point enemyPosition,double enemyHeading,int tick){
            System.out.println("score");
            Point[] corners= Calc.getCorners(enemyHeading,enemyPosition);
            boolean scored=false;
            Point pastPosition=startposition.add(Point.fromPolarCoordinates(heading,velo*(tick-this.tick)));
            Point nowPosition=pastPosition.add(Point.fromPolarCoordinates(heading,velo));
            for (int i = 0; i < 4; i++) {
                scored|= Calc.isIntersecting(corners[i],corners[i%4],pastPosition,nowPosition);
            }
            boolean outOfField=false;
            outOfField=nowPosition.getX()<0;
            outOfField|=nowPosition.getY()<0;
            outOfField|=nowPosition.getY()>battleFieldHeight;
            outOfField|=nowPosition.getX()>battleFieldWidth;
            disabled=outOfField;
            scored = nowPosition.distance(enemyPosition)<40;
            if (scored)disabled=true;
            return scored;
        }
    }
}

