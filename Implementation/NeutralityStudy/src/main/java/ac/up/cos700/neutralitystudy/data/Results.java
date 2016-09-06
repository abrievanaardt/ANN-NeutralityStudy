package ac.up.cos700.neutralitystudy.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Allows for experiment data to be written to file and/or directly transformed
 * into plots using JavaPlot/GNUPlot in conjunction with the swing API.
 * 
 * @author Abrie van Aardt
 */
public class Results {

    /**
     * Appends the value(s) to a file with the relative path:
     * experimentName/resultType
     *
     * @param experimentName
     * @param resultType
     * @param values
     * @throws IOException
     */
    public synchronized static void writeToFile(String experimentName, String resultType, double... values) throws IOException {
        BufferedWriter writer = null;
        try {
            File directory = new File(experimentName + "/" + resultType + ".csv");
            directory.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(directory, true));
            
            writer.write(Double.toString(values[0]));
            for (int i = 1; i < values.length; i++) {
                writer.write(", " + Double.toString(values[i]));               
            }
            
            writer.newLine();            
            writer.flush();
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
    
    public synchronized static void plot(double... values){
        
    }

}
