package rays;

public interface Light {
    
    public enum Type{
        POINT, DIRECTIONAL
    }
    
    public Type getType();
    
    
}
