package rays;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joml.Vector3f;

public class Camera {
    FixedVector camEye;
    FixedVector camToCenter;
    FixedVector camUpVector;
    
    // make as unmodifiable
    List<FixedVector> camFrame;
    
    final float fovy;
    
    
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

}
