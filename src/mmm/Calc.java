package mmm;

public class Calc {
    public static boolean isIntersecting(Point p0, Point p1, Point p2, Point p3){
       return isIntersecting(p0.getX(),p0.getY(),p1.getX(),p1.getY(),p2.getX(),p2.getY(),p3.getX(),p3.getY());
    }

    public static void main(String[] args) {
        Point[] p = getCorners(45,new Point(10,10));
        for (int i = 0; i < p.length; i++) {
        }
    }
    public static int maxBulletFlyTime(double bulletVelocity, Point enemyPosition,Point[] battleFieldCorners){
        double maxDistance=0;
        for (int i = 0; i < 4; i++) {
            maxDistance=Math.max(maxDistance,battleFieldCorners[i].distance(enemyPosition));
        }
        return (int)Math.ceil(maxDistance/bulletVelocity);
    }

    public static boolean isIntersecting(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
        if(y1==y0){
            if(x1==x0) return false;
            else return isIntersecting(y0,x0,y1,x1,y2,x2,y3,x3);
        }else{
            double a1 = x0-x2 +(y2-y0)/(y1-y0)*(x1-x0);
            double a2 = x3-x2-(y3-y2)/(y1-y0)*(x1-x0);
            if(a2==0){
                if(a1!=0) return false;
                else {
                    double l1= (y2-y0)/(y1-y0);
                    double l2= (y2-y0+y3-y2);
//                    System.out.println(l1);
//                    System.out.println(l2);
                    if((y3-y2)/(y1-y0)<0){
                        return l2<=1&&l1>=0;
                    }else{
                        return l1<=1&&l2>=0;
                    }
                }
            }else{
                double m =a1/a2;
                double l = (y2-y0+m*(y3-y2))/(y1-y0);
//                System.out.println(new Point(x0,y0).add(new Point(x1-x0,y1-y0).multiply(l)).distance(new Point(x2,y2).add(new Point(x3-x2,y3-y2).multiply(m)))<Double.MAX_VALUE*100.);
                return 0<=l&&l<=1&&0<=m&&m<=1;
            }
        }
    }
    private static int width = 18;
    private static int height = 18;
    //36x36
    private static Point[] corners = new Point[]{
            new Point(-width,-height),
            new Point(+width,-height),
            new Point(+width,+height),
            new Point(-width,+height)
    };
    static double[] angle;
    static double[] distance;
    static {
        angle = new double[corners.length];
        distance = new double[corners.length];
        for (int i = 0; i < corners.length; i++) {
            angle[i] = corners[i].angle();
            distance[i] = corners[i].length();
        }
    }
    public static Point[] getCorners(double heading, Point position){
        Point[] p = new Point[4];
        for (int i = 0; i < corners.length; i++) {
            p[i] = position.add(Point.fromPolarCoordinates(heading + angle[i], distance[i]));
        }
        return p;
    }
    public static boolean isShootable(Point position,Point enemyPoint,double distance,double predistance){
        enemyPoint=enemyPoint.subtract(position);
        int preShootableCorners=0;
        for (int i = 0; i < corners.length; i++) {
            if(cornerIsShootable(corners[i],enemyPoint,predistance)) preShootableCorners++;
        }
        System.out.println("PreEdges "+preShootableCorners);

        if (preShootableCorners>2){
            return false;

        }
//        if(preShootableCorners==2){
//
//            for (int i = 0; i < corners.length; i++) {
//                if (edgeIsShootable(enemyPoint,corners[i],corners[(i+1)%4],predistance)) return false;
//            }
//        }
        for (int i = 0; i < corners.length; i++) {
            if(cornerIsShootable(corners[i],enemyPoint,distance)) return true;
        }
//        System.out.println(enemyPoint + " dis:" + distance);
        for (int i = 0; i < corners.length; i++) {
            if(edgeIsShootable(enemyPoint,corners[i],corners[(i+1)%4],distance)) return true;
        }
        System.out.println("fehler2");
        return false;
    }
    public static boolean edgeIsShootable(Point enemyPosition,Point corner1,Point corner2,double distance){
        if(Math.min(corner1.getX(),corner2.getX())<=enemyPosition.getX()&&Math.max(corner1.getX(),corner2.getX())>=enemyPosition.getX()){
            System.out.println("luu"+Math.abs(corner1.getY()-enemyPosition.getY()));
            return Math.abs(corner1.getY()-enemyPosition.getY())<=distance;
        }

        if(Math.min(corner1.getY(),corner2.getY())<=enemyPosition.getY()&&Math.max(corner1.getY(),corner2.getY())>=enemyPosition.getY()){
            System.out.println("lu2u"+Math.abs(corner1.getX()-enemyPosition.getX()));
            return Math.abs(corner1.getX()-enemyPosition.getX())<=distance;
        }

        return false;
    }
    public static boolean cornerIsShootable(Point corner,Point emenyPosition,double distance){
        return corner.distance(emenyPosition)<=distance;
    }
    public static boolean couldThisBulletHitUs(double distance,double degree,Point position,Point enemyPosition){
        enemyPosition=enemyPosition.subtract(position);
//        System.out.println(enemyPosition + "e pos");
        Point goalPoint=enemyPosition.add(Point.fromPolarCoordinates(degree,distance));
//        System.out.println(goalPoint + "g pos");
        for (int i = 0; i < 4; i++) {
            if(isIntersecting(enemyPosition,goalPoint,corners[i],corners[(i+1)%4])) return true;
        }
        return false;
    }
    public static Point[] getRectangle(double width, double height){
        Point[] p = new Point[4];
        p[0]=new Point(0,0);
        p[1]=new Point(width,0);
        p[2]=new Point(width,height);
        p[3]=new Point(0,height);
        return p;
    }
    public static boolean isInEnemy(Point p, Point pos){
        p=p.subtract(pos);
        return Math.abs(p.getX())<=18&&Math.abs(p.getY())<=18;
    }
    public static boolean hitEnemy(Point past, Point now, Point enemyPos){
        if(isInEnemy(now,enemyPos)) return true;
        boolean b=false;
        for (int i = 0; i < corners.length; i++) {
            b|=isIntersecting(past,now,corners[i],corners[(i+1)% corners.length]);
        }
        return b;
    }
}
