package ac.up.cos700.neutralitystudy.data;

import ac.up.cos700.neutralitystudy.data.util.GraphException;
import ac.up.cos700.neutralitystudy.data.util.ResultsException;
import ac.up.cos700.neutralitystudy.function.IFunction;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Allows for experiment data to be written to file and/or directly transformed
 * into plots using JavaPlot/GNUPlot.
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
    public synchronized static void writeToFile(String experimentName, String resultType, double... values) throws ResultsException {
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
        catch (IOException e) {
            throw new ResultsException(e.getMessage());
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

    public synchronized static void newGraph(String path, String title, String xLabel, String yLabel, String zLabel, int dimensions)
            throws ResultsException {
        try {
            Graph graph = new Graph(path, title, xLabel, yLabel, zLabel, dimensions);               
            currentGraph = graph;
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }

    public synchronized static void addPlot(String title, IFunction function)
            throws ResultsException {
        try {
            currentGraph.addPlot(title, function);
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }
    
    public synchronized static void addPlot(String title, IFunction function, int lowerbound, int upperbound)
            throws ResultsException {
        try {
            currentGraph.addPlot(title, function, lowerbound, upperbound);
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }
    
    public synchronized static void addPlot(String title, double[] xData, double[] yData)
            throws ResultsException {
        try {
            currentGraph.addPlot(title, xData, yData);
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }
    
    public synchronized static void plot()
            throws ResultsException {
        try {
            currentGraph.plot();
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }

    private static Graph currentGraph;
}
