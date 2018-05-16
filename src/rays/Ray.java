


package rays;

import static java.lang.System.out;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.lang.Math;
import org.joml.*;

import rays.Light.Type;

public class Ray {
    
    private FixedVector start;
    private FixedVector direction;
    
    public Ray(Vector3f startVector, Vector3f directionVector) {
        
        this.start = new FixedVector(startVector);
        // normalize direction vector!
        Vector3fc normalDir = directionVector.normalize();
        this.direction = new FixedVector(normalDir);

    }
    
    public Ray(FixedVector startVector, FixedVector directionVector) {
        
        this.start = startVector;          
        // normalize direction vector!
        FixedVector normalDir = directionVector.normalize();
        this.direction = normalDir;

    }
    
    public FixedVector getStartVector() {
        return this.start;
    }
    
    public FixedVector getDirectionVector() {
        return this.direction;
    }
    
    
    /**
     * object is represented as primitive 
     * @param ray - ray in world (actual) coordinates
     * @param objMat - input the objects matrix
     * @return - the ray as if the object was not transformed to actual coordinates
     */
    public static Ray transformRayToPrimitive(Ray ray, FixedMatrix4 objMat) {
        
        // first get inverse matrix
        FixedMatrix4 objInv= objMat.invert();
        
        // transform start
        Vector3fc inputStartVector = new Vector3f(ray.start.x(), ray.start.y(), ray.start.z());
        FixedVector primitiveFixedStart = Geometry.mat4MultPosVec3(inputStartVector, objInv); 
          
        // transform direction
        FixedVector inputDirectionVector = new FixedVector(ray.direction.x(), ray.direction.y(), ray.direction.z());
        FixedVector primitiveFixedDir = Geometry.mat4MultDirVec3(inputDirectionVector, objInv); 
        
        // create Ray from start and direction
        Ray primitiveReturnRay = new Ray(primitiveFixedStart, primitiveFixedDir);
        
        return primitiveReturnRay;
    }
   
    /**
    *  returns ray which is reflected at given point according to normal provided
    *  
    * @param normalVector - normal vector of surface across which reflection occurs
    * @param reflectPt - pt at which reflection occurs
    * @return - the ray with the reflected direction (use Geometry method, which returns normalized direction)
    */  
    public Ray getReflectionAcross(FixedVector reflectPt, FixedVector normalVector) {
        // get direction for reflected 
        FixedVector inRayDir = this.direction;
        
        FixedVector reflectRayDir = Geometry.reflectDirectionVector(inRayDir, normalVector);
        
        // make new Ray and return
        Ray reflectRay = new Ray(reflectPt, reflectRayDir);

        return reflectRay;
    }
    
    /**
     * 
     * @param hitPt - where ray hits
     * @param normal - normal of object where ray hits
     * @param aLight - light being tested
     * @return true if ray hits the lighted side, false otherwise
     */
    public boolean rayCheckSide(FixedVector hitPt, FixedVector normal, Light aLight) {
        float rayDirDotN = this.getDirectionVector().dot(normal);
        float lightDotN;
        float signRay;
        float signLight=0;
        
        if (Math.abs(rayDirDotN)>GlobalConstants.acceptableError) {
            signRay = Math.abs(rayDirDotN)/rayDirDotN;
        }
        else {
            return false;
        }
        if (aLight.getType()==Type.POINT) {
            FixedVector hitPtToLight = aLight.getPosition().subtractFixed(hitPt);
            FixedVector hitPtFromLight = hitPtToLight.multConst(-1);
            lightDotN = hitPtFromLight.dot(normal);
            if (Math.abs(lightDotN)>GlobalConstants.acceptableError) {
                signLight = Math.abs(lightDotN)/lightDotN;
            }
            else {
                return true;
            }
        }
        else if (aLight.getType()==Type.DIRECTIONAL) {
            FixedVector hitPtFromLight = aLight.getDirectionTo();
            lightDotN = hitPtFromLight.dot(normal);
            if (Math.abs(lightDotN)>GlobalConstants.acceptableError) {
                signLight = Math.abs(lightDotN)/lightDotN;
            }
            else {
                return true;
            }         
        }
        
        if ( Math.abs(signRay+signLight)<GlobalConstants.acceptableError ) {
            return false;
        }
        else {
            return true;
        }
        
    }
    
    /**
     * assume ray hits object 
     * @param objHit
     * @param hitPt
     * @param theScene
     * @return - the color obtained from the object and lights
     */
    
    public Color getRayColorFrom(Primitive objHit, FixedVector hitPt, Scene theScene) {
        
        FixedVector colorSum = new FixedVector(0,0,0);
        // first color from object itself
        // get ambient and emission lists 
        FixedVector ambientInput = objHit.ambient;
        FixedVector emissionInput = objHit.emission;
        FixedVector colorFromObj = Colors.produceColor(ambientInput, emissionInput);
                
        // make new list minus objHit
        Map<Integer, Primitive> allObjects = theScene.objectIdMapFinal;
        int idExclude = objHit.getId();
        Map<Integer, Primitive> validObjects = new HashMap();
        for (int aKey: allObjects.keySet()) {
            if (aKey != idExclude) {
                validObjects.put(aKey, allObjects.get(aKey));
            }
        }
        
        Map<Integer, Primitive> validObjectsFinal = Collections.unmodifiableMap(validObjects);
        
        // get normal of object at hit point, and its properties
        FixedVector normal = objHit.getNormalAt(hitPt);
        FixedVector mydiffuse = objHit.diffuse;
        FixedVector myspecular = objHit.specular;
        float myshininess = objHit.shininess;
        
        // for each light....if it reaches object && if ray is on correct side
        Map<Integer, Light> allLights = theScene.lightIdMapFinal;
        for (Light aLight : allLights.values()) {    
            // check if ray on correct side
            boolean rayOnCorrectSide = this.rayCheckSide(hitPt, normal, aLight);
            
            if ((aLight.reaches(hitPt, validObjectsFinal) )&& rayOnCorrectSide ){
                // compute light color
                FixedVector lightcolor = aLight.getColor(); 
                FixedVector halfDirectn;
                FixedVector lightDirectionTo = null;
                
                if (aLight.getType()==Type.POINT) {
                    FixedVector ptPosn = aLight.getPosition();
                    lightDirectionTo = hitPt.subtractFixed(ptPosn).normalize();  
                }
                else if (aLight.getType()==Type.DIRECTIONAL) {
                    lightDirectionTo  = aLight.getDirectionTo();
                }
                
                FixedVector reflectDirection = 
                        Geometry.reflectDirectionVector(lightDirectionTo, normal);

                FixedVector halfDirectnRaw = reflectDirection.subtractFixed(this.direction);
                halfDirectn = halfDirectnRaw.normalize();
                
                // add this to color
                FixedVector colorCurrentLight = Light.computeLight(lightDirectionTo, lightcolor, normal, halfDirectn, mydiffuse, myspecular, myshininess);
                FixedVector tempColor = colorSum.addFixed(colorCurrentLight);

                
                if (colorCurrentLight.x()> 1) {
                    out.println(lightcolor.toString());
                }
                

                
                colorSum = new FixedVector(tempColor.x(),tempColor.y(),tempColor.z());
            }
        }
                
        FixedVector totalColorVec = colorSum.addFixed(colorFromObj);
        
        // now produce color from vector
        Color totalColor = new Color(totalColorVec.x(), totalColorVec.y(), totalColorVec.z());
        return totalColor;
    }
    
    /**
     * 
     * @param objectIdMap - enters in the map of objects id->Object
     *      the map doesnt necessarily contain all objects, just those under
     *      consideration
     * @return - the id, hitPoint
     */
    public Map<Integer,FixedVector> getClosestObject (Map<Integer,Primitive> objectIdMap){

        float smallDist = 0; // init smallest distance measure
        int currentId = 0;
        FixedVector hitPtVec = null;
        
        // go through all objects
        // use for each KEY!!! as some (one) objects may be missing from list!
        Set<Integer> idSet= objectIdMap.keySet();
        for (int objCount : idSet){
            
            Primitive currentObj = objectIdMap.get(objCount); 
            if (currentObj.rayHits(this) ){
                // get distance of start of ray to hit point
                FixedVector intersectionPt = currentObj.getHitPoint(this);
                FixedVector distSegment = intersectionPt.subtractFixed(this.start);            
                float currentDist = distSegment.length();

                if (smallDist == 0){
                    smallDist = currentDist;
                    // record id
                    currentId = objCount;
                    // record pt
                    hitPtVec = currentObj.getHitPoint(this);
                }
                else if (smallDist < currentDist){
                    // do nothing; already have first hit
                }
                else{
                    smallDist = currentDist;
                    currentId = objCount;
                    hitPtVec = currentObj.getHitPoint(this);
                }
            }
        }
        
        Map<Integer,FixedVector> idHitPtMapFinal;
        Map<Integer,FixedVector> idHitMap = new HashMap();
        
        idHitMap.put(currentId, hitPtVec);
        idHitPtMapFinal = Collections.unmodifiableMap(idHitMap);
        
        return idHitPtMapFinal;
    }
    
    /**
     * assumes object input is hit by ray
     * @param objectHit - the object intersected by ray
     * @return - the distance between ray start and hit point
     */
    public float getDistanceTo(Primitive objectHit){

        FixedVector hitPt = objectHit.getHitPoint(this);
        FixedVector startPt = this.getStartVector();
        FixedVector segmentVec = hitPt.subtractFixed(startPt);
        
        float dist = segmentVec.length();
        
        return dist;
    }

    // returns the smallest distance to object (among all objects in scene)
    // returns -1 if no hit
    public float getClosestDistanceToAnyObjectAmong(Map<Integer,Primitive> objectList) {
        float dist=-1;
        
        for (Primitive anObject: objectList.values()) {
            if (anObject.rayHits(this) ){
                float currentDist = this.getDistanceTo(anObject);
                if (dist == -1) {
                    dist = currentDist;
                }
                else if (  (currentDist <= dist) && (currentDist > GlobalConstants.acceptableError) ) {
                    dist = currentDist;
                }
            }
        }
        
        return dist;
    }
    
    
    
    
    

}
