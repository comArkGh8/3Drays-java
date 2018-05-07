package test;

import static org.junit.Assert.*;

import static java.lang.System.out;

import org.joml.*;
import org.junit.Test;

import rays.*;

public class RayTests {

    @Test
    public void testTransformScale(){
        Vector4f col0 = new Vector4f(2.0f, 0,0,0);
        Vector4f col1 = new Vector4f(0,1.0f,0,0);
        Vector4f col2 = new Vector4f(0,0,1.0f,0);
        Vector4f col3 = new Vector4f(0,0,0,1.0f);
        
        Matrix4f scaleMatrix = new Matrix4f(col0,col1,col2,col3);
       
        // make ray (1,1)
        FixedVector testStart = new FixedVector(0,0,0);
        FixedVector testDir = new FixedVector(1.0f,1.0f,0);
        Ray xyRay = new Ray(testStart, testDir);
        
        Ray xfrmRay = Ray.transformRayToPrimitive(xyRay, scaleMatrix);
        FixedVector xfrmStart = xfrmRay.getStartVector();
        FixedVector xfrmDir = xfrmRay.getDirectionVector();
        
        //out.println(xfrmStart.x());
        //out.println(xfrmStart.y());
        //out.println(xfrmStart.z());
        
        //out.println(xfrmDir.x());
        //out.println(xfrmDir.y());
        //out.println(xfrmDir.z());
        
        assertTrue(xfrmDir.y()==2*xfrmDir.x());      
    }  

}
