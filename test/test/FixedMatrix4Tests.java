package test;

import static org.junit.Assert.*;
import static java.lang.System.out;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.junit.Test;

import rays.FixedMatrix4;

public class FixedMatrix4Tests {

    @Test
    public void testInverse() {
        Vector4fc col0 = new Vector4f(1, 2, 1, 2);
        Vector4fc col1 = new Vector4f(1, 1, 0, 0);
        Vector4fc col2 = new Vector4f(-1, 2, 0, 1);
        Vector4fc col3 = new Vector4f(0, 1, 1, 1);        
        Matrix4f matToInvert = new Matrix4f(col0,col1,col2,col3);
        
        FixedMatrix4 fixedInput = new FixedMatrix4(matToInvert);
        FixedMatrix4 fixedInvert = fixedInput.invert();
        
        //out.println(fixedInvert.toString());
        
    }

}
