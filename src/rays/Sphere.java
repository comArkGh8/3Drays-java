package rays;

import java.util.Collections;
import static java.lang.System.out;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

public class Sphere extends Primitive {
    
    public final FixedVector center;
    public final float radius;
    public final Shape type;
    
    // creates a sphere
    public Sphere(int id, FixedVector centerIn, float radiusIn, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emissionList, float shininess, FixedMatrix4 matrixIn) {
        
        this.id = id;
        this.type = Shape.SPHERE;
        
        this.center = centerIn;
        this.radius = radiusIn;
        
        this.ambient = new FixedVector(ambientList.get(0), ambientList.get(1), ambientList.get(2));
        this.diffuse = new FixedVector(diffuseList.get(0), diffuseList.get(1), diffuseList.get(2));
        this.specular = new FixedVector(specularList.get(0), specularList.get(1), specularList.get(2));
        this.emission = new FixedVector(emissionList.get(0), emissionList.get(1), emissionList.get(2));
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
        FixedMatrix4 objMatrix = this.getTransformMatrix();
        FixedMatrix4 objInv = objMatrix.invert();
        
        // transform intersection point
        Vector3fc actualPointVec3 = new Vector3f(point.x(),point.y(),point.z());
        FixedVector primitiveIntersectionPtFixed = Geometry.mat4MultPosVec3(actualPointVec3, objInv);
        FixedVector primitiveIntersectionPt = new FixedVector(primitiveIntersectionPtFixed.x(),
                primitiveIntersectionPtFixed.y(),primitiveIntersectionPtFixed.z());       
        
        // get center as Vec3
        FixedVector centerVec3 = new FixedVector(this.center.x(), this.center.y(), this.center.z());       
        FixedVector normalAtPrimitiveInterscnPt = primitiveIntersectionPt.subtractFixed(centerVec3);
        
        // transform normal with transpose inverse
        FixedMatrix4 transposeInv = objMatrix.normal();
        FixedVector normalAtIntersectionRaw = Geometry.mat4MultDirVec3(normalAtPrimitiveInterscnPt, transposeInv);
        FixedVector normalAtIntersection = normalAtIntersectionRaw.normalize();       
        
        return normalAtIntersection;
    }
    
    // returns discriminant associated with primitve sphere
    // if ray points in wrong direction return -1;
    private float getDiscWith(Ray ray){
        float discriminant;

        // first transform ray to primitive
        FixedMatrix4 sphMatrix = this.getTransformMatrix();
        Ray primitiveRay = Ray.transformRayToPrimitive(ray, sphMatrix);

        FixedVector primRayDirn = primitiveRay.getDirectionVector();
        FixedVector primRayStart = primitiveRay.getStartVector();
        FixedVector sphCenter = this.center;
        
        // check to see if in direction of rayDirection
        FixedVector primRayStartToCenter = sphCenter.subtractFixed(primRayStart);
        if (primRayStartToCenter.dot(primRayDirn)<GlobalConstants.acceptableError) {
            return -1;
        }
        
        float r = this.radius;
        // quadratic eqn reads:
        // a = rayDirn^2;  b = 2 rayDirn (rayStart - sphCenter); c = (rayStart - sphCenter)^2
        float a = primRayDirn.dot(primRayDirn);
        float b = 2*( primRayDirn.dot( primRayStart.subtractFixed( sphCenter) ) ) ;
        // from center to ray start:
        FixedVector centerToStart = primRayStart.subtractFixed(sphCenter);
        float c = centerToStart.dot(centerToStart) -r*r;

        discriminant = b*b - 4*a*c;

        return discriminant;

    }
    

    @Override
    public boolean rayHits(Ray aRay) {
        // get discriminant
        float disc = this.getDiscWith(aRay);

        // if disc < 0 does not
        if (disc<0){
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
         FixedMatrix4 objMatrix = this.getTransformMatrix();
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
    public Sphere(Vector3f centerIn, float radiusIn, FixedMatrix4 matrixIn) {
        
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
