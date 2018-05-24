package test;

import static java.lang.System.out;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.joml.Matrix4f;
import org.junit.Test;

import rays.*;

public class SceneTests {

    @Test
    public void testScene1Dimensions(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        
        File fileToInsert = file.get();
        //Scene testScene;

        
        try(BufferedReader bufRead = new BufferedReader(new FileReader(fileToInsert))){
            // read the first line to get size
            String myLine = bufRead.readLine();
            myLine.trim();
            while ( (myLine.length()==0) || (myLine.startsWith("#")) ){
                String[] dimensionsString = myLine.split(" ");
                myLine = bufRead.readLine();
                myLine.trim();
            }
            String[] dimensionsString = myLine.split(" ");
            
            int width= Integer.parseInt(dimensionsString[1]);
            int height= Integer.parseInt(dimensionsString[2]);
            
            assertEquals("width should be 640", 640, width);
            assertEquals("height should be 480", 480, height);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1Insert(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        
        File fileToInsert = file.get();
        Scene testScene;
        try {
            testScene = new Scene(fileToInsert);
            assertTrue("scene should be created", testScene!=null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1CountObj(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Set<Integer> theKeys = testScene.objectIdMapFinal.keySet();
            int number = theKeys.size();
            assertEquals("number of objects is 2", 2, number);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    @Test
    public void testScene7CountObj(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene7.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Set<Integer> theKeys = testScene.objectIdMapFinal.keySet();
            int number = theKeys.size();
            assertEquals("number of objects is 92", 92, number);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    @Test
    public void testScene7AltCountObj(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\submission_scenes\\scene7-alt.txt";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Set<Integer> theKeys = testScene.objectIdMapFinal.keySet();
            int number = theKeys.size();
            assertEquals("number of objects is 92", 92, number);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    
    
    @Test
    public void testScene1CamPos(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            FixedVector camEye = testScene.sceneCam.camEye;
            int xPos = (int) camEye.x();
            int yPos = (int) camEye.y();
            int zPos = (int) camEye.z();
            assertEquals("x coor. is -4", 0, xPos);
            assertEquals("y coor. is -4", -3, yPos);
            assertEquals("x coor. is 4", 3, zPos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1FinalLights(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Light light1 = testScene.lightIdMapFinal.get(1);
            Light.Type light1Type = light1.getType();
            assertEquals("first is directional", Light.Type.DIRECTIONAL, light1Type);
            assertEquals("number lights is one", 2, testScene.lightIdMapFinal.size());
            
            // try to change light
            // testScene.lightIdMapFinal.put(1, null);  Didnt wrok!

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1GetColor(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Map<Integer, Primitive> objectList = testScene.objectIdMapFinal;
            FixedVector rayStart = new FixedVector(0,0,4);
            FixedVector rayDir = new FixedVector (0.1f,0,-1);
            Ray rayToCenter = new Ray(rayStart, rayDir);
            Map<Integer, FixedVector> objIdPt= rayToCenter.getClosestObject(objectList);
            int objId = objIdPt.keySet().iterator().next();
            Primitive objHit = objectList.get(objId);
            FixedVector hitPt = objIdPt.get(objId);
            
            FixedVector colorVec = rayToCenter.getRayColorFrom(objHit, hitPt, testScene);
            
            //out.println(objId);
            //out.println(colorVec.toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene6FinalLights(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene6.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Light light1 = testScene.lightIdMapFinal.get(1);
            Light.Type light1Type = light1.getType();
            assertEquals("first is point", Light.Type.POINT, light1Type);
            assertEquals("number lights is one", 2, testScene.lightIdMapFinal.size());
            
            // try to change light
            // testScene.lightIdMapFinal.put(1, null);  //Didnt wrok!

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene6GetMatrix(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene6.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Primitive object1 = testScene.objectIdMapFinal.get(1);
            Primitive.Shape shapeType = object1.getShape();
            assertEquals("first is triangle", Primitive.Shape.TRIANGLE, shapeType);
            assertEquals("number of objects", 38, testScene.objectIdMapFinal.size());
            
            // get matrix of 5th tri
            Primitive fifthTri = testScene.objectIdMapFinal.get(5);
            FixedMatrix4 fifthMat = fifthTri.getTransformMatrix();
            assertEquals("3,4 entry is -2", -2, (int) fifthMat.m32());
            //out.println(fifthMat);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene6Sphere4Rad(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene6.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene;
        File fileToInsert = file.get();
        try {
            testScene = new Scene(fileToInsert);
            Primitive object1 = testScene.objectIdMapFinal.get(1);
            Primitive.Shape shapeType = object1.getShape();
            assertEquals("first is triangle", Primitive.Shape.TRIANGLE, shapeType);
            assertEquals("number of objects", 38, testScene.objectIdMapFinal.size());
            
            // get matrix of 5th tri
            Sphere lastSphere = (Sphere) testScene.objectIdMapFinal.get(14);
            float radius4 = lastSphere.radius;
            assertEquals("radius of last sphere is 1", 1, (int) lastSphere.radius);
            

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene2ShootRay(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\test_files\\scene2.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene2;
        File fileToInsert = file.get();
        try {
            testScene2 = new Scene(fileToInsert);
            // get objects
            Map<Integer,Primitive> objectList = testScene2.objectIdMapFinal;
            // get camera
            Camera scene2Cam = testScene2.sceneCam;
            // generate ray in middle left
            int i = 0;
            int j = 0;
            Ray upCornerCamRay = scene2Cam.generateCamRay(i, j, 640, 480);
            // check hit
            //out.println(midLftCamRay.getClosestObject(objectList).keySet());

            // get hit pt
            FixedVector hitPt = upCornerCamRay.getClosestObject(objectList).get(10);
            // get triangle:
            Triangle tri10 = (Triangle) objectList.get(10);
            // ray should not hit 
            assertFalse("should not hit!", tri10.rayHits(upCornerCamRay));
                    
            
            // object is id 2, get object
            Primitive obj2 = objectList.get(2);
            assertTrue("ambient is 1/2", obj2.getAmbient().get(0) - 0.5 < GlobalConstants.acceptableError);
            // get hit pt
            //FixedVector hitPtObj2 = midLftCamRay.getClosestObject(objectList).get(2);           
            // enter into get color
            //Color testColor = midLftCamRay.getRayColorFrom(obj2, hitPtObj2, testScene2);
            //out.println(testColor);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1ShootRay(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\test_files\\scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene2;
        File fileToInsert = file.get();
        try {
            testScene2 = new Scene(fileToInsert);
            // get objects
            Map<Integer,Primitive> objectList = testScene2.objectIdMapFinal;
            // get camera
            Camera scene1Cam = testScene2.sceneCam;
            
            // generate ray in middle
            //Ray midCamRay = scene1Cam.generateCamRay(240, 320, 640, 480);
            //FixedVector directnTest = midCamRay.getDirectionVector();
            // out.println(directnTest.x());

            // get ray at top middle
            Ray midTopCamRay = scene1Cam.generateCamRay(0, 320, 640, 480);
            FixedVector directnTopTest = midTopCamRay.getDirectionVector();
            //out.println(directnTopTest.toString());

            FixedVector camToCenterFromFrame = scene1Cam.getCamFrame().get(0).multConst(-1);
            FixedVector actualCamToCenter = new FixedVector(5,4,-4);
            
            float dotFromFram = directnTopTest.dot(camToCenterFromFrame);
            float actualDot = directnTopTest.dot(actualCamToCenter)/actualCamToCenter.length();
            
            assertTrue("dots should be same", Math.abs(actualDot-dotFromFram)<GlobalConstants.acceptableError);            
            double thetaY = Math.toRadians(scene1Cam.fovy)/2.0;                        
            assertTrue("fovy should be 30", Math.abs(actualDot-Math.cos(thetaY))<0.01);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1ShootRayToFarCorner(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\test_files\\scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene1;
        File fileToInsert = file.get();
        try {
            testScene1 = new Scene(fileToInsert);
            // get objects
            Map<Integer,Primitive> objectList = testScene1.objectIdMapFinal;
            // get camera
            Camera scene1Cam = testScene1.sceneCam;        
            FixedVector w = scene1Cam.getCamFrame().get(0);
            FixedVector u = scene1Cam.getCamFrame().get(1);
            FixedVector v = scene1Cam.getCamFrame().get(2);
            //out.println(w.toString());
                       
            // get ray from corner to tri2 v3
            Triangle tri2 = (Triangle) objectList.get(2);
            FixedVector smallEp = new FixedVector(0.001f,-0.001f, 0);
            FixedVector cornerTri2 = tri2.v3.addFixed(smallEp);
            FixedVector directionToCorner = cornerTri2.subtractFixed(scene1Cam.camEye);           
            Ray rayToCorner = new Ray(scene1Cam.camEye, directionToCorner);
            //out.println(directionToCorner.normalize().toString());
            
            // get intersection with viewing plane
            // first create triangle in plane
            FixedVector vert1 = scene1Cam.camEye.addFixed(w.multConst(-1));
            FixedVector vert2 = scene1Cam.camEye.addFixed(w.multConst(-1)).addFixed(u);
            FixedVector vert3 = scene1Cam.camEye.addFixed(w.multConst(-1)).addFixed(v);
            
            Triangle triInViewPlane = new Triangle(vert1,vert2,vert3,new FixedMatrix4());
            FixedVector interPtRayViewPlane = triInViewPlane.rayPlaneIntersection(rayToCorner);
            // to solve
            FixedVector solnVec = interPtRayViewPlane.subtractFixed(scene1Cam.camEye).addFixed(w);
            
            // get ray at top corner (should hit tri1 or 2
            FixedVector smallEp2 = new FixedVector(0.00001f,-0.00001f, 0);
            Ray midTopCamRay = scene1Cam.generateCamRay(18, 154, 640, 480);
            FixedVector directnTopTest = midTopCamRay.getDirectionVector();
            //out.println(directnTopTest.toString());
 
            
            // get ray intersection, try color
            
            Map<Integer, FixedVector> objIdWithHit = midTopCamRay.getClosestObject(objectList);
            //out.println(objIdWithHit.keySet());
            
            Map<Integer, FixedVector> objIdWithHitFromGenerated = rayToCorner.getClosestObject(objectList);
            //out.println(objIdWithHitFromGenerated.keySet());
            FixedVector hitPt = objIdWithHitFromGenerated.get(2);
            Primitive objHit = objectList.get(2);
            
            // since ray hits, try get color of ray
            FixedVector cornerColor = midTopCamRay.getRayColorFrom(objHit, hitPt, testScene1);
            //out.println(cornerColor);

            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene3GetMatrices(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\test_files\\scene3.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene3;
        File fileToInsert = file.get();
        try {
            testScene3 = new Scene(fileToInsert);
            // get objects
            Map<Integer,Primitive> objectList = testScene3.objectIdMapFinal;
            // matrix for first tris are same, just scale
            Triangle tri1 = (Triangle) objectList.get(1);
            // 13 has scale and translate
            Triangle tri13 = (Triangle) objectList.get(13);
            FixedMatrix4 tri13Mat = tri13.getTransformMatrix();
            out.println(tri13Mat);

            // create ray and transform
            Camera scene3Cam = testScene3.sceneCam; 
            Ray midCamRay = scene3Cam.generateCamRay(100, 320, 640, 480);
            midCamRay.transformRayToPrimitive(midCamRay, tri13Mat);
            out.println(tri13Mat);
            out.println(tri13Mat.m32());
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    
    @Test
    public void testScene6Alt(){
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\test_files\\scene6-alt.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        Scene testScene6Alt;
        File fileToInsert = file.get();
        try {
            testScene6Alt = new Scene(fileToInsert);
            Camera scene6Cam = testScene6Alt.sceneCam;
            // get objects
            Map<Integer,Primitive> objectList = testScene6Alt.objectIdMapFinal;
            Primitive redSide = objectList.get(1);
            Primitive greenSide = objectList.get(3);
            
            FixedMatrix4 redMatrix = redSide.getTransformMatrix();
            FixedMatrix4 greenMatrix = greenSide.getTransformMatrix();
            //out.println(redMatrix.toString());
            //out.println(greenMatrix.toString());
            
            // transform centers and see where they lie
            FixedVector origCenter = new FixedVector(0,0,-3);
            
            FixedVector transRedCenter = Geometry.mat4MultPosVec3(origCenter, redMatrix);
            FixedVector transGreenCenter = Geometry.mat4MultPosVec3(origCenter, greenMatrix);
            //out.println(transRedCenter.toString());
            //out.println(transGreenCenter.toString());
            // create rays from cam to (+- 2.6, 0 , -4.5) then test color
            FixedVector redDir = transRedCenter.subtractFixed(scene6Cam.camEye);
            FixedVector greenDir = transGreenCenter.subtractFixed(scene6Cam.camEye);
            Ray redRay = new Ray(scene6Cam.camEye,redDir);
            Ray greenRay = new Ray(scene6Cam.camEye,greenDir);
            
            // make sure redRay hits object
            assertTrue("redRay hits", redRay.getClosestDistanceToAnyObjectAmong(objectList)>0 );
            Map<Integer, FixedVector> redIdHitMap = redRay.getClosestObject(objectList);
            int redId = redIdHitMap.keySet().iterator().next();
            //assertFalse("red ray should not hit green", greenSide.rayHits(redRay));
            
            assertEquals("red Ray hits redSide object", 1, redId);
            
            out.println(redRay.getRayColorFrom(redSide, transRedCenter, testScene6Alt) );

            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
