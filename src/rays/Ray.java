


package rays;

import static java.lang.System.out;

import java.awt.Color;

import org.joml.*;

public class Ray {
    
    FixedVector start;
    FixedVector direction;
    
    public Ray(Vector3f startVector, Vector3f directionVector) {
        
        this.start = new FixedVector(startVector);
        // normalize direction vector!
        Vector3fc normalDir = directionVector.normalize();
        this.direction = new FixedVector(normalDir);

    }
    
    public Ray(FixedVector startVector, FixedVector directionVector) {
        
        this.start = startVector;          
        // normalize direction vector!
        FixedVector normalDir = directionVector.normalize();
        this.direction = normalDir;

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
    public static Ray transformRayToPrimitive(Ray ray, Matrix4fc objMat) {
        
        // first get inverse matrix
        Matrix4f objInv = new Matrix4f();
        objMat.invert(objInv);
        
        // transform start
        Vector3fc inputStartVector = new Vector3f(ray.start.x(), ray.start.y(), ray.start.z());
        FixedVector primitiveFixedStart = Geometry.mat4MultPosVec3(inputStartVector, objInv); 
          
        // transform direction
        Vector3fc inputDirectionVector = new Vector3f(ray.direction.x(), ray.direction.y(), ray.direction.z());
        FixedVector primitiveFixedDir = Geometry.mat4MultDirVec3(inputDirectionVector, objInv); 
        
        // create Ray from start and direction
        Ray primitiveReturnRay = new Ray(primitiveFixedStart, primitiveFixedDir);
        
        return primitiveReturnRay;
    }
   
    /**
    *  returns ray which is reflected at given point according to normal provided
    *  
    * @param normalVector - normal vector of surface across which reflection occurs
    * @param reflectPt - pt at which reflection occurs
    * @return - the ray with the reflected direction (use Geometry method, which returns normalized direction)
    */  
    public Ray getReflectionAcross(FixedVector reflectPt, FixedVector normalVector) {
        // get direction for reflected 
        FixedVector inRayDir = this.direction;
        
        FixedVector reflectRayDir = Geometry.reflectDirectionVector(inRayDir, normalVector);
        
        // make new Ray and return
        Ray reflectRay = new Ray(reflectPt, reflectRayDir);

        return reflectRay;
    }
    
    
    public Color getRayColorFrom(Primitive objHit, FixedVector hiPt) {
        // IM HERE USE LIGHT.COMPUTELIGHT METHOD!!!!
        
        
        return null;
    }
    
    
    
    
    
    
    
    

}
