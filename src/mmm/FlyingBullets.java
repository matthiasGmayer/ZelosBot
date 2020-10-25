package Markus;
public class FlyingBullets {
    static double battlefieldheight;
    static double battlefieldwidth;
    static FlyingBullet[] flyingBullets;
    static int idcounter;
    public final int id;
    public FlyingBullets(){
        this.id=idcounter+1;
        idcounter++;
    }
    public void update(double angle,double power,Point position,int startTick){
        Scoring.cost(id,power);
        for (int i = 0; i < flyingBullets.length; i++) {
            if(flyingBullets[i]==null||flyingBullets[i].disabled){
                double velocity=20-3*power;
                double damage=4*power+Math.max(0,2*(power-1));
                flyingBullets[i]=new FlyingBullet(power,velocity,angle,position,startTick,damage);
                break;
            }
        }
    }
    public void update(Point enemyPosition,double enemyheading,int tick){
        for (int i = 0; i < flyingBullets.length; i++) {
           if (!flyingBullets[i].disabled&&flyingBullets[i].score(enemyPosition,enemyheading,tick)){
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
        public boolean score(Point enemyposition,double enemyheading,int tick){
            Point[] corners=Calc.getCorners(enemyheading,enemyposition);
            boolean scored=false;
            Point pastPosition=startposition.add(Point.fromPolarCoordinates(heading,velo*(tick-this.tick)));
            Point nowPosition=pastPosition.add(Point.fromPolarCoordinates(heading,velo));
            for (int i = 0; i < 4; i++) {
                scored|=Calc.isIntersecting(corners[i],corners[i%4],pastPosition,nowPosition);
            }
            boolean outOfField=false;
            outOfField=nowPosition.getX()<0;
            outOfField|=nowPosition.getY()<0;
            outOfField|=nowPosition.getY()>battlefieldheight;
            outOfField|=nowPosition.getX()>battlefieldwidth;
            disabled=outOfField;
            if (scored)disabled=true;
            return scored;
        }
    }
}

