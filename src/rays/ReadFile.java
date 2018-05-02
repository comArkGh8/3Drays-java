package rays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// REDO FOR 3D RAYS!!!


public class ReadFile {
    
    public Board(File file) throws IOException{
        final int sizeX;
        final int sizeY;
        
        String nameOfFile = file.getName();
        String path = file.getAbsolutePath();
        File fileToRead = new File(path);  
        
        
        // start up the squaresMap
        this.squaresMap = new ConcurrentSkipListMap<Integer,Square>();

        // read in board setup from file
        // use try with resources for buffered reader
        try(BufferedReader bufRead = new BufferedReader(new FileReader(fileToRead))){

            int rowTracker =0;
            String myLine = null;
            String[] dimensionsString;
            String[] valueOfSquareString;
            
            // read the first line to get dimensions
            myLine = bufRead.readLine();
            dimensionsString = myLine.split(" ");
            sizeX= Integer.parseInt(dimensionsString[0]);
            sizeY= Integer.parseInt(dimensionsString[1]);
            
            // add dimensions to the board
            this.numberRows = sizeY;
            this.numberColumns =sizeX;
            
            
            // now read the rest of the lines
            while ((myLine = bufRead.readLine()) !=null){
                // process the line
                valueOfSquareString = myLine.split(" ");
                // check Rep of Board

                
                for (int columnIndex = 0; columnIndex < sizeX; columnIndex++){    
                    this.squaresMap.putIfAbsent(sizeX*rowTracker+columnIndex, 
                            new Square(valueOfSquareString[columnIndex]));
                }
                rowTracker++;
            }
        }
        
        // now start up the locks
        // divideGroups gives a mapping to groups
        Map <Integer,Set<Integer>> lockToGroupMap = divideGroups();
        this.lockNumberForGroup = lockToGroupMap;
        
        // now for each value in the numbering (of Groups)
        // associate one lock
        Map<Integer,ReentrantLock> mapForLocks= new HashMap<Integer, ReentrantLock>();
        for (int key:lockToGroupMap.keySet()) {
            mapForLocks.put(key, new ReentrantLock());
        }
        this.locks=mapForLocks;
    }  

}
