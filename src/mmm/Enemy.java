package mmm;

public class Enemy {
    private double bearing, heading, velocity, distance;
    Point position, relativePosition;
    public void update(ScannedRobotEvent e, Point position){
        bearing=e.getBearing();
        heading = e.getHeading();
        velocity = e.getVelocity();
        distance= e.getDistance();
        relativePosition = Point.fromPolarCoordinates(heading+bearing,distance);
        position = position.add(relativePosition);
    }

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

    public Point getPosition() {
        return position;
    }

    public Point getRelativePosition() {
        return relativePosition;
    }
}

