package rays;

import java.awt.Color;
import static java.lang.System.out; 
import java.util.List;

public class Colors {
    
    Color colorOutput;
    
    public static FixedVector produceColor(FixedVector ambientInput, FixedVector emissionInput) {
        float[] totalArray;
        totalArray = new float[3];
        for (int index=0; index<3; index++) {
            totalArray[index] = ambientInput.get(index) + emissionInput.get(index);
        }
        // produce the color from the total
        FixedVector totalCol = new FixedVector(totalArray[0],totalArray[1],totalArray[2]);
        return totalCol;
    }
    
    /*
    public static Color produceColorFromVector(FixedVector inputColorVec) {
        
        return null;
    }
    */

    /*
    public static FixedVector getColor(FixedVector lambertVector, FixedVector phongVector) {        
        FixedVector totalCol = lambertVector.addFixed(phongVector);
        return totalCol;
    }
    */

    /*
    public static FixedVector add(FixedVector colorOneVec, FixedVector colorTwoVec) {
        FixedVector colorSumVec = colorOneVec.addFixed(colorTwoVec);
        return colorSumVec;
    }
    */

}
