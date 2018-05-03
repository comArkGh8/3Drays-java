package rays;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.joml.*;
import org.joml.Math;


public class Scene {
    
    int width;
    int height;

    Camera sceneCam;
    
    // initialize ambient diffuse, etc.
    List<Float> ambient;
    List<Float> diffuse;
    List<Float> specular;
    List<Float> emission;
    float shininess;
    
    List<Float> atten;

    // make map of primitiveObjects id->Object
    public Map<Integer,Primitive> objectIdMap;
    
    // make map of lights id->Light
    public Map<Integer,Light> lightIdMap;
    
    String outputName;
    int maxDepth;
    
    
    
     // Function to read the input data values
     // Use is optional, but should be very helpful in parsing.
     private boolean readvals(String[] sLine, int numVals){
         if (sLine.length != numVals) {
             out.println("Failed reading a value");
             return false;          
         }
       return true;
     }
     
    
    public Scene(File file) throws IOException{

        // set inits
        // initialize ambient diffuse, etc.
        ambient= Arrays.asList((float) 0.2, (float) 0.2, (float) 0.2);
        diffuse= Arrays.asList((float) 0, (float) 0, (float) 0);
        specular= Arrays.asList((float) 0, (float) 0, (float) 0);
        emission= Arrays.asList((float) 0, (float) 0, (float) 0);
        shininess = 0;
        
        atten = Arrays.asList((float) 1.0, (float) 0, (float) 0);
        
        maxDepth = 5;

        
        String nameOfFile = file.getName();
        String path = file.getAbsolutePath();
        File fileToRead = new File(path);  
        
        
        // start up the objectsHash (maps identifier to object) and lightMap
        // later build immutable Map of Entries
        objectIdMap = new HashMap();
        lightIdMap = new HashMap();
        int numObjects = 0;
        int numLights = 0;
        // can check if numObjects = length of IdMap!
        
        // get list ready to add vertices
        List<Vector3fc> verticesList = new ArrayList<Vector3fc>();
        
        // start the stack with the identity
        Matrix4fStack matTransStack = new Matrix4fStack(2);
        matTransStack.clear();

        // read in scene setup from file
        // use try with resources for buffered reader
        try(BufferedReader bufRead = new BufferedReader(new FileReader(fileToRead))){

            int rowTracker =0;
            String myLine = null;
            String[] cmdVals;
            // sizeX= Integer.parseInt(dimensionsString[0]);
             
            
            // now read the lines
            while ((myLine = bufRead.readLine().trim()) !=null){
                
                if( !(myLine.startsWith("#")) ) {
                    // Ruled out comment and blank lines
                    
                    boolean validinput;
                
                    // process the line
                    cmdVals = myLine.split(" ");
                    // check which command
                    String cmd = cmdVals[0];
                    
                    switch (cmd) {
                    // sets size
                    case "size": 
                        validinput = readvals(cmdVals,3);
                        if (validinput) {
                            width = Integer.parseInt(cmdVals[1]); 
                            height = Integer.parseInt(cmdVals[2]);
                        }
                        
                    // sets number of bounces (if not default 5);
                    case "maxdepth":
                        validinput = readvals(cmdVals,2);
                        if (validinput) {
                            maxDepth = Integer.parseInt(cmdVals[1]);
                        }
                    
                    case "output":
                        validinput = readvals(cmdVals,2);
                        if (validinput) {
                            outputName =  cmdVals[1];
                        }
                   
                    case "camera":
                        validinput = readvals(cmdVals,11); // 10 values eye cen up fov
                        if (validinput) {
                            // eyeinit are first 3 values,
                            // center are next 3

                            float eyeX = Float.parseFloat(cmdVals[1]);
                            float eyeY = Float.parseFloat(cmdVals[2]);
                            float eyeZ = Float.parseFloat(cmdVals[3]);

                            Vector3f eyeInit = new Vector3f(eyeX, eyeY, eyeZ);
                            
                            float origupX = Float.parseFloat(cmdVals[7]);
                            float origupY = Float.parseFloat(cmdVals[8]);
                            float origupZ = Float.parseFloat(cmdVals[9]);
                            
                            Vector3f origup = new Vector3f(origupX, origupY, origupZ);
                            
                            float centerX = Float.parseFloat(cmdVals[4]);
                            float centerY = Float.parseFloat(cmdVals[5]);
                            float centerZ = Float.parseFloat(cmdVals[6]);
                            
                            Vector3f center = new Vector3f(centerX, centerY, centerZ);
                            
                            // set fovy to values[10];
                            float fovy = Float.parseFloat(cmdVals[10]);
                            
                            // make the scene camera
                            sceneCam = new Camera(eyeInit, center, origup, fovy);
                        }
                        
                    case "ambient":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                ambient.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }
                    case "diffuse":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                diffuse.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }
                    case "specular":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                specular.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }  
                    case "emission":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                emission.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }                        
                    case "shininess":
                        validinput = readvals(cmdVals, 2);
                        if (validinput) {
                            shininess = Float.parseFloat(cmdVals[1]);
                        }
                    
                     // tri v1,v2,v3 sets triangle with verts, counter-clockwise order
                    case "maxverts":
                        // ignore
                        
                    case "vertex":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            float readVectorX = Float.parseFloat(cmdVals[1]);
                            float readVectorY = Float.parseFloat(cmdVals[2]);
                            float readVectorZ = Float.parseFloat(cmdVals[3]);
                            
                            // take a vertex as a vector
                            Vector3fc readVector=  new Vector3f(readVectorX, readVectorY, readVectorX);
                            // enter into list
                            verticesList.add(readVector);
                        }  
                    
                    case "tri":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            // create a triangle with own function
                            // use values to get each vertex
                            int indexFirstVertex = Integer.parseInt(cmdVals[1]);
                            int indexSecondVertex = Integer.parseInt(cmdVals[2]);
                            int indexThirdVertex = Integer.parseInt(cmdVals[3]);
                            FixedVector v1= new FixedVector(verticesList.get(indexFirstVertex));
                            FixedVector v2= new FixedVector(verticesList.get(indexSecondVertex));
                            FixedVector v3= new FixedVector(verticesList.get(indexThirdVertex));

                            // now get transform
                            Matrix4f triMatrix = new Matrix4f();
                            matTransStack.get(triMatrix);
  
                            // now create triangle and enter into objectMap
                            numObjects++;
                            Triangle triToAdd = new Triangle(v1, v2, v3, 
                                    ambient, diffuse, specular, emission, shininess, triMatrix);
                                    
                            objectIdMap.put(numObjects, triToAdd);
                        }                        
                    
                    case "sphere":
                        validinput = readvals(cmdVals, 5);
                        if (validinput) {
                            // create a triangle with own function
                            // use values to get each vertex
                            float centerX = Float.parseFloat(cmdVals[1]);
                            float centerY = Float.parseFloat(cmdVals[2]);
                            float centerZ = Float.parseFloat(cmdVals[3]);
                            FixedVector center= new FixedVector(centerX, centerY, centerZ);
                            
                            float radius = Float.parseFloat(cmdVals[4]);


                            // now get transform
                            Matrix4f sphMatrix = new Matrix4f();
                            matTransStack.get(sphMatrix);
  
                            // now create sph and enter into objectMap
                            numObjects++;
                            Sphere sphToAdd = new Sphere(center, radius, 
                                    ambient, diffuse, specular, emission, shininess, sphMatrix);
                                    
                            objectIdMap.put(numObjects, sphToAdd);
                        }    
                        
                    case "translate":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            // takes transfstack and multiplies on right by input matrix
                            float transX = Float.parseFloat(cmdVals[1]);
                            float transY = Float.parseFloat(cmdVals[2]);
                            float transZ = Float.parseFloat(cmdVals[3]);
                            // translate at the top
                            matTransStack.translate(transX, transY, transZ);
                        }  
                        
                    case "scale":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            // takes transfstack and multiplies on right by input matrix
                            float scaleX = Float.parseFloat(cmdVals[1]);
                            float scaleY = Float.parseFloat(cmdVals[2]);
                            float scaleZ = Float.parseFloat(cmdVals[3]);
                            matTransStack.scale(scaleX, scaleY, scaleZ);
                        }    
                      
                    case "rotate":
                        validinput = readvals(cmdVals, 5);
                        if (validinput) {
                            // values[1..3] are the axis, values[4] is the angle.
                            float rotAxisX = Float.parseFloat(cmdVals[1]);
                            float rotAxisY = Float.parseFloat(cmdVals[2]);
                            float rotAxisZ = Float.parseFloat(cmdVals[3]);
                            float rotAngle = Float.parseFloat(cmdVals[4]);
                            
                            matTransStack.rotate((float) Math.toRadians(rotAngle), rotAxisX, rotAxisY, rotAxisZ);
                        }      
                        
                        // basic push/pop code for matrix stacks
                    case "pushTransform":
                        matTransStack.pushMatrix(); 
                    case "popTransform":
                        matTransStack.popMatrix(); 
                        
                    case "directional":
                        validinput = readvals(cmdVals, 7);
                        if (validinput) {
                            // values[1..3] are the (into)direction, others are color.
                            float dirX = Float.parseFloat(cmdVals[1]);
                            float dirY = Float.parseFloat(cmdVals[2]);
                            float dirZ = Float.parseFloat(cmdVals[3]);
                            FixedVector directionVec= new FixedVector(dirX, dirY, dirZ);
                            
                            float colorR = Float.parseFloat(cmdVals[4]);
                            float colorG = Float.parseFloat(cmdVals[5]);
                            float colorB = Float.parseFloat(cmdVals[6]);

                            // IM HERE!!! what to do about color ????!!!!!!!!1

                        }                       
                        
/**
 * 


                // now do lights
                // Make use of "light"Posn[] and "light"Color[] arrays in variables.h
                else if (cmd == "directional") {
                    validinput = readvals(s, 6, values);
                    if (validinput) {
                        for(i=0; i<3; i++){
                            dirPosn.push_back(values[i]);
                        }

                        for(i=0; i<3; i++){
                            dirColor.push_back(values[i+3]);
                        }
                        numDirLights++;
                    }
                }
                else if (cmd == "point") {
                    validinput = readvals(s, 6, values);
                    if (validinput) {
                        for(i=0; i<3; i++){
                            pointPosn.push_back(values[i]);
                        }

                        for(i=0; i<3; i++){
                            pointColor.push_back(values[i+3]);
                        }
                        numPtLights++;
                    }
                }
                else if (cmd == "attenuation") {
                    validinput = readvals(s, 3, values);
                    if (validinput) {
                        for (i = 0; i < 3; i++) {
                            atten[i] = values[i];
                        }
                    }
                }
 * 
 * 
 * 
 * 
 */

                    
                    }
                    
                    
                    
                    
                    


                    }
                }
                
                for (int columnIndex = 0; columnIndex < sizeX; columnIndex++){    
                    this.squaresMap.putIfAbsent(sizeX*rowTracker+columnIndex, 
                            new Square(valueOfSquareString[columnIndex]));
                }
                rowTracker++;
            }
        }
        
        // now start up the locks
        // divideGroups gives a mapping to groups
        Map <Integer,Set<Integer>> lockToGroupMap = divideGroups();
        this.lockNumberForGroup = lockToGroupMap;
        
        // now for each value in the numbering (of Groups)
        // associate one lock
        Map<Integer,ReentrantLock> mapForLocks= new HashMap<Integer, ReentrantLock>();
        for (int key:lockToGroupMap.keySet()) {
            mapForLocks.put(key, new ReentrantLock());
        }
        this.locks=mapForLocks;
    }  

}
