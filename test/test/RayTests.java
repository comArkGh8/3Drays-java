package test;

import static org.junit.Assert.*;

import static java.lang.System.out;

import org.joml.*;
import org.junit.Test;

import rays.*;

import java.lang.Math;

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
    
    @Test
    public void testDirectionNormal(){      
        // make ray (1,1)
        FixedVector testStart = new FixedVector(0,0,0);
        FixedVector testDir = new FixedVector(1.0f,1.0f,0);
        Ray xyRay = new Ray(testStart, testDir);

        FixedVector dirVector = xyRay.getDirectionVector();
        //out.println(dirVector.x());
        //out.println(dirVector.y());
        //out.println(dirVector.z());
        
        assertTrue(dirVector.x()==dirVector.y());      
    } 
    
    @Test
    public void testReflectRayNormal111(){      
        // make ray (1,1)
        FixedVector testStart = new FixedVector(5,0.5f,0);
        FixedVector testDir = new FixedVector(-1.0f,0,0);
        Ray xyRay = new Ray(testStart, testDir);
        
        // make tri
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);       
        Triangle testTri = new Triangle(vert1, vert2, vert3, new Matrix4f());
        
        FixedVector reflectPt = testTri.rayPlaneIntersection(xyRay); // = (0.5, 0.5, 0.5)
        FixedVector testNormal = new FixedVector(1, 1, 1);

        // reflect ray
        Ray reflectedRay = xyRay.getReflectionAcross(reflectPt, testNormal);
        
        assertTrue("start should be at 0.5, 0.5, 0", 0.5f == reflectedRay.getStartVector().x());   
        assertTrue("start should be at 0.5, 0.5, 0", 0.5f == reflectedRay.getStartVector().y());
        assertTrue("start should be at 0.5, 0.5, 0", 0 == reflectedRay.getStartVector().z());
        
        GlobalConstants myConstant = new GlobalConstants(.00001);
        
        assertTrue("reflect ray should be in (-1,2,2) direction",
                Math.abs(reflectedRay.getDirectionVector().y() + 2 * reflectedRay.getDirectionVector().x() ) < 
                GlobalConstants.acceptableError);
        assertTrue("reflect ray should be in (-1,2,2) direction",
                Math.abs(reflectedRay.getDirectionVector().z() + 2 * reflectedRay.getDirectionVector().x() ) < 
                GlobalConstants.acceptableError);
    } 

}
