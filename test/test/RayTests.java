package test;

import static org.junit.Assert.*;

import static java.lang.System.out;

import org.joml.*;
import org.junit.Test;

import rays.*;

import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RayTests {

    @Test
    public void testTransformScale(){
        Vector4f col0 = new Vector4f(2.0f, 0,0,0);
        Vector4f col1 = new Vector4f(0,1.0f,0,0);
        Vector4f col2 = new Vector4f(0,0,1.0f,0);
        Vector4f col3 = new Vector4f(0,0,0,1.0f);
        
        Matrix4f scaleMatrix = new Matrix4f(col0,col1,col2,col3);
        FixedMatrix4 fixedScaleMatrix= new FixedMatrix4(scaleMatrix);
       
        // make ray (1,1)
        FixedVector testStart = new FixedVector(0,0,0);
        FixedVector testDir = new FixedVector(1.0f,1.0f,0);
        Ray xyRay = new Ray(testStart, testDir);
        
        Ray xfrmRay = Ray.transformRayToPrimitive(xyRay, fixedScaleMatrix);
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
        Triangle testTri = new Triangle(vert1, vert2, vert3, new FixedMatrix4());
        
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
    
    @Test
    public void testClosestTwoTri(){
        
        Matrix4f posMatrix = new Matrix4f();
        posMatrix.translate(-2.0f, 0, 0); 
        posMatrix.scale(2.0f, 1.0f, 1.0f);   
        FixedMatrix4 fixedPosMatrix= new FixedMatrix4(posMatrix);
        
        // make tri 1
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);       
        Triangle testTri = new Triangle(vert1, vert2, vert3, new FixedMatrix4());
        
        // make tri 2
        Vector3f vert21 = new Vector3f(1.0f, 0, 0);
        Vector3f vert22 = new Vector3f(0, 1.0f, 0);
        Vector3f vert23 = new Vector3f(0, 0, 1.0f);       
        Triangle testTri2 = new Triangle(vert1, vert2, vert3, fixedPosMatrix);
        
        Map<Integer,Primitive> idTriMap = new HashMap();       
        idTriMap.put(1, testTri);
        idTriMap.put(2, testTri2);
        
        // make ray (1,1)
        FixedVector testStart = new FixedVector(5,0.5f,0);
        FixedVector testDir = new FixedVector(-1.0f,0,0);
        Ray xyRay = new Ray(testStart, testDir);
        
        Map<Integer,FixedVector> closestObj = xyRay.getClosestObject(idTriMap);
        
        Set<Integer> ans = new HashSet();
        ans.add(1);
        assertEquals("closest should be tri 1", ans, closestObj.keySet());
    }  
    
    @Test
    public void testClosestTwoTriFromBack(){
        
        Matrix4f posMatrix = new Matrix4f();
        posMatrix.translate(-2.0f, 0, 0); 
        //posMatrix.scale(2.0f, 1.0f, 1.0f); 
        FixedMatrix4 fixedPosMatrix= new FixedMatrix4(posMatrix);
        
        // make tri 1
        Vector3f vert1 = new Vector3f(1.0f, 0, 0);
        Vector3f vert2 = new Vector3f(0, 1.0f, 0);
        Vector3f vert3 = new Vector3f(0, 0, 1.0f);       
        Triangle testTri = new Triangle(vert1, vert2, vert3, new FixedMatrix4());
        
        // make tri 2
        Vector3f vert21 = new Vector3f(1.0f, 0, 0);
        Vector3f vert22 = new Vector3f(0, 1.0f, 0);
        Vector3f vert23 = new Vector3f(0, 0, 1.0f);       
        Triangle testTri2 = new Triangle(vert1, vert2, vert3, fixedPosMatrix);
        
        Map<Integer,Primitive> idTriMap = new HashMap();       
        idTriMap.put(1, testTri);
        idTriMap.put(2, testTri2);
        
        // make ray (1,1)
        FixedVector testStart = new FixedVector(-5,0.5f,0);
        FixedVector testDir = new FixedVector(1.0f,0,0);
        Ray xyRay = new Ray(testStart, testDir);
        
        Map<Integer,FixedVector> closestObj = xyRay.getClosestObject(idTriMap);
        
        Set<Integer> ans = new HashSet();
        ans.add(2);
        assertEquals("closest should be tri 2", ans, closestObj.keySet());
        //out.println(closestObj.get(2).x());
    } 
    
    @Test
    public void testClosestTriSphere(){
        
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
        
        Map<Integer,Primitive> idTriMap = new HashMap();       
        idTriMap.put(1, testTri);
        idTriMap.put(2, testSph);
        
        // make ray (1,1)
        FixedVector testStart = new FixedVector(5,0.5f,0);
        FixedVector testDir = new FixedVector(-1.0f,0,0);
        Ray xyRay = new Ray(testStart, testDir);
        
        Map<Integer,FixedVector> closestObj = xyRay.getClosestObject(idTriMap);
        
        Set<Integer> ans = new HashSet();
        ans.add(1);
        assertEquals("closest should be tri 1", ans, closestObj.keySet());
        
        // make ray start on y axis
        FixedVector testStart2 = new FixedVector(5, 1.5f, 0);
        FixedVector testDir2 = new FixedVector(-1.0f,0,0);
        Ray outyRay = new Ray(testStart2, testDir2);
        Map<Integer,FixedVector> closestObj2 = outyRay.getClosestObject(idTriMap);
        
        Set<Integer> ans2 = new HashSet();
        ans2.add(2);
        assertEquals("closest should be sphere 2", ans2, closestObj2.keySet());
        
    }  
    
    
    @Test
    public void testRayOnCorrectSide(){      
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
        
        
        // make pt Light at (1,1,1) with red color
        FixedVector pointPos = new FixedVector(1.0f,1.0f,1.0f);
        Light ptLight = new PointLight(pointPos, 1.0f, 0,0);
        
        // make directional Light at (infty,0,0) with blue color
        FixedVector directionIntoLight = new FixedVector(1.0f,0,0);
        Light dirLight = new DirectionalLight(directionIntoLight, 0, 0, 1.0f);
        
        
        // make ray at (2,2,2) should hit point lighted side of tri
        FixedVector startRayFaceTri = new FixedVector(2,2,2);
        FixedVector directRayFaceTri = new FixedVector(-1,-1,-1);
        Ray rayToFaceTri = new Ray(startRayFaceTri, directRayFaceTri);
        
        // get normal at face (1,1,1)
        FixedVector triInter = testTri.rayPlaneIntersection(rayToFaceTri);
        FixedVector triNormal = testTri.getNormalAt(triInter);

        GlobalConstants myTestConst = new GlobalConstants(0.0001f,5);
        
        assertTrue("pt light hits face of tri", rayToFaceTri.rayCheckSide(triInter, triNormal, ptLight));
        
        
        // make ray at (-2,-2,-2) should not hit point lighted side of tri
        FixedVector startRayBackTri = new FixedVector(-2,-2,-2);
        FixedVector directRayBackTri = new FixedVector(1,1,1);
        Ray rayToBackTri = new Ray(startRayBackTri, directRayBackTri);
        
        // get normal at face (1,1,1)
        FixedVector triInterBack = testTri.rayPlaneIntersection(rayToBackTri);
        FixedVector triNormalBack = testTri.getNormalAt(triInterBack);
        
        assertFalse("pt light hits face of tri, ray hits back", rayToBackTri.rayCheckSide(triInter, triNormalBack, ptLight));
        assertFalse("direct light hits face of tri, ray hits back", rayToBackTri.rayCheckSide(triInter, triNormalBack, dirLight));
        
        // create ray from back of sphere
        FixedVector startRayBackSph = new FixedVector(-6,-0.5f,0);
        FixedVector directRayBackSph = new FixedVector(1,0,0);
        Ray rayToBackSph = new Ray(startRayBackSph, directRayBackSph);
        FixedVector rayBackInterSph = testSph.getHitPoint(rayToBackSph);
        FixedVector normAtBackSph = testSph.getNormalAt(rayBackInterSph);

        assertTrue("ray from behind hits sphere", testSph.rayHits(rayToBackSph));
        assertFalse("pt light hits side of sphere, ray hits back", rayToBackSph.rayCheckSide(rayBackInterSph, normAtBackSph, ptLight));
        assertFalse("dir light also hits side of sphere, ray hits back", rayToBackSph.rayCheckSide(rayBackInterSph, normAtBackSph, dirLight));
        
        // now try same with ray from back of sphere shifted to be hit by lights
        FixedVector startRayBackSph2 = new FixedVector(-6,0.5f,0);
        FixedVector directRayBackSph2 = new FixedVector(1,0,0);
        Ray rayToBackSph2 = new Ray(startRayBackSph2, directRayBackSph2);
        FixedVector rayBackInterSph2 = testSph.getHitPoint(rayToBackSph2);
        FixedVector normAtBackSph2 = testSph.getNormalAt(rayBackInterSph2);
        
        assertTrue("ray from behind hits sphere", testSph.rayHits(rayToBackSph2));
        assertFalse("pt light hits side of sphere, ray hits back", rayToBackSph2.rayCheckSide(rayBackInterSph2, normAtBackSph2, ptLight));
        assertFalse("dir light also hits side of sphere, ray hits back", rayToBackSph2.rayCheckSide(rayBackInterSph2, normAtBackSph2, dirLight));

        // now directional light but from y axis
        // make directional Light at (infty,0,0) with blue color
        FixedVector directionIntoLightY = new FixedVector(0,1.0f,0);
        Light dirLightY = new DirectionalLight(directionIntoLightY, 0, 0, 1.0f);
        assertTrue("dirY light also hits side of sphere, ray hits back/side", rayToBackSph2.rayCheckSide(rayBackInterSph2, normAtBackSph2, dirLightY));

    }
    
    
    @Test
    public void testRayReflectsToSphere(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\submission_scenes\\scene4-diffuse.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene4;
        File fileToInsert = file.get();
        try {
            testScene4 = new Scene(fileToInsert);
            // get objects
            Map<Integer,Primitive> objectList = testScene4.objectIdMapFinal;
            // get camera
            Camera scene4Cam = testScene4.sceneCam;       
            
            // get ray from cam to (.9,.3,.25)
            FixedVector testPoint = new FixedVector(1.45f, 0.67f, 0.25f);
            FixedVector start = scene4Cam.camEye;
            FixedVector dir = testPoint.subtractFixed(start);
            
            Ray camToTestPt = new Ray(start, dir);

            // get ray intersection       
            Map<Integer, FixedVector> objIdWithHit = camToTestPt.getClosestObject(objectList);
            int objHitId = objIdWithHit.keySet().iterator().next(); 
            // double check hitPt
            FixedVector hitPt = objIdWithHit.get(11);
            
            Primitive objHit = objectList.get(11);
            FixedVector normal = objHit.getNormalAt(hitPt);
            // now reflect
            FixedVector reflectDirection = Geometry.reflectDirectionVector(dir, normal);
            
            Ray reflectRay = new Ray(hitPt, reflectDirection);
            // make new object list without table and see if hits sphere
            Map<Integer, Primitive> validObjects = new HashMap();
            for (int aKey: objectList.keySet()) {
                if (aKey != objHitId) {
                    validObjects.put(aKey, objectList.get(aKey));
                }
            }
            
            assertTrue("sphere should be there", validObjects.containsKey(65));
            
            Map<Integer, FixedVector> idHitReflect = reflectRay.getClosestObject(validObjects);
            //out.println(idHitReflect.keySet());
            //out.println(reflectDirection.toString());

            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
