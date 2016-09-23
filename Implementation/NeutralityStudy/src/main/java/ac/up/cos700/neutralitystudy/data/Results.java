package ac.up.cos700.neutralitystudy.data;

import ac.up.cos700.neutralitystudy.data.util.GraphException;
import ac.up.cos700.neutralitystudy.data.util.ResultsException;
import ac.up.cos700.neutralitystudy.function.Function;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
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
     * @throws ResultsException
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
    
    /**
     * Appends the value(s) to a file with the relative path:
     * experimentName/resultType
     *
     * @param experimentName
     * @param resultType
     * @param values
     * @throws ResultsException
     */
    public synchronized static void writeToFile(String experimentName, String resultType, double[] values1, double[] values2) throws ResultsException {
        BufferedWriter writer = null;
        
        if (values1.length != values2.length)
            throw new ResultsException("The length of the two arrays do not correspond");
        
        try {
            File directory = new File(experimentName + "/" + resultType + ".csv");
            directory.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(directory, true));

            for (int i = 0; i < values1.length; i++) {
                writer.write(Double.toString(values1[i]) + ", " + values2[i]);
                writer.newLine();
            }
            
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

    public synchronized static void addPlot(String title, RealProblem problem)
            throws ResultsException {
        try {
            currentGraph.addPlot(title, problem);
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }
    
    public synchronized static void addPlot(String title, Function function, int lowerbound, int upperbound)
            throws ResultsException {
        try {
            currentGraph.addPlot(title, function, lowerbound, upperbound);
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }
    
    public synchronized static void addPlot(String title, double[] xData, double[] yData, String type)
            throws ResultsException {
        try {
            currentGraph.addPlot(title, xData, yData, type);
        }
        catch (GraphException e) {
            throw new ResultsException(e.getMessage());
        }
    }
    
    public synchronized static void addPlot(String title, double[][] xData, double[] yData, String type)
            throws ResultsException {
        
        try {
            currentGraph.addPlot(title, xData, yData, type);
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
