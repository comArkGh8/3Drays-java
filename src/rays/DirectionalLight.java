package rays;

import java.awt.Color;
import java.util.Map;

public class DirectionalLight implements Light{
        public final Type lightSource;
        public final FixedVector lightDirection;
        public final FixedVector lightColor;
        
        public DirectionalLight(FixedVector direction, FixedVector color) {
            // Note: direction is the direction into the light
            this.lightSource = Type.DIRECTIONAL;
            this.lightDirection = direction;
            this.lightColor = color;
            
        }
        
        public DirectionalLight(FixedVector directionVec, float colorR, float colorG, float colorB) {
            // create FixedVector with colors
            FixedVector rgbVec = new FixedVector(colorR, colorG, colorB);
            this.lightSource = Type.DIRECTIONAL;
            this.lightDirection = directionVec;
            this.lightColor = rgbVec;
            
        }

        public final Type getType(){
            return this.lightSource;
        }

        /**
         * determines if ray from hit point in direction of
         * light hits any other object obstructing the light
         * @param objects: does not include the object on which the hitPt rests
         */
        @Override
        public boolean reaches(FixedVector hitPt, Map<Integer, Primitive> objects) {
            // create ray from hitPt in direction of light
            Ray rayInDirnLight = new Ray(hitPt, this.lightDirection);
            // if distance to closest object returns -1, then light is visible
            if (rayInDirnLight.getClosestDistanceToAnyObjectAmong(objects)<0) {
                return true;
            }
            return false;
        }
        
        @Override
        public FixedVector getColor() {
            return this.lightColor;
        }

        @Override
        public FixedVector getPosition() {
            throw new IllegalArgumentException();
        }

        @Override
        public FixedVector getDirectionTo() {
            FixedVector directionIn = this.lightDirection;
            //FixedVector lightTo = directionIn.multConst(-1);
            return directionIn.normalize();
        }


    }

