package test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import rays.Camera;
import rays.FixedMatrix4;
import rays.FixedVector;
import rays.Geometry;
import rays.GlobalConstants;
import rays.Light;
import rays.Primitive;
import rays.Ray;
import rays.Scene;
import rays.Triangle;

public class HalfVecCalcTests {

    
    
    @Test
    public void testScene4HalfVec() {
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene4-diffuse.test";
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
            
            // get ray from cam to (1,-1,1)
            FixedVector testPoint = new FixedVector(0.6f, -1, 0.25f);
            FixedVector start = scene4Cam.camEye;
            FixedVector dir = testPoint.subtractFixed(start);
            
            Ray camToTestPt = new Ray(start, dir);

            // get ray intersection, try color          
            Map<Integer, FixedVector> objIdWithHit = camToTestPt.getClosestObject(objectList);
            // double check hitPt
            FixedVector hitPt = objIdWithHit.get(11);
            //out.println(hitPt.toString());
            
            // now ray from light pt:
            // get lights (just one)
            Map<Integer,Light> lightList = testScene4.lightIdMapFinal;
            FixedVector startLight = lightList.get(1).getPosition();
            FixedVector dirLight = testPoint.subtractFixed(startLight);
            
            Ray lightToTestPt = new Ray(startLight, dirLight);

            // get ray intersection, try color          
            Map<Integer, FixedVector> objIdWithLightHit = lightToTestPt.getClosestObject(objectList);
            // read id: 11
            // out.println(objIdWithLightHit.keySet());
            // double check hitPt
            FixedVector hitLightPt = objIdWithLightHit.get(11);
            //out.println(hitLightPt.toString());
            
            Primitive objectHit = objectList.get(11);
            // normal of object
            FixedVector normal = objectHit.getNormalAt(hitPt);
            
            // half vector calcs as in ray.getcolor...:
            FixedVector reflectDirection = 
                    Geometry.reflectDirectionVector(lightToTestPt.getDirectionVector(), normal);
            
            // test reflect direction:
            // out.println(lightToTestPt.getDirectionVector());
            // out.println(normal);
             out.println(reflectDirection.toString());
             
             out.println(dir.normalize());

            FixedVector halfDirectnRaw = reflectDirection.subtractFixed(dir.normalize());
            FixedVector halfDirectn = halfDirectnRaw.normalize();
            out.println(halfDirectnRaw.toString());
            out.println(halfDirectn.toString());
            

            //out.println(dir.multConst(-1).normalize().toString());

            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
