package rays;

import java.awt.Color;
import java.util.List;

public class Colors {
    
    Color colorOutput;
    
    public static Color produceColor(List<Float> ambient, List<Float> emission) {
        
        return null;
    }
    
    public static Color produceColorFromVector(FixedVector inputColorVec) {
        
        return null;
    }

    public static Color getColor(float[] lambertArray, float[] phongArray) {
        float[] totalArray;
        totalArray = new float[3];
        for (int index=0; index<3; index++) {
            totalArray[index] = lambertArray[index] + phongArray[index];
        }
        
        Color totalCol = new Color(totalArray[0],totalArray[1],totalArray[2]);
        return totalCol;
    }

    public static Color add(Color colorOne, Color colorTwo) {
        // get Vectors representing Colors
        FixedVector colorOneVec = new FixedVector(colorOne.getRed(), colorOne.getGreen(),
                colorOne.getBlue());
        FixedVector colorTwoVec = new FixedVector(colorTwo.getRed(), colorTwo.getGreen(),
                colorTwo.getBlue());
        FixedVector colorSumVec = colorOneVec.addFixed(colorTwoVec);
        Color colorSum = new Color(colorSumVec.x(), colorSumVec.y(), colorSumVec.z());
        return colorSum;
    }

}
