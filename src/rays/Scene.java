package rays;

import static java.lang.System.out;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joml.*;
import org.joml.Math;


public class Scene {
    
    public int width;
    public int height;

    public Camera sceneCam;
    
    // initialize ambient diffuse, etc.
    private List<Float> ambient;
    private List<Float> diffuse;
    private List<Float> specular;
    private List<Float> emission;
    private float shininess;
    
    public List<Float> atten;

    // make map of primitiveObjects id->Object
    public Map<Integer,Primitive> objectIdMapFinal;
    
    // make map of lights id->Light
    public Map<Integer,Light> lightIdMapFinal;
    
    String outputName;
    public int maxDepth;
    
    
    
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
        
        //maxDepth = 5;
        
        //String nameOfFile = file.getName();
        String path = file.getAbsolutePath();
        File fileToRead = new File(path);  
        
        
        // start up the objectsHash (maps identifier to object) and lightMap
        // later build immutable Map of Entries
        Map<Integer,Primitive> objectIdMap = new HashMap();
        Map<Integer,Light> lightIdMap = new HashMap();
        int numObjects = 0;
        int numLights = 0;
        // can check if numObjects = length of IdMap!
        
        // get list ready to add vertices
        List<Vector3fc> verticesList = new ArrayList<Vector3fc>();
        
        // start the stack with the identity
        Matrix4fStack matTransStack = new Matrix4fStack(3);
        matTransStack.clear();

        // read in scene setup from file
        // use try with resources for buffered reader
        try(BufferedReader bufRead = new BufferedReader(new FileReader(fileToRead))){

            String myLine = null;
            String[] cmdVals;    
            boolean stop = false;
            
            // now read the lines
            while ((myLine = bufRead.readLine()) !=null  && !stop){
                myLine.trim();
                cmdVals = myLine.split("\\s+");
                String cmd = cmdVals[0];
                
                if( !(myLine.startsWith("#")) && (myLine.length()!=0) ) {
                    // Ruled out comment and blank lines

                    
                    boolean validinput;
                
                    // process the line                  
                    switch (cmd) {
                    // sets size
                    case "size": 
                        validinput = readvals(cmdVals,3);
                        if (validinput) {
                            width = Integer.parseInt(cmdVals[1]); 
                            height = Integer.parseInt(cmdVals[2]);
                        }
                        break;
                        
                    // sets number of bounces (if not default 5);
                    case "maxdepth":
                        validinput = readvals(cmdVals,2);
                        if (validinput) {
                            maxDepth = Integer.parseInt(cmdVals[1]);
                        }
                        break;
                    
                    case "output":
                        validinput = readvals(cmdVals,2);
                        if (validinput) {
                            outputName =  cmdVals[1];
                        }
                        break;
                   
                    case "camera":
                        validinput = readvals(cmdVals,11); // 10 values eye cen up fov
                        if (validinput) {
                            // eyeinit are first 3 values,
                            // center are next 3

                            float eyeX = Float.parseFloat(cmdVals[1]);
                            float eyeY = Float.parseFloat(cmdVals[2]);
                            float eyeZ = Float.parseFloat(cmdVals[3]);

                            FixedVector eyeInit = new FixedVector(eyeX, eyeY, eyeZ);
                            
                            float origupX = Float.parseFloat(cmdVals[7]);
                            float origupY = Float.parseFloat(cmdVals[8]);
                            float origupZ = Float.parseFloat(cmdVals[9]);
                            
                            FixedVector origup = new FixedVector(origupX, origupY, origupZ);
                            
                            float centerX = Float.parseFloat(cmdVals[4]);
                            float centerY = Float.parseFloat(cmdVals[5]);
                            float centerZ = Float.parseFloat(cmdVals[6]);
                            
                            FixedVector center = new FixedVector(centerX, centerY, centerZ);
                            
                            // set fovy to values[10];
                            float fovy = Float.parseFloat(cmdVals[10]);
                            
                            // make the scene camera
                            sceneCam = new Camera(eyeInit, center, origup, fovy);
                        }
                        break;
                        
                    case "ambient":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                ambient.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }
                        break;
                        
                    case "diffuse":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                diffuse.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }
                        break;
                        
                    case "specular":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                specular.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        } 
                        break;
                        
                    case "emission":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                emission.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        } 
                        break;
                        
                    case "shininess":
                        validinput = readvals(cmdVals, 2);
                        if (validinput) {
                            shininess = Float.parseFloat(cmdVals[1]);
                        }
                        break;
                    
                     // tri v1,v2,v3 sets triangle with verts, counter-clockwise order
                    case "maxverts":
                        // ignore
                        break;
                        
                    case "vertex":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            float readVectorX = Float.parseFloat(cmdVals[1]);
                            float readVectorY = Float.parseFloat(cmdVals[2]);
                            float readVectorZ = Float.parseFloat(cmdVals[3]);
                            
                            // take a vertex as a vector
                            Vector3fc readVector=  new Vector3f(readVectorX, readVectorY, readVectorZ);
                            // enter into list
                            verticesList.add(readVector);
                        }
                        break;
                    
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
                            FixedMatrix4 fixedTriMatrix = new FixedMatrix4(triMatrix);

                            // now create triangle and enter into objectMap
                            numObjects++;
                            Triangle triToAdd = new Triangle(numObjects, v1, v2, v3, 
                                    ambient, diffuse, specular, emission, shininess, fixedTriMatrix);
                                    
                            objectIdMap.put(numObjects, triToAdd);
                        }
                        break;
                    
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
                            FixedMatrix4 fixedSphMatrix = new FixedMatrix4(sphMatrix);
  
                            // now create sph and enter into objectMap
                            numObjects++;
                            Sphere sphToAdd = new Sphere(numObjects, center, radius, 
                                    ambient, diffuse, specular, emission, shininess, fixedSphMatrix);
                                    
                            objectIdMap.put(numObjects, sphToAdd);
                        }

                        break;
                        
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
                        break;
                        
                    case "scale":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            // takes transfstack and multiplies on right by input matrix
                            float scaleX = Float.parseFloat(cmdVals[1]);
                            float scaleY = Float.parseFloat(cmdVals[2]);
                            float scaleZ = Float.parseFloat(cmdVals[3]);
                            matTransStack.scale(scaleX, scaleY, scaleZ);
                        } 
                        break;
                      
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
                        break;
                        
                        // basic push/pop code for matrix stacks
                    case "pushTransform":
                        matTransStack.pushMatrix(); 
                        break;
                    case "popTransform":
                        matTransStack.popMatrix(); 
                        break;
                        
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
                            
                            // create light and add to list
                            Light readDirLight = new DirectionalLight(directionVec, colorR, colorG, colorB);
                            numLights++;
                            lightIdMap.put(numLights, readDirLight);
                        } 
                        break;
                        
                    case "point":
                        validinput = readvals(cmdVals, 7);
                        if (validinput) {
                            // values[1..3] are the position of the source, others are color.
                            float posX = Float.parseFloat(cmdVals[1]);
                            float posY = Float.parseFloat(cmdVals[2]);
                            float posZ = Float.parseFloat(cmdVals[3]);
                            FixedVector positionVec= new FixedVector(posX, posY, posZ);
                            
                            float colorR = Float.parseFloat(cmdVals[4]);
                            float colorG = Float.parseFloat(cmdVals[5]);
                            float colorB = Float.parseFloat(cmdVals[6]);
                            
                            // create light and add to list
                            Light readPtLight = new PointLight(positionVec, colorR, colorG, colorB);
                            numLights++;
                            lightIdMap.put(numLights, readPtLight);
                        } 
                        break;
                        
                    case "attenuation":
                        validinput = readvals(cmdVals, 4);
                        if (validinput) {
                            for (int i = 0; i < 3; i++) {
                                atten.set( i, Float.parseFloat(cmdVals[i+1]) );
                            }
                        }
                        break;
                    
                        
                    case "stop":
                        stop = true;   
                        break;
                    }


                }
                
            } // end reading file
            // now create immutable map
            this.objectIdMapFinal = 
                    Collections.unmodifiableMap(objectIdMap);
            this.lightIdMapFinal = 
                    Collections.unmodifiableMap(lightIdMap);
            
            
        }

    }  

}
