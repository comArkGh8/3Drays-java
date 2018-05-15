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
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene3.test";
        file = Optional.of(new File(pathNameToFile));
        File fileToInsert = file.get();
        Scene fileScene = new Scene(fileToInsert);
        
        String pathNameToOutFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/my_images/output.png";
        File outFile = new File(pathNameToOutFile);
        try {
            RenderImage.draw(fileScene, outFile);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
