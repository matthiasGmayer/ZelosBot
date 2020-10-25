package mmm;
public class ZelosBot extends SimpleRobot{
    static{
        System.out.println("Robo");
    }
    public ZelosBot() {
        behavior=new MyFirstBehavior(this);
    }
}
