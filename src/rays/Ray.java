


package rays;

import org.joml.*;

public class Ray {
    
    Vector3fc start;
    Vector3fc direction;
    
    public Ray(Vector3f startVector, Vector3f directionVector) {
        
        this.start = new Vector3f(startVector);     
        this.direction = new Vector3f(directionVector);

    }

}
