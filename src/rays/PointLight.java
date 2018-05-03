package rays;

public class PointLight implements Light{
    public final Type lightSource;
    public final FixedVector lightPosition;
    public final FixedVector lightColor;
    
    public PointLight(FixedVector position, FixedVector color) {
        
        this.lightSource = Type.POINT;
        this.lightPosition = position;
        this.lightColor = color;
        
    }

}
