package rays;

import java.awt.Color;

public class PointLight implements Light{
    public final Type lightSource;
    public final FixedVector lightPosition;
    public final Color lightColor;
    
    public PointLight(FixedVector position, Color color) {
        
        this.lightSource = Type.POINT;
        this.lightPosition = position;
        this.lightColor = color;
        
    }

}
