package rays;

import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;

public class Sphere extends Primitive {
    
    public final FixedVector center;
    public final float radius;
    
    // contains coloring properties, these should be final
    public final List<Float> ambient;
    public final List<Float> diffuse;
    public final List<Float> specular;
    public final List<Float> emmission;
    public final float shininess;
    
    // creates a sphere
    public Sphere(FixedVector centerIn, float radiusIn, FixedVector vert3, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emmissionList, float shininess, Matrix4f matrixIn) {
        
        this.center = centerIn;
        this.radius = radiusIn;
        
        this.ambient = Collections.unmodifiableList(ambientList);
        this.diffuse = Collections.unmodifiableList(diffuseList);
        this.specular = Collections.unmodifiableList(diffuseList);
        this.emmission = Collections.unmodifiableList(diffuseList);
        this.shininess = shininess;
        
        // set the matrix
        this.setTransformMatrix(matrixIn);
    }
    
}
