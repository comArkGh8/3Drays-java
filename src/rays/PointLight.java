package rays;

import java.awt.List;
import java.util.Map;

import rays.Light.Type;

public class PointLight implements Light{
    public final Type lightSource;
    public final FixedVector lightPosition;
    public final FixedVector lightColor;
    
    public PointLight(FixedVector position, FixedVector color) {
        
        this.lightSource = Type.POINT;
        this.lightPosition = position;
        this.lightColor = color;
        
    }
    
    public PointLight(FixedVector positionVec, float colorR, float colorG, float colorB) {
        // create FixedVector with colors
        FixedVector rgbVec = new FixedVector(colorR, colorG, colorB);
        this.lightSource = Type.POINT;
        this.lightPosition = positionVec;
        this.lightColor = rgbVec;
    }

    public final Type getType(){
        return this.lightSource;
    }
    
    /**
     * checks whether light shines on a certain pt
     * @param objects - list of objects minus hit object!
     * @param hitPt - point on object which is being studied
     * @return true if light is not obstructed to reach the point
     */
    @Override
    public boolean reaches(FixedVector hitPt, Map<Integer, Primitive> objects) {
        // make ray and check if distance is not smaller! (mod error)
        FixedVector lightPos = this.lightPosition;
        FixedVector lightRayDir = hitPt.subtractFixed(lightPos);
        
        // get distance of light to hit point
        float distanceCheck = lightRayDir.length();
        
        Ray lightRay = new Ray(lightPos, lightRayDir);
        
        // get distance of closest obj hitting ray
        float distanceTraveled = lightRay.getClosestDistanceToAnyObjectAmong(objects);
        
        if ( distanceTraveled  < distanceCheck - GlobalConstants.acceptableError) {
            return false;
        }
        
        return true;
    }

    @Override
    public FixedVector getColor() {
        return this.lightColor;
    }

    @Override
    public FixedVector getPosition() {
        return this.lightPosition;
    }

    @Override
    public FixedVector getDirectionTo() {
        throw new IllegalArgumentException();
    }



}
