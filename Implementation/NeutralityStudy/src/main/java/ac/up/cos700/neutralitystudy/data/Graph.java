package ac.up.cos700.neutralitystudy.data;

import ac.up.cos700.neutralitystudy.data.util.GraphException;
import ac.up.cos700.neutralitystudy.data.util.ResultsException;
import ac.up.cos700.neutralitystudy.function.IFunction;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Abrie van Aardt
 */
public class Graph {

    public class Plot {

        public String title;
        public String dataset;
    }

    public Graph(String _path, String _title, String xLabel, String yLabel, String zLabel, int _dimensions) throws GraphException {
        try {
            gnuPlotProcess = Runtime.getRuntime().exec("gnuplot --persist");
            gnuPlotTerminal = new BufferedWriter(new OutputStreamWriter(gnuPlotProcess.getOutputStream()));
            //ensure the directory where the graph will reside, exists
            Files.createDirectories(Paths.get(_path, _title));
        }
        catch (IOException e) {
            throw new GraphException(e.getMessage());
        }

        path = _path;
        title = _title;
        properties = new ArrayList<>(10);
        plots = new ArrayList<>();
        plotCommand = "";

        dimensions = _dimensions;

        //set some graph properties
        //Terminal
        properties.add("set terminal postscript eps enhanced color font 'Helvetica,10'");
        //Output
        properties.add("set output '" + ".\\" + path + "\\" + title + "\\" + title + ".eps'");
        //Title
        properties.add("set title '" + title + "'");

        //XLabel
        properties.add("set xlabel '" + xLabel + "'");

        //YLabel
        properties.add("set ylabel '" + yLabel + "'");

        //Move key outside graph
        //properties.add("set key outside");
        switch (dimensions) {
            case 2:
                break;
            case 3:
                //ZLabel
                properties.add("set zlabel '" + zLabel + "'");
                properties.add("set pm3d");
                properties.add("set hidden3d");
                properties.add("unset colorbox");
                properties.add("set palette grey");
                properties.add("unset surface");
                break;
            default:
                throw new GraphException("Can only plot in 2 or 3 dimensions");
        }

    }

    public void addPlot(String _title, IFunction function)
            throws GraphException {
        addPlot(_title, function, function.getLowerBound(), function.getUpperBound());
    }

    public void addPlot(String _title, IFunction function, double lowerbound, double upperbound)
            throws GraphException {

        if (lowerbound == Double.NEGATIVE_INFINITY
                || upperbound == Double.POSITIVE_INFINITY)
            throw new GraphException("Cannot plot between infinite bounds.");

        Plot plot = new Plot();
        plot.title = _title;
        plot.dataset = "'.\\" + path + "\\" + title + "\\" + _title + ".dat'";

        final int detail = 1000;

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(path + "\\" + title + "\\" + _title + ".dat", false));

            if (dimensions == 2) {
                for (int i = 0; i <= detail; i++) {
                    double x = lowerbound + i * (upperbound - lowerbound) / detail;
                    double y = function.evaluate(x);
                    writer.write(Double.toString(x));
                    writer.write(" " + Double.toString(y));
                    writer.newLine();
                }
            }
            else if (dimensions == 3) {
                for (int i = 0; i <= detail; i++) {
                    double x = lowerbound + i * (upperbound - lowerbound) / detail;
                    for (int j = 0; j <= detail; j++) {
                        double y = lowerbound + j * (upperbound - lowerbound) / detail;
                        writer.write(Double.toString(x));
                        writer.write(" " + Double.toString(y));
                        writer.write(" " + Double.toString(function.evaluate(x, y)));
                        writer.newLine();
                    }
                    writer.newLine();//next datablock
                }
            }

            writer.flush();
            writer.close();
            plots.add(plot);
        }
        catch (UnequalArgsDimensionException | IOException e) {
            throw new GraphException(e.getMessage());
        }

    }

    public void addPlot(String _title, double[] xData, double[] yData)
            throws GraphException {

        Plot plot = new Plot();
        plot.title = _title;
        plot.dataset = "'.\\" + path + "\\" + title + "\\" + _title + ".dat'";

        if (xData.length != yData.length)
            throw new GraphException("X and Y arrays must be of same length to plot.");

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(path + "\\" + title + "\\" + _title + ".dat", false));

            for (int i = 0; i < xData.length; i++) {
                writer.write(Double.toString(xData[i]));
                writer.write(" " + Double.toString(yData[i]));
                writer.newLine();
            }

            writer.flush();
            writer.close();
            plots.add(plot);
        }
        catch (IOException e) {
            throw new GraphException(e.getMessage());
        }
    }

    public void plot() throws GraphException {

        if (dimensions == 2) {
            plotCommand = "plot";
        }
        else {
            plotCommand = "splot";
        }

        for (Plot plot : plots) {
            plotCommand += " " + plot.dataset + " title '" + plot.title + "' with lines,";
        }

        try {
            for (String property : properties) {
                gnuPlotTerminal.write(property);
                gnuPlotTerminal.newLine();
            }

            gnuPlotTerminal.write(plotCommand);
            gnuPlotTerminal.newLine();
            gnuPlotTerminal.flush();
        }
        catch (IOException e) {
            throw new GraphException("There was an error communicating with GNUPlot: " + e.getMessage());
        }

        plotCommand = "";
    }

    public List<String> properties;

    private int dimensions;
    private String path;
    private String title;
    private List<Plot> plots;
    private String plotCommand;
    private final transient Process gnuPlotProcess;
    private final transient BufferedWriter gnuPlotTerminal;

}
