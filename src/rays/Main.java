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
        file = Optional.of(new File(args[0]));
        File fileToInsert = file.get();
        Scene fileScene = new Scene(fileToInsert);
        
        File outFile = new File(args[1]);
        int numThreads = Integer.parseInt(args[2]);
        
        try {
            RenderImage.draw(fileScene, outFile, numThreads);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
