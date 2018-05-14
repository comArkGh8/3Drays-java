package rays;

import java.util.Collections;
import java.util.List;

import org.joml.*;


// NOTE USE  Collections.unmodifiableList(aList); to create immutable list

public abstract class Primitive {
    
    public enum Shape{
        TRIANGLE, SPHERE
    }
    
    // contains coloring properties, these should be final
    protected FixedVector ambient;
    protected FixedVector diffuse;
    protected FixedVector specular;
    protected FixedVector emission;
    protected float shininess;
    
    public int id;
    
    private Matrix4f objectMatrix;
  
    protected void setTransformMatrix(Matrix4f matrixIn) {
        this.objectMatrix = matrixIn;
    }
    
    public FixedVector getAmbient() {
        return this.ambient;
    }
    
    public Matrix4f getTransformMatrix() {
        return this.objectMatrix;
    }
    
    public abstract int getId();
    
    public abstract Shape getShape();
    
    public abstract FixedVector getNormalAt(FixedVector point);
    
    public abstract boolean rayHits(Ray aRay);
    
    public abstract FixedVector getHitPoint(Ray aRay);
}

