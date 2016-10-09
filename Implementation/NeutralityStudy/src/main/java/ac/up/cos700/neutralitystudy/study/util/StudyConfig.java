package ac.up.cos700.neutralitystudy.study.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the configuration of a study.
 *
 * @author Abrie van Aardt
 */
public class StudyConfig {

    public String name;
    public Map<String, Double> entries;
    public String path;

    public StudyConfig() {
        entries = new HashMap<>(20);
    }

    public static StudyConfig fromFile(String studyName) throws FileNotFoundException, StudyConfigException {
        StudyConfig config = new StudyConfig();               

        BufferedReader reader = new BufferedReader(new FileReader(new File(studyName + ".config")));

        try {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.indexOf("=") != -1) {
                    config.entries.put(line.split("=")[0].trim(),
                            Double.parseDouble(line.split("=")[1]));
                }
            }
        }
        catch (IOException | NumberFormatException e) {
            throw new StudyConfigException("Could not read from the config file: " + e.getMessage());
        }

        return config;
    }
}
