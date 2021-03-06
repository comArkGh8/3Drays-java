package rays;

import java.util.Collections;
import static java.lang.System.out;

import java.util.List;

import org.joml.*;
import java.lang.*;
import java.lang.Math;

import rays.Primitive.Shape;

public class Triangle extends Primitive {
    
    public final Shape type;
    
    public final FixedVector v1;
    public final FixedVector v2;
    public final FixedVector v3;
    public final FixedVector primitiveNormal;

            
    // creates a triangle
    public Triangle(int id, FixedVector vert1, FixedVector vert2, FixedVector vert3, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emissionList, float shininess, FixedMatrix4 matrixIn) {
        
        this.id = id;
        this.type = Shape.TRIANGLE;
        
        this.v1 = vert1;
        this.v2 = vert2;
        this.v3 = vert3;

        this.ambient = new FixedVector(ambientList.get(0), ambientList.get(1), ambientList.get(2));
        this.diffuse = new FixedVector(diffuseList.get(0), diffuseList.get(1), diffuseList.get(2));
        this.specular = new FixedVector(specularList.get(0), specularList.get(1), specularList.get(2));
        this.emission = new FixedVector(emissionList.get(0), emissionList.get(1), emissionList.get(2));
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
    
    
    private Vector4fc getPlaneEqnVector() {
        // use n X = n P
        FixedVector normalVec = this.getNormalAt(null);
        // for RHS use v1 transformed:
        FixedMatrix4 objMat = this.getTransformMatrix();
        FixedVector vertexOnePositioned = Geometry.mat4MultPosVec3(v1, objMat);
        float RHS = normalVec.dot(vertexOnePositioned);
        
        Vector4fc planeEqnRep = new Vector4f(normalVec.x(),normalVec.y(),normalVec.z(),RHS);
        
        return planeEqnRep;
    }
    

    @Override
    public FixedVector getNormalAt(FixedVector point) {
        // normal primitive is already normalized
        FixedVector normalPrimitive = this.primitiveNormal;
        // make into Vector3fc
        FixedVector inputVector = new FixedVector(normalPrimitive.x(),normalPrimitive.y(),normalPrimitive.z());
        
        // get objMatrix
        FixedMatrix4 objMatrix = this.getTransformMatrix();
        // now transform with obj matrix, transpose of inverse...
        FixedMatrix4 transposeInv = objMatrix.normal();
        
        // now actual normal: multiply with transpose inverse
        FixedVector normalAtInterscnPt = Geometry.mat4MultDirVec3(inputVector, transposeInv);              

        // normalize the vector and return
        FixedVector normalAtIntersection = normalAtInterscnPt.normalize();
        return normalAtIntersection;
    }
    
    // given a ray and a triangle, returns the pt of intersection of ray and triangle plane
    /**
     * @ray = input ray
     * outputs the pt of intersection of the ray and the tri plane
     */
    public FixedVector rayPlaneIntersection(Ray ray){

        // get planeEqnRep
        Vector4fc planeEqnRep = this.getPlaneEqnVector();
        float d = planeEqnRep.w();
       
        FixedVector nVec = new FixedVector(planeEqnRep.x(), planeEqnRep.y(), planeEqnRep.z());
        FixedVector startRay = ray.getStartVector();
        FixedVector dirRay = ray.getDirectionVector();
        
        // intersection time: (this should be actual space)
        float t = (d - nVec.dot(startRay))/(nVec.dot(dirRay));
        
        float xIntersect = startRay.x() + (dirRay.x())*t;
        float yIntersect = startRay.y() + (dirRay.y())*t;
        float zIntersect = startRay.z() + (dirRay.z())*t;
        
        // make FixedVec and transform
        FixedVector actualIntersect = new FixedVector(xIntersect, yIntersect, zIntersect);
        
        return actualIntersect;

    }

    
    // given a ray and a triangle, returns true if intersection is in triangle, else false
    @Override
    public boolean rayHits(Ray aRay) {
        
        FixedVector side1Tri = this.v2.subtractFixed(v1);
        FixedVector side2Tri = this.v3.subtractFixed(v1);
        // first get intersection with plane
        // then find soln to point with side vectors

        // this is in actual space!
        FixedVector pointOnPlane = this.rayPlaneIntersection(aRay);

        // check to see if in direction of rayDirection
        FixedVector rayStartToPt = pointOnPlane.subtractFixed(aRay.getStartVector());
        if (rayStartToPt.dot(aRay.getDirectionVector())<GlobalConstants.acceptableError) {
            return false;
        }

        // now go back to primitive space
        FixedMatrix4 objMatrix = this.getTransformMatrix();
        FixedMatrix4 invMatrix=  objMatrix.invert();        
        FixedVector primitiveInterPt = Geometry.mat4MultPosVec3(pointOnPlane, invMatrix);

        FixedVector ptToPrimTri = primitiveInterPt.subtractFixed(this.v1);

        // check cross prod for non-zero determinant
        FixedVector sidesCrossProd = side1Tri.cross(side2Tri);
        
        float alpha=0;
        float beta=0;

        if (Math.abs(sidesCrossProd.x()) > GlobalConstants.acceptableError){
            alpha = (side2Tri.z() * ptToPrimTri.y() - side2Tri.y() * ptToPrimTri.z())/
                            (sidesCrossProd.x());
            beta = (-side1Tri.z() * ptToPrimTri.y() + side1Tri.y() * ptToPrimTri.z())/
                            (sidesCrossProd.x());
        }
        else if (Math.abs(sidesCrossProd.y())  > GlobalConstants.acceptableError){
            alpha = (side2Tri.x() * ptToPrimTri.z() - side2Tri.z() * ptToPrimTri.x())/
                            (sidesCrossProd.y());
            beta = (-side1Tri.x() * ptToPrimTri.z() + side1Tri.z() * ptToPrimTri.x())/
                            (sidesCrossProd.y());
        }
        else if (Math.abs(sidesCrossProd.z())  > GlobalConstants.acceptableError){
            alpha = (side2Tri.y() * ptToPrimTri.x() - side2Tri.x() * ptToPrimTri.y())/
                            (sidesCrossProd.z());
            beta = (-side1Tri.y() * ptToPrimTri.x() + side1Tri.x() * ptToPrimTri.y())/
                            (sidesCrossProd.z());
        }

        if ( (alpha >= 0 & beta >= 0) & (alpha+beta<=1 ) ) {
            return true;
        }
        else{
            return false;
        }
        
    }
    
    
    // assumes a hit!
    @Override
    public FixedVector getHitPoint(Ray aRay) {
        FixedVector hitPt = this.rayPlaneIntersection(aRay);
        return hitPt;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    
    
    
    // for unit tests
    // creates tri from just input vertices
    public Triangle(Vector3f vert1, Vector3f vert2, Vector3f vert3, FixedMatrix4 triMatrix) {
        this.type = Shape.TRIANGLE;
        
        this.v1 = new FixedVector(vert1);
        this.v2 = new FixedVector(vert2);
        this.v3 = new FixedVector(vert3);
        
        this.ambient = null;
        this.diffuse = null;
        this.specular = null;
        this.emission = null;
        this.shininess = 0.0f;
        
        this.id = 0;
        
        FixedVector side1 = this.v2.subtractFixed(this.v1);
        FixedVector side2 = this.v3.subtractFixed(this.v1);

        // normal is cross product
        FixedVector rawPrimitiveNormal = side1.cross(side2);
        
        this.primitiveNormal = rawPrimitiveNormal.normalize();
        
        // set the matrix as identity
        this.setTransformMatrix(triMatrix);
    }
    
    // for unit tests
    // creates tri from just input vertices
    public Triangle(FixedVector vert1, FixedVector vert2, FixedVector vert3, FixedMatrix4 triMatrix) {
        this.type = Shape.TRIANGLE;
        
        this.v1 = vert1;
        this.v2 = vert2;
        this.v3 = vert3;
        
        this.ambient = null;
        this.diffuse = null;
        this.specular = null;
        this.emission = null;
        this.shininess = 0.0f;
        
        this.id = 0;
        
        FixedVector side1 = this.v2.subtractFixed(this.v1);
        FixedVector side2 = this.v3.subtractFixed(this.v1);

        // normal is cross product
        FixedVector rawPrimitiveNormal = side1.cross(side2);
        
        this.primitiveNormal = rawPrimitiveNormal.normalize();
        
        // set the matrix as identity
        this.setTransformMatrix(triMatrix);
    }





    
}


