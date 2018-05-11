package rays;

import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
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
    
    private final int id;
    
    // creates a sphere
    public Sphere(int id, FixedVector centerIn, float radiusIn, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emissionList, float shininess, Matrix4f matrixIn) {
        
        this.id = id;
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

        // get matrix and inverse
        Matrix4f objMatrix = this.getTransformMatrix();
        Matrix4f objInv = new Matrix4f();
        objMatrix.invert(objInv);
        
        // transform intersection point
        Vector3fc actualPointVec3 = new Vector3f(point.x(),point.y(),point.z());
        FixedVector primitiveIntersectionPtFixed = Geometry.mat4MultPosVec3(actualPointVec3, objInv);
        Vector3fc primitiveIntersectionPt = new Vector3f(primitiveIntersectionPtFixed.x(),
                primitiveIntersectionPtFixed.y(),primitiveIntersectionPtFixed.z());       
        
        // get center as Vec3
        Vector3fc centerVec3 = new Vector3f(this.center.x(), this.center.y(), this.center.z());
        Vector3f normalAtPrimitiveInterscnPt = new Vector3f();
        primitiveIntersectionPt.sub(centerVec3, normalAtPrimitiveInterscnPt);
        
        // transform normal with transpose inverse
        Matrix4f transposeInv = objMatrix.normal();
        FixedVector normalAtIntersectionRaw = Geometry.mat4MultDirVec3(normalAtPrimitiveInterscnPt, transposeInv);
        FixedVector normalAtIntersection = normalAtIntersectionRaw.normalize();       
        
        return normalAtIntersection;
    }
    
    // returns discriminant associated with primitve sphere
    // if ray points in wrong direction return -1;
    //CHANGE TO PRIVATE!!!
    private float getDiscWith(Ray ray){
        float discriminant;

        // first transform ray to primitive
        Matrix4f sphMatrix = this.getTransformMatrix();
        Ray primitiveRay = Ray.transformRayToPrimitive(ray, sphMatrix);

        FixedVector rayDirn = primitiveRay.getDirectionVector();
        FixedVector rayStart = primitiveRay.getStartVector();
        FixedVector sphCenter = this.center;
        
        // check to see if in direction of rayDirection
        FixedVector rayStartToCenter = sphCenter.subtractFixed(ray.getStartVector());
        if (rayStartToCenter.dot(ray.getDirectionVector())<GlobalConstants.acceptableError) {
            return -1;
        }
        
        float r = this.radius;
        // quadratic eqn reads:
        // a = rayDirn^2;  b = 2 rayDirn (rayStart - sphCenter); c = (rayStart - sphCenter)^2
        float a = rayDirn.dot(rayDirn);
        float b = 2*( rayDirn.dot( rayStart.subtractFixed( sphCenter) ) ) ;
        // from center to ray start:
        FixedVector centerToStart = rayStart.subtractFixed(sphCenter);
        float c = centerToStart.dot(centerToStart) -r*r;

        discriminant = b*b - 4*a*c;
        return discriminant;

    }
    

    @Override
    public boolean rayHits(Ray aRay) {
        // get discriminant
        float disc = this.getDiscWith(aRay);

        // if disc < 0 does not
        if (disc<GlobalConstants.acceptableError){
            return false;
        }
        return true;
    }
    
     // given a ray and a sphere, returns the pt of intersection of ray and sphere
     // assume intersection; disc >= 0
     @Override
     public FixedVector getHitPoint(Ray ray){
         // get center vector
         FixedVector sphCenter = this.center;
    
         // first transform ray to primitive
         Matrix4f objMatrix = this.getTransformMatrix();
         Ray primitiveRay = Ray.transformRayToPrimitive(ray, objMatrix);
    
         // get (primitve) ray start and direction
         FixedVector rayStart = primitiveRay.getStartVector();
         FixedVector rayDirn = primitiveRay.getDirectionVector();
    
         float a = rayDirn.dot(rayDirn);
         float b = 2*( rayDirn.dot( rayStart.subtractFixed( sphCenter) ) ) ;
    
         float disc = this.getDiscWith(ray);
    
         // soln to quadratic
         // a = rayDirn^2;  b = 2 rayDirn (rayStart - sphCenter); c = (rayStart - sphCenter)^2-r^2
         double soln1 = (-b+Math.sqrt(disc))/(2*a);
         double soln2 = (-b-Math.sqrt(disc))/(2*a);
    
         // soln gives time for ray
         double t;
    
         // from assumption: if disc !=0 then must be pos
         if (disc < GlobalConstants.acceptableError){
             t = soln1;
         }
         else if (soln2<=0){
             t = soln1;
         }
         else{
             t = soln2;
         }
    
         // now find pt by plugging in time into (primitive) ray eqn:
    
         float xIntersect = (float) (rayStart.x() + (rayDirn.x())*t);
         float yIntersect = (float) (rayStart.y() + (rayDirn.y())*t);
         float zIntersect = (float) (rayStart.z() + (rayDirn.z())*t);
    
         // now transform this point to actual coordinates
         FixedVector primitiveVec = new FixedVector(xIntersect,yIntersect,zIntersect);
         FixedVector actualPt = Geometry.mat4MultPosVec3(primitiveVec, objMatrix);
    
         return actualPt;
     }
     
     
     @Override
     public int getId() {
         return this.id;
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
        this.id = 0;
        
        // set the matrix
        this.setTransformMatrix(matrixIn);
    }



    
    
    
}
