package rays;

public class GlobalConstants {
    
    public static double acceptableError;
    public static int maxDepth;
    
    public GlobalConstants(double error, int depth) {
        acceptableError = error;
        maxDepth = depth;
    }
    
    public GlobalConstants(double d) {
        acceptableError = d;
        maxDepth = 5;
    }

}
