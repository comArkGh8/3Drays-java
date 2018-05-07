


package rays;

import org.joml.*;

public class Ray {
    
    FixedVector start;
    FixedVector direction;
    
    public Ray(Vector3f startVector, Vector3f directionVector) {
        
        this.start = new FixedVector(startVector);     
        this.direction = new FixedVector(directionVector);

    }
    
    public Ray(FixedVector startVector, FixedVector directionVector) {
        
        this.start = startVector;     
        this.direction = directionVector;

    }
    
    public FixedVector getStartVector() {
        return this.start;
    }
    
    public FixedVector getDirectionVector() {
        return this.direction;
    }
    
    /**
     * object is represented as primitive 
     * @param ray - ray in world (actual) coordinates
     * @param objMat - input the objects matrix
     * @return - the ray as if the object was not transformed to actual coordinates
     */
    public static Ray transformRayToPrimitive(Ray ray, Matrix4f objMat) {
        
        // first get inverse matrix
        Matrix4f objInv = new Matrix4f();
        objMat.invert(objInv);
        
        // transform start
        Vector4f rayStartExtend = new Vector4f(ray.start.x(), ray.start.y(), ray.start.z(), 1.0f);
        Vector4f primitiveRayStartExtended = new Vector4f();
        rayStartExtend.mul(objInv,primitiveRayStartExtended);
        // put into Vec3
        Vector3f primitiveRayStarVec3 = new Vector3f(primitiveRayStartExtended.x/primitiveRayStartExtended.w,
                primitiveRayStartExtended.y/primitiveRayStartExtended.w,
                primitiveRayStartExtended.z/primitiveRayStartExtended.w);
        
        FixedVector primitiveFixedStart = new FixedVector(primitiveRayStarVec3.x,primitiveRayStarVec3.y,primitiveRayStarVec3.z);
        
        // transform direction
        Vector4f rayDirExtend = new Vector4f(ray.direction.x(), ray.direction.y(), ray.direction.z(), 0);
        Vector4f primitiveRayDirExtended = new Vector4f();
        rayDirExtend.mul(objInv,primitiveRayDirExtended);
        Vector3f primitveRaWRayVec3 = new Vector3f(primitiveRayDirExtended.x,primitiveRayDirExtended.y,primitiveRayDirExtended.z);
        primitveRaWRayVec3.normalize();
        
        FixedVector primitiveFixedDir = new FixedVector(primitveRaWRayVec3.x,primitveRaWRayVec3.y,primitveRaWRayVec3.z);
        
        // create Ray from start and direction
        Ray primitiveReturnRay = new Ray(primitiveFixedStart, primitiveFixedDir);
        
        return primitiveReturnRay;
    }

}
