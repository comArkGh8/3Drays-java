package rays;

import java.util.Arrays;
import java.util.List;

import org.joml.*;


public class FixedMatrix4 {
    
    // read m(col)(row)
    private final float m00, m01, m02, m03;
    private final float m10, m11, m12, m13;
    private final float m20, m21, m22, m23;
    private final float m30, m31, m32, m33;
    
    // creates fixedMatrix from a Matrix4f
    public FixedMatrix4(Matrix4f m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02(); this.m03 = m.m03();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12(); this.m13 = m.m13(); 
        this.m20 = m.m20(); this.m21 = m.m21(); this.m22 = m.m22(); this.m23 = m.m23();
        this.m30 = m.m30(); this.m31 = m.m31(); this.m32 = m.m32(); this.m33 = m.m33();     
      }
    
    // creates identity fixedMatrix
    public FixedMatrix4() {
        this.m00 = 1; this.m01 = 0; this.m02 = 0; this.m03 = 0;
        this.m10 = 0; this.m11 = 1; this.m12 = 0; this.m13 = 0; 
        this.m20 = 0; this.m21 = 0; this.m22 = 1; this.m23 = 0;
        this.m30 = 0; this.m31 = 0; this.m32 = 0; this.m33 = 1;     
      }
    
    
    // takes an (object) input matrix and returns its inverse
    public FixedMatrix4 invert() {
        // first create Matrix4f from input
        Vector4fc col0 = new Vector4f(this.m00, this.m01, this.m02, this.m03);
        Vector4fc col1 = new Vector4f(this.m10, this.m11, this.m12, this.m13);
        Vector4fc col2 = new Vector4f(this.m20, this.m21, this.m22, this.m23);
        Vector4fc col3 = new Vector4f(this.m30, this.m31, this.m32, this.m33);        
        Matrix4f matToInvert = new Matrix4f(col0,col1,col2,col3);
        
        Matrix4f objInv = new Matrix4f();
        matToInvert.invert(objInv);
        
        FixedMatrix4 retMat = new FixedMatrix4(objInv);
        return retMat;
    }
    
    // returns transpose inverse
    public FixedMatrix4 normal() {
        // first create Matrix4f from input
        Vector4fc col0 = new Vector4f(this.m00, this.m01, this.m02, this.m03);
        Vector4fc col1 = new Vector4f(this.m10, this.m11, this.m12, this.m13);
        Vector4fc col2 = new Vector4f(this.m20, this.m21, this.m22, this.m23);
        Vector4fc col3 = new Vector4f(this.m30, this.m31, this.m32, this.m33);        
        Matrix4f matToNormal = new Matrix4f(col0,col1,col2,col3);
        
        Matrix4f objTrnInv = matToNormal.normal();
        
        FixedMatrix4 retMat = new FixedMatrix4(objTrnInv);
        return retMat;
    }
    
    
    
    
    
    public String toString() {        
        // first create Matrix4f from input
        Vector4fc col0 = new Vector4f(this.m00, this.m01, this.m02, this.m03);
        Vector4fc col1 = new Vector4f(this.m10, this.m11, this.m12, this.m13);
        Vector4fc col2 = new Vector4f(this.m20, this.m21, this.m22, this.m23);
        Vector4fc col3 = new Vector4f(this.m30, this.m31, this.m32, this.m33);        
        Matrix4f matToPrint = new Matrix4f(col0,col1,col2,col3);
        
        String outString = matToPrint.toString();
        return outString;
    }
    
    
    // the following methods get the components of the matrix
    // creates fixedMatrix from a Matrix4f
    public float m00() {
        return this.m00; 
    }
    
    public float m01() {
        return this.m01; 
    }
    
    public float m02() {
        return this.m02; 
    }
    
    public float m03() {
        return this.m03; 
    }
    
    public float m10() {
        return this.m10; 
    }
    
    public float m11() {
        return this.m11; 
    }
    
    public float m12() {
        return this.m12; 
    }
    
    public float m13() {
        return this.m13; 
    }
    
    public float m20() {
        return this.m20; 
    }
    
    public float m21() {
        return this.m21; 
    }
    
    public float m22() {
        return this.m22; 
    }
    
    public float m23() {
        return this.m23; 
    }
    
    public float m30() {
        return this.m30; 
    }
    
    public float m31() {
        return this.m31; 
    }
    
    public float m32() {
        return this.m32; 
    }
    
    public float m33() {
        return this.m33; 
    }
    
    
    
    
    
    

}
