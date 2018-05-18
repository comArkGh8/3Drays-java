package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.System.out;

import org.junit.Test;

import rays.Camera;
import rays.DirectionalLight;
import rays.FixedMatrix4;
import rays.FixedVector;
import rays.Geometry;
import rays.GlobalConstants;
import rays.Light;
import rays.PointLight;
import rays.Primitive;
import rays.Ray;
import rays.Scene;
import rays.Sphere;
import rays.Triangle;

public class LightTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetDirection(){      
        // make pt at (1,1,1) with red color
        FixedVector pointPos = new FixedVector(1.0f,1.0f,1.0f);
        Light ptLight = new PointLight(pointPos, 1.0f, 0,0);
        
        // make dir shining in -x direction with color blue
        FixedVector toDir = new FixedVector(1.0f,0,0);
        Light dirLight = new DirectionalLight(toDir, 0,0,1.0f);
        
        //out.println(dirLight.getDirectionTo());
        ptLight.getDirectionTo();      
 
    } 
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetPosn(){      
        // make pt at (1,1,1) with red color
        FixedVector pointPos = new FixedVector(1.0f,1.0f,1.0f);
        Light ptLight = new PointLight(pointPos, 1.0f, 0,0);
        
        // make dir shining in -x direction with color blue
        FixedVector toDir = new FixedVector(1.0f,0,0);
        Light dirLight = new DirectionalLight(toDir, 0,0,1.0f);
        
        //out.println(ptLight);
        dirLight.getPosition();      
 
    } 
    
    @Test
    public void testPtReachesObj(){      
        // setup test scene
        Matrix4f posMatrix = new Matrix4f();
        posMatrix.translate(-2.0f, 0, 0); 
        posMatrix.scale(1.0f, 2.0f, 1.0f); 
        FixedMatrix4 fixedPosMatrix= new FixedMatrix4(posMatrix);
        
        // make tri 1
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);       
        Triangle testTri = new Triangle(vert1, vert2, vert3, new FixedMatrix4());
        
        // make sphere     
        Vector3f center = new Vector3f(0, 0, 0);
        float radius = 1.0f;       
        Sphere testSph = new Sphere(center, radius, fixedPosMatrix);
        
        // make the list of objects
        Map<Integer,Primitive> justTri= new HashMap();
        justTri.put(1, testTri);
        // make the list of objects
        Map<Integer,Primitive> justSph= new HashMap();
        justSph.put(2, testSph);
        
        // make pt Light at (1,1,1) with red color
        FixedVector pointPos = new FixedVector(1.0f,1.0f,1.0f);
        Light ptLight = new PointLight(pointPos, 1.0f, 0,0);
        
        // make directional Light at (infty,0,0) with blue color
        FixedVector directionIntoLight = new FixedVector(1.0f,0,0);
        Light dirLight = new DirectionalLight(directionIntoLight, 0, 0, 1.0f);
        
        float oneThird = 1.0f/3.0f;
        // some hit points
        FixedVector hitPtVertTri = new FixedVector(1,0,0);
        FixedVector hitPtCenterTri = new FixedVector(oneThird,oneThird,oneThird);
        
        
        // get hit point of point light to sphere with a ray
        FixedVector centerFixed = new FixedVector(-2.0f,0,0);
        FixedVector directionToSphere = centerFixed.subtractFixed(pointPos);
        Ray pointLightSphereRay = new Ray(pointPos, directionToSphere);
        FixedVector hitPtSphere = pointLightSphereRay.getClosestObject(justSph).get(2);
        FixedVector testHitSphereBehindTri = new FixedVector(-1.0f,0.1f,0);
        
        
        assertTrue("pt light should reach triangle", ptLight.reaches(hitPtVertTri, justSph));
        assertTrue("pt light should reach sphere", ptLight.reaches(hitPtSphere, justTri));
        assertTrue("dir light should reach triangle in middle", dirLight.reaches(hitPtCenterTri, justSph));
        assertFalse("dir light should not reach sphere in middle", dirLight.reaches(testHitSphereBehindTri, justTri));
        
    } 
    
    @Test
    public void testLightRayReachesBack(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\submission_scenes\\scene6.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene6;
        File fileToInsert = file.get();
        try {
            testScene6 = new Scene(fileToInsert);
            // get objects
            Map<Integer,Primitive> objectList = testScene6.objectIdMapFinal;
            // get camera
            Camera scene6Cam = testScene6.sceneCam;       
            
            // get ray from cam to back (001 to 010)
            FixedVector start = scene6Cam.camEye;
            FixedVector dir = new FixedVector(0,1,-1);
            
            Ray camToBack = new Ray(start, dir);
            
            // try with camera ray
            Ray camToBack2 =  scene6Cam.generateCamRay(200, 320, 640, 480);

            // get ray intersection       
            Map<Integer, FixedVector> objIdWithHit = camToBack2.getClosestObject(objectList);
            int objHitId = objIdWithHit.keySet().iterator().next(); 
            //out.println(objHitId);
            // get objId: 9
            Primitive bottomBox = objectList.get(9);
            FixedVector hitPt = objIdWithHit.get(objHitId);
            FixedVector normal = bottomBox.getNormalAt(hitPt);
            // check if light reaches
            Light ptLight = testScene6.lightIdMapFinal.get(1);
            assertTrue("light reaches", ptLight.reaches(hitPt, objectList) );
            FixedVector colorBottom = camToBack2.getRayColorFrom(bottomBox, hitPt, testScene6);
            
            // check side
            boolean rayOnCorrectSide = camToBack2.rayCheckSide(hitPt, normal, ptLight);
            assertTrue("ray should be on same side as bottom", rayOnCorrectSide);
            
            assertTrue("should not be 0 color", colorBottom.length()>GlobalConstants.acceptableError);
            
            
            //FixedVector hitPt = objIdWithHit.get(11);

            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    

}
