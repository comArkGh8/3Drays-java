package test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Test;

import rays.FixedVector;
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
        
        Vector3f center = new Vector3f(0, 0, 0);
        float radius = 1.0f;
        
        FixedVector inputPt = new FixedVector(0,0,1.0f);

        
        Sphere testSph = new Sphere(center, radius, scaleMatrix);
        //out.println(testSph.getNormalAt(inputPt).x());
        //out.println(testSph.getNormalAt(inputPt).y());
        //out.println(testSph.getNormalAt(inputPt).z());
        assertEquals("normal in z dir", (int) 1, (int) testSph.getNormalAt(inputPt).z());       
    }    
        
       

}
