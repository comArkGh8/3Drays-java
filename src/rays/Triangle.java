package rays;

import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;

public class Triangle extends Primitive {
    
    public final FixedVector v1;
    public final FixedVector v2;
    public final FixedVector v3;
    public final FixedVector primitiveNormal;
    
    // contains coloring properties, these should be final
    public final List<Float> ambient;
    public final List<Float> diffuse;
    public final List<Float> specular;
    public final List<Float> emmission;
    public final float shininess;
    
    // creates a triangle
    public Triangle(FixedVector vert1, FixedVector vert2, FixedVector vert3, List<Float> ambientList, List<Float> diffuseList, List<Float> specularList,
            List<Float> emmissionList, float shininess, Matrix4f matrixIn) {
        
        this.v1 = vert1;
        this.v2 = vert2;
        this.v3 = vert3;

        this.ambient = Collections.unmodifiableList(ambientList);
        this.diffuse = Collections.unmodifiableList(diffuseList);
        this.specular = Collections.unmodifiableList(diffuseList);
        this.emmission = Collections.unmodifiableList(diffuseList);
        this.shininess = shininess;
        

        FixedVector side1 = this.v3.subtractFixed(this.v1);
        FixedVector side2 = this.v2.subtractFixed(this.v1);
        

        
        // normal is cross product
        FixedVector rawPrimitiveNormal = side1.cross(side2);
        
        this.primitiveNormal = rawPrimitiveNormal.normalize();
        
        // set the matrix
        this.setTransformMatrix(matrixIn);


    }
    
}


