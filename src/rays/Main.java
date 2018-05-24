package rays;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) throws IOException {

        // generate scene
        String pathNameToFile = args[0];       
        Optional<File> file = Optional.empty();
        file = Optional.of(new File(pathNameToFile));
        File fileToInsert = file.get();
        Scene fileScene = new Scene(fileToInsert);
        
        // set constants
        int maxDepth = fileScene.maxDepth;
        if (maxDepth == 0) {
            maxDepth = 5;
        }
        
        maxDepth = 1;
        
        float error = 0.0000000001f;
        GlobalConstants myConstants = new GlobalConstants(error, maxDepth);
        
        String pathNameToOutFile = args[1];
        File outFile = new File(pathNameToOutFile);
        int numThreads = Integer.parseInt(args[2]);
        
        String pathNameForOutput = args[3]; 
        
        try {
            RenderImage.draw(fileScene, outFile, numThreads, pathNameForOutput);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
