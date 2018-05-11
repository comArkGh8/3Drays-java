package rays;

import java.awt.Color;
import java.util.List;
import java.util.Map;

public interface Light {
    
    public enum Type{
        POINT, DIRECTIONAL
    }
    
    public Type getType();
    
    public FixedVector getColor();
    
    public FixedVector getPosition ();

    
    // ComputeLight Fcn:
    public static Color computeLight (FixedVector direction, FixedVector lightcolor,
            FixedVector normal, FixedVector halfvec, List<Float> mydiffuse, List<Float> myspecular,
            float myshininess) {
        
        float nDotL =  normal.dot(direction);
        
        // get entries for lambert
        float[] lambertArray;
        lambertArray = new float[3];
        for (int l=0; l<3; l++) {
            lambertArray[l] = mydiffuse.get(l) * lightcolor.get(l) * Math.max (nDotL, 0);
        }
 
        float nDotH =  normal.dot(halfvec);

        float[] phongArray;
        phongArray = new float[3];
        for (int l=0; l<3; l++) {
            phongArray[l] = (float) (myspecular.get(l) * lightcolor.get(l) * 
                    Math.pow (Math.max(nDotH, 0), myshininess));
        }
        // now create color with two arrays
        Color diffuseSpecularLight = Colors.getColor(lambertArray, phongArray);        
        return diffuseSpecularLight ;
    }
    
    public abstract boolean reaches(FixedVector hitPt, Map<Integer, Primitive> objects);
    
    
}
