package rays;

import java.awt.Color;
import static java.lang.System.out;

import java.util.List;
import java.util.Map;

public interface Light {
    
    public enum Type{
        POINT, DIRECTIONAL
    }
    
    public Type getType();
    
    public FixedVector getColor();
    
    public FixedVector getPosition ();
    
    /**
     * 
     * @return the direction in which the light shines (if Directional!)
     * result is automatically normalized
     */
    public FixedVector getDirectionTo ();

    
    // ComputeLight Fcn:
    public static FixedVector computeLight (FixedVector direction, FixedVector lightcolor,
            FixedVector normal, FixedVector halfvec, FixedVector mydiffuse, FixedVector myspecular,
            float myshininess) {
        
        float nDotL =  - normal.dot(direction);
        
        // get entries for lambert
        float[] lambertArray;
        lambertArray = new float[3];
        for (int l=0; l<3; l++) {
            lambertArray[l] = mydiffuse.get(l) * lightcolor.get(l) * Math.abs(nDotL);
        }
        FixedVector lambertVector = new FixedVector(lambertArray[0], lambertArray[1], lambertArray[2]);
        
        
        float nDotH =  normal.dot(halfvec);    

        float[] phongArray;
        phongArray = new float[3];
        for (int l=0; l<3; l++) {
            phongArray[l] = (float) (myspecular.get(l) * lightcolor.get(l) * Math.pow (Math.abs(nDotH), myshininess) );            
        }
        FixedVector phongVector = new FixedVector(phongArray[0], phongArray[1], phongArray[2]);

        
        // now create color with two arrays
        FixedVector diffuseSpecularLight = lambertVector.addFixed(phongVector);        
        return diffuseSpecularLight ;
    }
    
    public abstract boolean reaches(FixedVector hitPt, Map<Integer, Primitive> objects);
    
    
}
