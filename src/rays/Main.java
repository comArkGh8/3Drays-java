package rays;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) throws IOException {
        
       
        // generate scene
        //test_files\\scene6-alt
        // submission_scenes\\scene4-specular
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\submission_scenes\\scene7.test";
        file = Optional.of(new File(pathNameToFile));
        File fileToInsert = file.get();
        Scene fileScene = new Scene(fileToInsert);
        
        // set constants
        int maxDepth = fileScene.maxDepth;
        if (maxDepth == 0) {
            maxDepth = 5;
        }
        
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        String pathNameToOutFile = "D:\\eclipse workspace\\3DRays-java\\my_image_files\\output.png";
        File outFile = new File(pathNameToOutFile);
        try {
            RenderImage.draw(fileScene, outFile);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
