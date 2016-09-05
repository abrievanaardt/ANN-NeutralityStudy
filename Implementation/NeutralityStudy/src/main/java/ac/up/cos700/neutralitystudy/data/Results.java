package ac.up.cos700.neutralitystudy.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
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
    public static void writeToFile(String experimentName, String resultType, double... values) throws IOException {
        BufferedWriter writer = null;
        try {
            File directory = new File(experimentName + "/" + resultType + ".csv");
            directory.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(directory, true));
            
            for (int i = 0; i < values.length; i++) {
                writer.write(String.format(Locale.forLanguageTag("en_ZA"), "%f", values[i]));
                writer.newLine();
            }
            
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

}
