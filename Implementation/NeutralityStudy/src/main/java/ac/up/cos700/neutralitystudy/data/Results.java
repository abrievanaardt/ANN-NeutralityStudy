package ac.up.cos700.neutralitystudy.data;

import ac.up.cos700.neutralitystudy.function.IFunction;
import ac.up.cos700.neutralitystudy.util.UnequalArgsDimensionException;
import com.panayotis.gnuplot.GNUPlotException;
import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.plot.Graph;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
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

    public synchronized static void plot(String graphTitle, String plotTitle, String xTitle, String yTitle, double[][] values)
            throws GNUPlotException {

        JavaPlot plotter = new JavaPlot(gnuplotPath);
        plotter.setTitle(graphTitle);
        plotter.getAxis("x").setLabel(xTitle);
        plotter.getAxis("y").setLabel(yTitle);

        PlotStyle style = new PlotStyle(Style.LINES);

        DataSetPlot plot = new DataSetPlot(values);
        plot.setTitle(plotTitle);
        plot.setPlotStyle(style);

        plotter.addPlot(plot);
        plotter.plot();

    }

    public synchronized static void plot(IFunction function, String graphTitle, String plotTitle, String xTitle, String yTitle, double[] x)
            throws UnequalArgsDimensionException {

        //2D
        double[][] points = new double[x.length][2];

        for (int i = 0; i < points.length; i++) {
            points[i][0] = x[i];
            points[i][1] = function.evaluate(x[i]);
        }

        plot(graphTitle, plotTitle, xTitle, yTitle, points);
    }

    public static String gnuplotPath = "C:\\Program Files\\gnuplot\\bin\\gnuplot.exe";

}
