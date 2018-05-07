package test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import org.joml.*;
import org.junit.Test;

import rays.Triangle;

public class TriangleTest {

    @Test
    public void testNormalScale(){
        Vector4f col0 = new Vector4f(2.0f, 0,0,0);
        Vector4f col1 = new Vector4f(0,1.0f,0,0);
        Vector4f col2 = new Vector4f(0,0,1.0f,0);
        Vector4f col3 = new Vector4f(0,0,0,1.0f);
        
        Matrix4f scaleMatrix = new Matrix4f(col0,col1,col2,col3);
        
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);
        
        Triangle testTri = new Triangle(vert1, vert2, vert3, scaleMatrix);
        //out.println(testTri.getNormalAt(null).x());
        //out.println(testTri.getNormalAt(null).y());
        //out.println(testTri.getNormalAt(null).z());
        assertTrue(testTri.getNormalAt(null).y()==2*testTri.getNormalAt(null).x());
        
        
        
    }

}
