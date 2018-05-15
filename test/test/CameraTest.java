package test;

import static org.junit.Assert.*;
import static java.lang.System.out; 

import org.joml.Vector3f;
import org.junit.Test;

import rays.Camera;
import rays.FixedVector;
import rays.Ray;

public class CameraTest {

    @Test
    public void testCamRayAtCenter() {

        FixedVector eyeVec= new FixedVector(1,1,1);
        FixedVector centerVec = new FixedVector(0,0,0);
        FixedVector origUpVec = new FixedVector(0,0,1);
        Camera testCam = new Camera(eyeVec, centerVec, origUpVec, 30);
        
        FixedVector w = testCam.getCamFrame().get(0);

        
        
        int width = 640;
        int height = 480;
        Ray camRay = testCam.generateCamRay(240, 320, width, height);
     
        //out.println(camRay.getDirectionVector().x());
        //out.println(camRay.getDirectionVector().y());
        //out.println(camRay.getDirectionVector().z());
        
        assertTrue("direction should be appprox -1,-1,-1",
                camRay.getDirectionVector().x()-camRay.getDirectionVector().y()<0.01 );
        assertTrue("direction should be appprox -1,-1,-1",
                camRay.getDirectionVector().x()-camRay.getDirectionVector().z()<0.01 );
 
        
    }

}
