package rays;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) throws IOException {
        // set constants
        int maxDepth = 1;
        float error = 0.00001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        
        // generate scene
        Optional<File> file = Optional.empty();
        String pathNameToFile = "D:\\eclipse workspace\\3DRays-java\\test_files\\scene1.test";
        file = Optional.of(new File(pathNameToFile));
        File fileToInsert = file.get();
        Scene fileScene = new Scene(fileToInsert);
        
        // DEBUGGING!!!!
        //out.println(fileScene.height);
        // END
        
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
