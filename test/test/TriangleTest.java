package test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import org.joml.*;
import org.junit.Test;

import rays.FixedMatrix4;
import rays.FixedVector;
import rays.GlobalConstants;
import rays.Ray;
import rays.Triangle;

public class TriangleTest {

    @Test
    public void testNormalScale(){
        Vector4f col0 = new Vector4f(2.0f, 0,0,0);
        Vector4f col1 = new Vector4f(0,1.0f,0,0);
        Vector4f col2 = new Vector4f(0,0,1.0f,0);
        Vector4f col3 = new Vector4f(0,0,0,1.0f);
        
        Matrix4f scaleMatrix = new Matrix4f(col0,col1,col2,col3);
        FixedMatrix4 fixedScaleMatrix = new FixedMatrix4(scaleMatrix);
        
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);
        
        Triangle testTri = new Triangle(vert1, vert2, vert3, fixedScaleMatrix);
        //out.println(testTri.getNormalAt(null).x());
        //out.println(testTri.getNormalAt(null).y());
        //out.println(testTri.getNormalAt(null).z());
        assertTrue(testTri.getNormalAt(null).y()==2*testTri.getNormalAt(null).x());
   
    }
    
    @Test
    public void testPlaneEqnAndIntersect(){
        
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);
        
        Triangle testTri = new Triangle(vert1, vert2, vert3, new FixedMatrix4());

        FixedVector rayStart = new FixedVector(2.0f, 2.0f, 2.0f);
        FixedVector rayDirection = new FixedVector(-2.0f, 0, 0);
        
        Ray testRay = new Ray(rayStart, rayDirection);
        // intersection point:
        FixedVector interPt = testTri.rayPlaneIntersection(testRay);
        float xInter = interPt.x();
        float yInter = interPt.y();
        float zInter = interPt.z();
        
        GlobalConstants myConstant = new GlobalConstants(.00001);
        assertTrue("intersect on 1,1,1 = 1 plane", (xInter + yInter + zInter -1) < GlobalConstants.acceptableError);       
           
    }
    
    @Test
    public void testRayTriIntersect(){
        GlobalConstants myConstant = new GlobalConstants(.00001);
        
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);
        
        Triangle testTri = new Triangle(vert1, vert2, vert3, new FixedMatrix4());

        FixedVector rayStart = new FixedVector(2.0f, 2.0f, 2.0f);
        FixedVector rayDirectionTo = new FixedVector(-2.0f, -2.0f, -2.0f);
        FixedVector rayDirectionAway = new FixedVector(2.0f, 2.0f, 2.0f);
        
        Ray testRayTo = new Ray(rayStart, rayDirectionTo);
        Ray testRayAway = new Ray(rayStart, rayDirectionAway);
        
        assertTrue("to ray intersects", testTri.rayHits(testRayTo));
        assertFalse("to ray intersects", testTri.rayHits(testRayAway));
           
    }

}
