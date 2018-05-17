


package rays;

import org.joml.*;

// use for geometric calculations
public class Geometry {
    
    /**
     * 
     * @param inVector - inputs read only 3Vec
     *      assumes is a direction vector (extended to Vec4 by 0)
     * @param mulMatrix - matrix with which to multiply
     * @return - Vec3 result of matrix acting on extended vector
     */
    public static  FixedVector mat4MultDirVec3 (FixedVector inVector, FixedMatrix4 mulMatrix) {
        // write inVector as 4Vec
        Vector4fc inVectorExtended = 
                new Vector4f(inVector.x(), inVector.y(), inVector.z(), 0.0f);
        
        // write mulMatrix as Matrix4f      
        Matrix4f mulMatrix4f = mulMatrix.writeAs4f();
        
        // now multiply
        Vector4f multVectorExtended = new Vector4f();
        inVectorExtended.mul(mulMatrix4f, multVectorExtended);
        
        // now write as 3Vec
        Vector3fc resultVector = 
                new Vector3f(multVectorExtended.x,multVectorExtended.y,multVectorExtended.z);
        // write as FixedVector
        FixedVector fixedResult = new FixedVector(resultVector);

        return fixedResult;
    }
    
    /**
     * 
     * @param inVector - inputs read only 3Vec
     *      assumes vector has start (extended to Vec4 by 1.0)
     * @param mulMatrix - matrix with which to multiply
     * @return - Vec3 result of matrix acting on extended vector
     */
    public static  FixedVector mat4MultPosVec3 (Vector3fc inVector, FixedMatrix4 mulMatrix) {
        // write inVector as 4Vec
        Vector4fc inVectorExtended = 
                new Vector4f(inVector.x(), inVector.y(), inVector.z(), 1.0f);
        
        // write mulMatrix as Matrix4f      
        Matrix4f mulMatrix4f = mulMatrix.writeAs4f();
        
        // now multiply
        Vector4f multVectorExtended = new Vector4f();
        inVectorExtended.mul(mulMatrix4f, multVectorExtended);
        
        // now write as 3Vec
        Vector3fc resultVector = new Vector3f(multVectorExtended.x/multVectorExtended.w,
                        multVectorExtended.y/multVectorExtended.w,multVectorExtended.z/multVectorExtended.w);
        // write as FixedVector
        FixedVector fixedResult = new FixedVector(resultVector);

        return fixedResult;
    }
    
    public static  FixedVector mat4MultPosVec3 (FixedVector inVector, FixedMatrix4 mulMatrix) {
        // write inVector as 4Vec
        Vector4fc inVectorExtended = 
                new Vector4f(inVector.x(), inVector.y(), inVector.z(), 1.0f);
        
        // write mulMatrix as Matrix4f      
        Matrix4f mulMatrix4f = mulMatrix.writeAs4f();
        
        // now multiply
        Vector4f multVectorExtended = new Vector4f();
        inVectorExtended.mul(mulMatrix4f, multVectorExtended);
        
        // now write as 3Vec
        Vector3fc resultVector = new Vector3f(multVectorExtended.x/multVectorExtended.w,
                        multVectorExtended.y/multVectorExtended.w,multVectorExtended.z/multVectorExtended.w);
        // write as FixedVector
        FixedVector fixedResult = new FixedVector(resultVector);

        return fixedResult;
    }
    
    
    /**
     * 
     * @param inVector - incoming direction vector
     * @param normalVector - normal of vector to surface (plane) on which 
     *      direction is to be reflected
     * @return - direction (normalized) of reflection 
     */
    public static  FixedVector reflectDirectionVector (FixedVector inVector, FixedVector normalVector) {
        // write direction as d_n + d_inplane
        // first normalize normal vector! (should be done as input)
        FixedVector normalizedNormal = normalVector.normalize();
        float dDotn = inVector.dot(normalizedNormal);  
        FixedVector dN = normalizedNormal.multConst(dDotn);

        FixedVector dPlane = inVector.subtractFixed(dN);
        FixedVector reflectDir = dPlane.subtractFixed(dN);

        FixedVector reflectNormalized = reflectDir.normalize();

        return reflectNormalized;
    }


    
}


