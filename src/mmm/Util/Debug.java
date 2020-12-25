package mmm;

public class Debug {
    private static final int debuglevel = 1;
    public static<T> void println(T t){ println(t,0);}
        public static<T> void println(T t, int priority){
        if(priority<debuglevel||debuglevel==-1) return;
        System.out.println(t.toString());
    }
    public static<T> void print(T t){ print(t,0);}
    public static <T> void print(T t,int priority){
        if(priority<debuglevel||debuglevel==-1) return;
        System.out.print(t);
    }
}
