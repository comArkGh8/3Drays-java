package rays;

import org.joml.*;

public class FixedVector {
    
    final float x;
    final float y;
    final float z;
    
    // sets vector; note that the coordinates are set to final
    // creates an immutable vector
    public FixedVector(float xIn, float yIn, float zIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
      }
    
    // creates fixedVector from a vector3f
    public FixedVector(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
      }
    
    // creates fixedVector from a vector3fc
    public FixedVector(Vector3fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
      }
    
    // simple operations
    public FixedVector addFixed(FixedVector v) {
        return new FixedVector(x + v.x, y + v.y, z + v.z);
    }

    public FixedVector subtractFixed(FixedVector v) {
        return new FixedVector(x -  v.x, y - v.y, z - v.z);
    }
    
    
    public FixedVector addVector(Vector3d v) {
        return new FixedVector(x + (float) v.x, y + (float) v.y, z + (float) v.z);
    }

    public FixedVector subtractVector(Vector3d v) {
        return new FixedVector(x - (float) v.x, y - (float) v.y, z - (float) v.z);
    }
    
    // takes cross product of fixed vector with fixed vector
    public FixedVector cross(FixedVector fixedV) {
        // first create Vector3f for cross product
        Vector3f givenVector = new Vector3f(this.x, this.y, this.z);
        Vector3f inputVector = new Vector3f(fixedV.x, fixedV.y, fixedV.z);
        
        Vector3f crossResult = new Vector3f();
        // get cross product from joml
        givenVector.cross(inputVector, crossResult);
        
        // now create fixedVector from result     
        FixedVector fixedCross = new FixedVector(crossResult);
        
        return fixedCross;
    }

    // normalizes fixed vector
    public FixedVector normalize() {
        // first create Vector3f for normalization
        Vector3f givenVector = new Vector3f(this.x, this.y, this.z);
        
        Vector3f normalizedIntermediate = givenVector.normalize();
        
        // now create fixedVector from result     
        FixedVector normedRes = new FixedVector(normalizedIntermediate);
        
        return normedRes;
    }
    

}

