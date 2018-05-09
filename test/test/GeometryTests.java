package test;

import static org.junit.Assert.assertTrue;
import static java.lang.System.out;

import org.junit.Test;

import rays.FixedVector;
import rays.Geometry;

public class GeometryTests {
    
    @Test
    public void testReflectNormalZZeroPlane(){      
        // make direction(1,1)
        FixedVector testNormal = new FixedVector(0,0,1.0f);
        FixedVector testDir = new FixedVector(1.0f,1.0f,1.0f);
        
        FixedVector bounceVec = Geometry.reflectDirectionVector(testDir, testNormal);
        
        assertTrue(bounceVec.x()==bounceVec.y());   
        assertTrue(bounceVec.z()==-bounceVec.y()); 
    } 

}
