package rays;



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

    }

