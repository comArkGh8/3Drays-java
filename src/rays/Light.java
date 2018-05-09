package rays;

import java.awt.Color;

public interface Light {
    
    public enum Type{
        POINT, DIRECTIONAL
    }
    
    public Type getType();
    
    // ComputeLight Fcn:
    public Color computeLight (const vec3 direction, const vec3 lightcolor,
        const vec3 normal, const vec3 halfvec, const vec3 mydiffuse, const vec3 myspecular,
        const float myshininess) {



            float nDotL = glm::dot(normal, direction)  ;

            vec3 lambert = mydiffuse * lightcolor * max (nDotL, (float) 0.0) ;


            float nDotH = glm::dot(normal, halfvec) ;
            vec3 phong = myspecular * lightcolor * pow (max(nDotH, (float) 0.0), myshininess) ;


            vec3 retval = lambert + phong ;
            return retval ;
    }
    
    
}
