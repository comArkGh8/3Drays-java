package rays;

import java.util.Arrays;
import static java.lang.System.out;

import java.util.Collections;
import java.util.List;

import org.joml.Vector3f;

public class Camera {
    public final FixedVector camEye;
    private final FixedVector camUpVector;
    
    // make as unmodifiable
    private final List<FixedVector> camFrame;
    
    public final float fovy;
    
    
    // init Camera
    public Camera(FixedVector eye, FixedVector center, FixedVector up, float fieldOV){

        // transform to FixedVectors:   
        this.camEye = eye;    
        this.camUpVector = up;
        
        // calculate camera coordinates
        FixedVector upUnit = up.normalize();
           
        FixedVector w = eye.subtractFixed(center).normalize(); // eye - center  
        FixedVector u = upUnit.cross(w).normalize();
        FixedVector v = w.cross(u);
        
        // make frame into list
        List<FixedVector> frameList = Arrays.asList(w,u,v);
        List<FixedVector> frame = Collections.unmodifiableList(frameList);
        
        camFrame = frame;
        
        fovy = fieldOV;
    }
    
    
    public List<FixedVector> getCamFrame(){
        return this.camFrame;
    }
    
    public Ray generateCamRay(int i, int j, int width, int height) {
        FixedVector w = this.camFrame.get(0);
        FixedVector u = this.camFrame.get(1);
        FixedVector v = this.camFrame.get(2);

        FixedVector rayStart = this.camEye;

        // use radians!
        double fovyRad = Math.toRadians(this.fovy);

        // define values as in slides
        // float alpha = tan(fovx/2)*(j-width/2)/(width/2);
        //       fovx = w/h * fovy'
        double widthD = (double) width;
        double heightD = (double) height;
        double iD = (double) i;
        double jD = (double) j;
        
        // for alpha
        double scaleForWidth = (widthD/heightD); 
        double scaleFOV = scaleForWidth * Math.tan(fovyRad/2.0);
        float alpha = (float) (scaleFOV * (jD+0.5-widthD/2.0)/(widthD/2.0));
        
        // for beta
        double FOV = Math.tan(fovyRad/2.0);
        float beta = (float) (FOV * (heightD/2.0-(iD+0.5))/(heightD/2.0));
        // alpha*u+beta*v-w
        // alpha*u
        
        FixedVector au = u.multConst(alpha);
        FixedVector bv = v.multConst(beta);
        FixedVector auPlusbv = au.addFixed(bv);
        FixedVector directionRaw = auPlusbv.subtractFixed(w);
        FixedVector direction =directionRaw.normalize(); 

        Ray returnRay = new Ray(rayStart, direction);

        return returnRay;

    }
    
    

}
