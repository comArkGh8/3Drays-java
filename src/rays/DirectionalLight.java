package rays;

import java.awt.Color;

public class DirectionalLight implements Light{
        public final Type lightSource;
        public final FixedVector lightDirection;
        public final Color lightColor;
        
        public DirectionalLight(FixedVector direction, Color color) {
            // Note: direction is the direction into the light
            this.lightSource = Type.DIRECTIONAL;
            this.lightDirection = direction;
            this.lightColor = color;
            
        }
        
        public final Type getType(){
            return this.lightSource;
        }


    }

