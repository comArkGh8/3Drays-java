package test;

import static java.lang.System.out;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
            assertEquals("x coor. is -4", -4, xPos);
            assertEquals("y coor. is -4", -4, yPos);
            assertEquals("x coor. is 4", 4, zPos);
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
            Matrix4f fifthMat = fifthTri.getTransformMatrix();
            assertEquals("3,4 entry is -2", -2, (int) fifthMat.m32());
            out.println(fifthMat);

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
    
    

}