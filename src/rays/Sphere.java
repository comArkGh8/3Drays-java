package rays;

import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Sphere extends Primitive {
    
    public final FixedVector center;
    public final float radius;
    public final Shape type;
    
    // contains coloring properties, these should be final
    public final List<Float> ambient;
    public final List<Float> diffuse;
    public final List<Float> specular;
    public final List<Float> emission;
    public final float shininess;
    
    // creates a sphere
    public Sphere(FixedVector centerIn, float radiusIn, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emissionList, float shininess, Matrix4f matrixIn) {
        
        this.type = Shape.SPHERE;
        
        this.center = centerIn;
        this.radius = radiusIn;
        
        this.ambient = Collections.unmodifiableList(ambientList);
        this.diffuse = Collections.unmodifiableList(diffuseList);
        this.specular = Collections.unmodifiableList(diffuseList);
        this.emission = Collections.unmodifiableList(diffuseList);
        this.shininess = shininess;
        
        // set the matrix
        this.setTransformMatrix(matrixIn);
    }

    @Override
    public Shape getShape() {       
        return this.type;
    }

    @Override
    public FixedVector getNormalAt(FixedVector point) {
        // extend input point
        Vector4f actualPointVec4 = new Vector4f(point.x(),point.y(),point.z(),1.0f);

        // get matrix and inverse
        Matrix4f objMatrix = this.getTransformMatrix();
        Matrix4f objInv = new Matrix4f();
        objMatrix.invert(objInv);
        
        Vector4f primitiveIntersectionPtExtended = new Vector4f();
        actualPointVec4.mul(objInv,primitiveIntersectionPtExtended);
        
        Vector3f primitiveIntersectionPt = new Vector3f(primitiveIntersectionPtExtended.x/primitiveIntersectionPtExtended.w,
                    primitiveIntersectionPtExtended.y/primitiveIntersectionPtExtended.w,
                    primitiveIntersectionPtExtended.z/primitiveIntersectionPtExtended.w);
        
        // get center as Vec3
        Vector3f centerVec3 = new Vector3f(this.center.x(), this.center.y(), this.center.z());

        Vector3f normalAtPrimitiveInterscnPt = primitiveIntersectionPt.sub(centerVec3);
        Vector4f normalAtPrimitiveExtended = 
                new Vector4f(normalAtPrimitiveInterscnPt.x, normalAtPrimitiveInterscnPt.y,
                        normalAtPrimitiveInterscnPt.z, 0.0f);

        // now transform with obj matrix to actual coordinates:
        Matrix4f transposeInv = objMatrix.normal();
        
        // now actual normal extended
        Vector4f normalAtInterscnPtExtended = new Vector4f();
        normalAtPrimitiveExtended.mul(transposeInv, normalAtInterscnPtExtended);
        
        // now write as 3Vec
        Vector3f normalAtInterscnPt = 
                new Vector3f(normalAtInterscnPtExtended.x,normalAtInterscnPtExtended.y,normalAtInterscnPtExtended.z);

        // normalize the vector and return
        normalAtInterscnPt.normalize();
        FixedVector normalAtIntersection = new FixedVector(normalAtInterscnPt);
        return normalAtIntersection;
    }
    
    

    @Override
    public boolean rayHits(Ray aRay) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    // for unit tests;
    // creates sphere with just radius and center
    public Sphere(Vector3f centerIn, float radiusIn, Matrix4f matrixIn) {
        
        this.type = Shape.SPHERE;
        
        this.center = new FixedVector(centerIn);
        this.radius = radiusIn;
        
        this.ambient = null;
        this.diffuse = null;
        this.specular = null;
        this.emission = null;
        this.shininess = 0;
        
        // set the matrix
        this.setTransformMatrix(matrixIn);
    }

    
    
    
}
