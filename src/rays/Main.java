package rays;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {
        // generate scene
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        File fileToInsert = file.get();
        Scene fileScene = new Scene(fileToInsert);
        
        

    }

}
