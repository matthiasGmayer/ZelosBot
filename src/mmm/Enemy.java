package mmm;

public class Enemy {
    private double bearing, heading, velocity, distance, hitbyBulletSize, bulletHitSize, velocityWithWallHit, energy;
    private boolean gotHitByBullet=false, bulletHit=false, crashedRobo=false, hitWall=false;
    Point position, relativePosition;
    public static double battleFieldWidth=0,battleFieldHeight=0;//initialisieren!

    public Enemy(ScannedRobotEvent e, Point position, double robotHeading){
        update(e,position,robotHeading);

    }
    private void update(ScannedRobotEvent e, Point position, double robotHeading){
        energy=e.getEnergy();
        bearing=e.getBearing();
        heading = e.getHeading();
        velocity = e.getVelocity();
        distance= e.getDistance();
        relativePosition = Point.fromPolarCoordinates(robotHeading+bearing,distance);
        this.position = position.add(relativePosition);
    }
    public void update(HitByBulletEvent hitByBulletEvent){
        bulletHit=true;
        bulletHitSize=hitByBulletEvent.getPower();
    }
    public void update(BulletHitEvent bulletHitEvent){
        gotHitByBullet =true;
        hitbyBulletSize=bulletHitEvent.getBullet().getPower();
    }
    public void update(HitRobotEvent hitRobotEvent){ //nur ausführen nachdem ersten Scan, sonst wird noch kein Objekt der Klasse erstellt
        crashedRobo=true;
    }
    public void update(boolean hitWall,double velocity){
        this.hitWall=hitWall;
        velocityWithWallHit=velocity;
    }
    public boolean gotHitByBullet(){return  gotHitByBullet;}

    public boolean bulletHit(){return bulletHit;}

    public boolean crashedRobo(){ return crashedRobo;}

    public boolean hitWall(){ return hitWall;}

    public double getHitbyBulletSize(){ return hitbyBulletSize;}

    public double getBulletHitSize(){ return bulletHitSize;}

    public double getVelocityWithWallHit(){ return velocityWithWallHit;}

    public double getBearing() {
        return bearing;
    }

    public double getDistance() {
        return distance;
    }

    public double getHeading() {
        return heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getEnergy(){return energy;}

    public Point getPosition() {
        return position;
    }

    public Point getRelativePosition() {
        return relativePosition;
    }

    public double getFutureWallHitVelocity(){
        return velocityWithWallHit;
    }
    public boolean willHeHitWallInNextTick(){
        double closestWallDist=Math.min(battleFieldWidth-position.getX(),position.getX());
        closestWallDist=Math.min(closestWallDist,position.getY());
        closestWallDist=Math.min(closestWallDist,battleFieldHeight-position.getY());
        if(closestWallDist<=6&&velocity>2){
            velocityWithWallHit=velocity-2;
            return true;
        }
        return false;
    }

}

