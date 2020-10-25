package mmm;

public class Calc {
    public static boolean isIntersecting(Point p0, Point p1, Point p2, Point p3){
       return isIntersecting(p0.getX(),p0.getY(),p1.getX(),p1.getY(),p2.getX(),p2.getY(),p3.getX(),p3.getY());
    }

    public static void main(String[] args) {
        Point[] p = getCorners(45,new Point(10,10));
        for (int i = 0; i < p.length; i++) {
            System.out.println(p[i]);
        }
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
                    System.out.println(l1);
                    System.out.println(l2);
                    if((y3-y2)/(y1-y0)<0){
                        return l2<=1&&l1>=0;
                    }else{
                        return l1<=1&&l2>=0;
                    }
                }
            }else{
                double m =a1/a2;
                double l = (y2-y0+m*(y3-y2))/(y1-y0);
                System.out.println(new Point(x0,y0).add(new Point(x1-x0,y1-y0).multiply(l)));
                System.out.println(new Point(x2,y2).add(new Point(x3-x2,y3-y2).multiply(m)));
                return 0<=l&&l<=1&&0<=m&&m<=1;
            }
        }
    }
    //36x36
    private static Point[] corners = new Point[]{
            new Point(-18,-18),
            new Point(+18,-18),
            new Point(+18,+18),
            new Point(-18,+18)
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
}
