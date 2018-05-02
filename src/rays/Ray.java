


package rays;

import org.joml.Vector3d;

public class Ray {
    
    FixedVector start;
    FixedVector direction;
    
    public Ray(Vector3d startVector, Vector3d directionVector) {
        float startX = (float) startVector.x;
        float startY = (float) startVector.y;
        float startZ = (float) startVector.z;
        
        this.start = new FixedVector(startX, startY, startZ);

        float dirX = (float) directionVector.x;
        float dirY = (float) directionVector.y;
        float dirZ = (float) directionVector.z;
        
        this.direction = new FixedVector(dirX, dirY, dirZ);

    }

}
