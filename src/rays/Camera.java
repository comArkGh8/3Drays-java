package rays;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joml.Vector3f;

public class Camera {
    public final FixedVector camEye;
    private final FixedVector camToCenter;
    private final FixedVector camUpVector;
    
    // make as unmodifiable
    private final List<FixedVector> camFrame;
    
    public final float fovy;
    
    
    // init Camera
    public Camera(Vector3f eye, Vector3f center, Vector3f up, float fieldOV){

        // transform to FixedVectors:   
        camEye = new FixedVector(eye);
        
        Vector3f camCenterVec = new Vector3f();
        center.sub(eye, camCenterVec);
        camToCenter = new FixedVector(camCenterVec);
        
        camUpVector = new FixedVector(up);
        
        // calculate camera coordinates
        Vector3f upUnit = new Vector3f();
        up.normalize(upUnit);
        
        camCenterVec.mul(-1).normalize(); // eye - center     
        FixedVector w = new FixedVector(camCenterVec);
        
        Vector3f secondVec = new Vector3f();
        upUnit.cross(camCenterVec, secondVec);
        FixedVector u = new FixedVector(secondVec);
        
        Vector3f thirdVec = new Vector3f();
        camCenterVec.cross(secondVec, thirdVec);
        FixedVector v = new FixedVector(thirdVec);
        
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
        float thetaY = (float) (Math.toRadians(this.fovy)/2.0);

        // define values as in slides
        // float alpha = tan(fovx/2)*(j-width/2)/(width/2);
        //       fovx = w/h * fovy'
        float widthFlt = (float) width;
        float heightFlt = (float) height;
        float alpha = (float) ((widthFlt/heightFlt) * Math.tan(thetaY) * 
                (j+0.5-widthFlt/2.0)/(widthFlt/2.0));
        float beta = (float) (Math.tan(thetaY) * (heightFlt/2-(i+0.5))/(heightFlt/2.0));

        // alpha*u+beta*v-w
        FixedVector directionRaw = (u.multConst(alpha) ).addFixed(v.multConst(beta) )
                .subtractFixed(w);
        FixedVector direction =directionRaw.normalize(); 

        Ray returnRay = new Ray(rayStart, direction);

        return returnRay;

    }
    
    

}
