package rays;

import java.util.Collections;
import java.util.List;

import org.joml.*;

import rays.Primitive.Shape;

public class Triangle extends Primitive {
    
    public final Shape type;
    
    public final FixedVector v1;
    public final FixedVector v2;
    public final FixedVector v3;
    public final FixedVector primitiveNormal;
    
    // contains coloring properties, these should be final
    public final List<Float> ambient;
    public final List<Float> diffuse;
    public final List<Float> specular;
    public final List<Float> emission;
    public final float shininess;
    
            
    // creates a triangle
    public Triangle(FixedVector vert1, FixedVector vert2, FixedVector vert3, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emissionList, float shininess, Matrix4f matrixIn) {
        
        this.type = Shape.TRIANGLE;
        
        this.v1 = vert1;
        this.v2 = vert2;
        this.v3 = vert3;

        this.ambient = Collections.unmodifiableList(ambientList);
        this.diffuse = Collections.unmodifiableList(diffuseList);
        this.specular = Collections.unmodifiableList(specularList);
        this.emission = Collections.unmodifiableList(emissionList);
        this.shininess = shininess;
        
        FixedVector side1 = this.v2.subtractFixed(this.v1);
        FixedVector side2 = this.v3.subtractFixed(this.v1);

        

        
        // normal is cross product
        FixedVector rawPrimitiveNormal = side1.cross(side2);
        
        this.primitiveNormal = rawPrimitiveNormal.normalize();
        
        // set the matrix
        this.setTransformMatrix(matrixIn);


    }

    @Override
    public Shape getShape() {
        return this.type;
    }

    @Override
    public FixedVector getNormalAt(FixedVector point) {
        // normal primitive is already normalized
        FixedVector normalPrimitive = this.primitiveNormal;
        // write normal as 4Vec
        Vector4f normalPrimitiveExtended = 
                new Vector4f(normalPrimitive.x(), normalPrimitive.y(), normalPrimitive.z(), 0.0f);
        
        // get objMatrix
        Matrix4f objMatrix = this.getTransformMatrix();

        // now transform with obj matrix, transpose of inverse...
        Matrix4f transposeInv = objMatrix.normal();
        
        // now actual normal
        Vector4f normalAtInterscnPtExtended = new Vector4f();
        normalPrimitiveExtended.mul(transposeInv, normalAtInterscnPtExtended);
        
        // now write as 3Vec
        Vector3f normalAtInterscnPt = 
                new Vector3f(normalAtInterscnPtExtended.x,normalAtInterscnPtExtended.y,normalAtInterscnPtExtended.z);

        // normalize the vector and return
        normalAtInterscnPt.normalize();
        FixedVector normalAtIntersection = new FixedVector(normalAtInterscnPt);
        return normalAtIntersection;
    }
    
    // given a ray and a triangle, returns the pt of intersection of ray and triangle plane
    /**
     * @ray = input ray
     * outputs the pt of intersection of the ray and the tri plane
     */
    public FixedVector rayPlaneIntersection(Ray ray){

        // first transform ray to primitive
        // get matrix and inverse
        Matrix4f objMatrix = this.getTransformMatrix();

        Ray primitiveRay = Ray.transformRayToPrimitive(ray, objMatrix);


        return null;

    }

    @Override
    public boolean rayHits(Ray aRay) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    
    
    
    
    // for unit tests
    // creates tri from just input vertices
    public Triangle(Vector3f vert1, Vector3f vert2, Vector3f vert3, Matrix4f triMatrix) {
        this.type = Shape.TRIANGLE;
        
        this.v1 = new FixedVector(vert1);
        this.v2 = new FixedVector(vert2);
        this.v3 = new FixedVector(vert3);
        
        this.ambient = null;
        this.diffuse = null;
        this.specular = null;
        this.emission = null;
        this.shininess = 0.0f;
        
        FixedVector side1 = this.v2.subtractFixed(this.v1);
        FixedVector side2 = this.v3.subtractFixed(this.v1);

        // normal is cross product
        FixedVector rawPrimitiveNormal = side1.cross(side2);
        
        this.primitiveNormal = rawPrimitiveNormal.normalize();
        
        // set the matrix as identity
        this.setTransformMatrix(triMatrix);
    }
    
}


