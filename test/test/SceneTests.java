package test;

import static java.lang.System.out;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import org.junit.Test;

import rays.Scene;

public class SceneTests {

    @Test
    public void testScene1NumberLights(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        
        File fileToInsert = file.get();
        //Scene testScene;

        
        try(BufferedReader bufRead = new BufferedReader(new FileReader(fileToInsert))){
            // read the first line to get size
            String myLine = bufRead.readLine();
            myLine.trim();
            while ( (myLine.length()==0) || (myLine.startsWith("#")) ){
                String[] dimensionsString = myLine.split(" ");
                myLine = bufRead.readLine();
                myLine.trim();
            }
            String[] dimensionsString = myLine.split(" ");
            
            int width= Integer.parseInt(dimensionsString[1]);
            int height= Integer.parseInt(dimensionsString[2]);
            
            assertEquals("width should be 640", 640, width);
            assertEquals("height should be 480", 480, height);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testScene1Insert(){
        Optional<File> file = Optional.empty();
        String pathNameToFile = "/home/sauld/computer_programming/computing_graphics/HW3Java/test_files/scene1.test";
        file = Optional.of(new File(pathNameToFile));
        assert(file.get().isFile());
        
        File fileToInsert = file.get();
        Scene testScene;
        try {
            testScene = new Scene(fileToInsert);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    

}
