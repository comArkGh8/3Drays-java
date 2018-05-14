

package rays;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import static java.lang.System.out; 

public class RenderImage {
    
    private static Color rayTracer(Ray inRay, int missingObjectId, Scene theScene, int depth) {
        
        if (depth >= GlobalConstants.maxDepth) {
            Color blackColor = new Color(0);
            return blackColor;
        }
        
        // get the full object list
        Map<Integer, Primitive> fullObjectList = theScene.objectIdMapFinal;
        
        // does ray hit...if not return black
        boolean hitExists;
        // make list of objects to be considered (full list minus hit object)
        Map<Integer, Primitive> validObjects = new HashMap();
        for (int aKey: fullObjectList.keySet()) {
            if (aKey != missingObjectId) {
                validObjects.put(aKey, fullObjectList.get(aKey));
            }
        }
        
        Map<Integer, Primitive> validObjectsFinal = Collections.unmodifiableMap(validObjects);
    
        float closestHitDist = inRay.getClosestDistanceToAnyObjectAmong(validObjectsFinal);
        hitExists = closestHitDist> -0.1f;
        if (hitExists) {
            // get object id, hitPt
            Map<Integer, FixedVector> objIdHitPt = inRay.getClosestObject(validObjectsFinal);
            int objHitId = objIdHitPt.keySet().iterator().next();
            FixedVector hitPt = objIdHitPt.get(objHitId);
            Primitive objHit = fullObjectList.get(objHitId);
            // get color from ray
            // for recursion:
            // Color colorFromRay = rayTracer(reflectRay, objHitId, theScene, depth +1);
            Color colorFromRay = inRay.getRayColorFrom(objHit, hitPt, theScene);
            return colorFromRay;
            
            
        }
        else {
            Color blackColor = new Color(0);
            return blackColor;
        }
        
    }
    
    

    public static void draw(Scene aScene, File outFile) throws IOException, InterruptedException {
        int initDepth = 0;   
        int noObjId = 0;
        Camera theSceneCamera = aScene.sceneCam;
        int sceneW = aScene.width;
        int sceneH = aScene.height;
        final BufferedImage image = new BufferedImage(sceneW, sceneH, BufferedImage.TYPE_INT_RGB);

        try {
            for (int i = 0; i<sceneH; i++) {
                for (int j = 0; j<sceneW; j++) {
                    // generate camera ray
                    Ray initCamRay = theSceneCamera.generateCamRay(i, j, sceneW, sceneH);
                    
                    // now rayTracer
                    Color colorTraced = rayTracer(initCamRay,noObjId,aScene,initDepth);
                    
                    // set color at i,j index with java
                    image.setRGB(j, i, colorTraced.getRGB());
                    
                }
            }

            // retrieve image
            ImageIO.write(image, "png", outFile);
        }
        catch (IOException e) {
        }
        
      
        
    }

}
