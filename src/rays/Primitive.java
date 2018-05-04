package rays;

import java.util.Collections;
import java.util.List;

import org.joml.*;


// NOTE USE  Collections.unmodifiableList(aList); to create immutable list

public abstract class Primitive {
    
    public enum Shape{
        TRIANGLE, SPHERE
    }
    
    private Matrix4f objectMatrix;
  
    protected void setTransformMatrix(Matrix4f matrixIn) {
        this.objectMatrix = matrixIn;
    }
    
    protected Matrix4f getTransformMatrix(Matrix4f matrixIn) {
        return this.objectMatrix;
    }
    
    //protected abstract FixedVector getNormalAt(FixedVector point);
    
    // TO ADD
    // public abstract boolean rayHits(Ray aRay);
}

