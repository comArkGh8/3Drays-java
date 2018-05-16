package test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Test;

import rays.FixedMatrix4;
import rays.FixedVector;
import rays.GlobalConstants;
import rays.Ray;
import rays.Sphere;
import rays.Triangle;

public class SphereTest {

    @Test
    public void testNormalScale(){
        Vector4f col0 = new Vector4f(2.0f, 0,0,0);
        Vector4f col1 = new Vector4f(0,1.0f,0,0);
        Vector4f col2 = new Vector4f(0,0,1.0f,0);
        Vector4f col3 = new Vector4f(0,0,0,1.0f);
        
        Matrix4f scaleMatrix = new Matrix4f(col0,col1,col2,col3);
        FixedMatrix4 fixedScaleMatrix = new FixedMatrix4(scaleMatrix);
        
        Vector3f center = new Vector3f(0, 0, 0);
        float radius = 1.0f;
        
        FixedVector inputPt = new FixedVector(0,0,1.0f);

        
        Sphere testSph = new Sphere(center, radius, fixedScaleMatrix);
        //out.println(testSph.getNormalAt(inputPt).x());
        //out.println(testSph.getNormalAt(inputPt).y());
        //out.println(testSph.getNormalAt(inputPt).z());
        assertEquals("normal in z dir", (int) 1, (int) testSph.getNormalAt(inputPt).z());       
    }    
    
    @Test
    public void testNormalShiftScale(){
        
        Matrix4f posMatrix = new Matrix4f();
        posMatrix.translate(2.0f, 0, 0); 
        posMatrix.scale(2.0f, 1.0f, 1.0f);       
        //out.println(posMatrix);
        FixedMatrix4 fixedPosMatrix = new FixedMatrix4(posMatrix);
        
        Vector3f center = new Vector3f(0, 0, 0);
        float radius = 1.0f;
        
        FixedVector inputPt = new FixedVector(3, 0.8660254037844386f, 0);
        
        Sphere testSph = new Sphere(center, radius, fixedPosMatrix);
                
        //out.println(testSph.getNormalAt(inputPt).x());
        //out.println(testSph.getNormalAt(inputPt).y());
        //out.println(testSph.getNormalAt(inputPt).z());
        assertTrue("normal in pos x-y dir", testSph.getNormalAt(inputPt).x()>0);    
        assertTrue("normal in pos x-y dir", testSph.getNormalAt(inputPt).y()>0);
    }  
    
    @Test
    public void testRaySphereIntersect(){
        GlobalConstants myConstant = new GlobalConstants(.00001);
        
        Vector3f center = new Vector3f(0, 0, 0);
        float r = 1;
        
        Sphere testSphere = new Sphere(center, r, new FixedMatrix4());

        FixedVector rayStart = new FixedVector(2.0f, 2.0f, 2.0f);
        FixedVector rayDirectionTo = new FixedVector(-2.0f, -2.0f, -2.0f);
        FixedVector rayDirectionAway = new FixedVector(2.0f, 2.0f, 2.0f);
        
        Ray testRayTo = new Ray(rayStart, rayDirectionTo);
        Ray testRayAway = new Ray(rayStart, rayDirectionAway);
        
        assertTrue("to ray intersects", testSphere.rayHits(testRayTo));
        assertFalse("away does not intersect", testSphere.rayHits(testRayAway));
           
    }
        
    @Test
    public void testRayTransformedSphereIntersect(){
        GlobalConstants myConstant = new GlobalConstants(.00001);
        
        Matrix4f posMatrix = new Matrix4f();
        posMatrix.translate(4.0f, 0, 0); 
        posMatrix.scale(2.0f, 1.0f, 1.0f);       
        //out.println(posMatrix);
        FixedMatrix4 fixedPosMatrix = new FixedMatrix4(posMatrix);
        
        Vector3f center = new Vector3f(0, 0, 0);
        float radius = 1.0f;        
        
        Sphere testSphere = new Sphere(center, radius, fixedPosMatrix);
        

        FixedVector rayStart = new FixedVector(2.0f, 2.0f, 2.0f);
        
        FixedVector pointInSphere = new FixedVector(5, 0, 0);
        FixedVector rayDirectionTo = pointInSphere.subtractFixed(rayStart);
        
        FixedVector pointOutSphere = new FixedVector(7, 0, 0);
        FixedVector rayDirectionToOut = pointOutSphere.subtractFixed(rayStart);
        
        Ray testRayTo = new Ray(rayStart, rayDirectionTo);  
        
        Ray testRayToOut = new Ray(rayStart, rayDirectionToOut); 
        
        assertTrue("to ray intersects", testSphere.rayHits(testRayTo));
        assertFalse("to ray intersects", testSphere.rayHits(testRayToOut));
           
    }
        
       

}
