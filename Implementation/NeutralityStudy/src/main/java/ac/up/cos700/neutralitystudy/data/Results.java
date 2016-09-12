package ac.up.cos700.neutralitystudy.data;

import ac.up.cos700.neutralitystudy.data.util.ResultsException;
import ac.up.cos700.neutralitystudy.function.IFunction;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import com.panayotis.gnuplot.GNUPlotException;
import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.ArrayDataSet;
import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.dataset.FileDataSet;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.plot.FunctionPlot;
import com.panayotis.gnuplot.plot.Plot;
import com.panayotis.gnuplot.style.PlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.FileTerminal;
import com.panayotis.iodebug.Debug;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    //todo: add graph path as parameter
    public synchronized static JavaPlot newGraph(String graphTitle, String xTitle, String yTitle)
            throws ResultsException {
        try {
            JavaPlot plotter = new JavaPlot(gnuplotPath);
            JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
            plotter.newGraph3D();
            plotter.set("pm3d", "");
            plotter.set("hidden3d", "");
            //todo: prefer a vector graphic, check with latex        
            plotter.setTerminal(new FileTerminal("png", graphTitle + ".png"));
            plotter.setTitle(graphTitle);
            plotter.getAxis("x").setLabel(xTitle);
            plotter.getAxis("y").setLabel(yTitle);
            return plotter;
        }
        catch (GNUPlotException e) {
            throw new ResultsException(e.getMessage());
        }
    }

    public synchronized static void addPlot(JavaPlot graph, String plotTitle)
            throws ResultsException {
        try {
            PlotStyle style = new PlotStyle(Style.LINES);
            DataSetPlot plot = new DataSetPlot(new FileDataSet(new File(tempGnuPlotData)));
            plot.setTitle(plotTitle);
            plot.setPlotStyle(style);

            graph.addPlot(plot);
        }
        catch (GNUPlotException | IOException e) {
            throw new ResultsException(e.getMessage());
        }
    }

    /**
     * Adds a plot to the given graph with the specified parameters. Assumes
     * that that lower and upper bounds apply to all dimensions resulting in a
     * plot that falls within a cube/square.
     *
     * @param graph
     * @param plotTitle
     * @param function
     * @param dimensions
     * @throws ResultsException
     */
    public synchronized static void addPlot(JavaPlot graph, String plotTitle, IFunction function, int dimensions)
            throws ResultsException {
        addPlot(graph, plotTitle, function, function.getLowerBound(), function.getUpperBound(), dimensions);
    }

    /**
     * Adds a plot to the given graph with the specified parameters. Assumes
     * that that lower and upper bounds apply to all dimensions resulting in a
     * plot that falls within a cube/square.
     *
     * @param graph
     * @param plotTitle
     * @param function
     * @param lowerbound
     * @param upperbound
     * @param dimensions
     * @throws ResultsException
     */
//    public synchronized static void addPlot(JavaPlot graph, String plotTitle, IFunction function, double lowerbound, double upperbound, int dimensions)
//            throws ResultsException {
//
//        if (lowerbound == Double.NEGATIVE_INFINITY
//                || upperbound == Double.POSITIVE_INFINITY)
//            throw new ResultsException("Cannot plot between infinite bounds.");
//
//        final int detail = 20;
//
//        double[][] points;
//
//        try {
//            if (dimensions == 2) {
//                points = new double[detail][2];
//                for (int i = 0; i <= detail; i++) {
//                    points[i][0] = lowerbound + i * (upperbound - lowerbound) / detail;
//                    points[i][1] = function.evaluate(points[i][0]);
//                }
//            }
//            else if (dimensions == 3) {
//                points = new double[detail*detail+detail+1][3];
//                for (int i = 0; i <= detail; i++) {  
//                    double x = lowerbound + i * (upperbound - lowerbound) / detail;
//                    for (int j = 0; j <= detail; j++) {                        
//                        points[detail * i + j][0] = x;
//                        points[detail * i + j][1] = lowerbound + j * (upperbound - lowerbound) / detail;
//                        points[detail * i + j][2] = function.evaluate(points[detail * i + j][0], points[detail * i + j][1]);
//                    }
//                }
//            }
//            else {
//                throw new ResultsException("Can only plot in 2 or 3 dimensions");
//            }
//
//        }
//        catch (UnequalArgsDimensionException e) {
//            throw new ResultsException(e.getMessage());
//        }
//
//        Results.addPlot(graph, plotTitle, points);
//    }
    public synchronized static void addPlot(JavaPlot graph, String plotTitle, IFunction function, double lowerbound, double upperbound, int dimensions)
            throws ResultsException {

        if (lowerbound == Double.NEGATIVE_INFINITY
                || upperbound == Double.POSITIVE_INFINITY)
            throw new ResultsException("Cannot plot between infinite bounds.");

        final int detail = 20;

        double[][] points;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempGnuPlotData, false));

            if (dimensions == 2) {
                points = new double[detail][2];
                for (int i = 0; i <= detail; i++) {
                    points[i][0] = lowerbound + i * (upperbound - lowerbound) / detail;
                    points[i][1] = function.evaluate(points[i][0]);
                }
            }
            else if (dimensions == 3) {
                points = new double[detail * detail + detail + 1][3];
                for (int i = 0; i <= detail; i++) {
                    double x = lowerbound + i * (upperbound - lowerbound) / detail;
                    for (int j = 0; j <= detail; j++) {
                        double y = lowerbound + j * (upperbound - lowerbound) / detail;
                        writer.write(Double.toString(x));
                        writer.write(" " + Double.toString(y));
                        writer.write(" " + Double.toString(function.evaluate(x, y)));
                        writer.newLine();
                        
                    }
                    writer.newLine();

                }
                writer.flush();
                writer.close();
            }
            else {
                throw new ResultsException("Can only plot in 2 or 3 dimensions");
            }
        }
        catch (UnequalArgsDimensionException | IOException e) {
            throw new ResultsException(e.getMessage());
        }

        Results.addPlot(graph, plotTitle);
    }

    /**
     * Merges 2 arrays to form a 2D array in the format that JavaPlot expects.
     *
     * @param x
     * @param y
     * @return 2D array of XY-points
     * @throws ResultsException
     */
    public synchronized static double[][] mergeVectors(double[] x, double[] y)
            throws ResultsException {

        if (x.length != y.length)
            throw new ResultsException("X and Y arrays must be of same length to merge.");

        double[][] points = new double[y.length][2];

        for (int i = 0; i < y.length; i++) {
            points[i][0] = x[i];
            points[i][1] = y[i];
        }

        return points;
    }

    /**
     * Merges 3 arrays to form a 2D array in the format that JavaPlot expects.
     *
     * @param x
     * @param y
     * @param z
     * @return 2D array of XYZ-points
     * @throws ResultsException
     */
    public synchronized static double[][] mergeVectors(double[] x, double[] y, double[] z)
            throws ResultsException {

        if (x.length != y.length || y.length != z.length)
            throw new ResultsException("X, Y and Z arrays must be of same length to merge.");

        double[][] points = new double[y.length][3];

        for (int i = 0; i < y.length; i++) {
            points[i][0] = x[i];
            points[i][1] = y[i];
            points[i][2] = z[i];
        }

        return points;
    }

    public static String gnuplotPath = "C:\\Program Files\\gnuplot\\bin\\gnuplot.exe";
    private static String tempGnuPlotData = "tempGNUPlotData.dat";
}
