package ac.up.cos700.neutralitystudy.data.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author Abrie van Aardt
 */
public class StudyLogFormatter extends Formatter {

    private String formatString = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %2$-7s %3$-30s %4$s %5$s%n";

    public synchronized String format(LogRecord record) {
        Date date = new Date();
        date.setTime(record.getMillis());

        String sourceClassName = record.getSourceClassName();

        if (sourceClassName == null)
            sourceClassName = record.getLoggerName();

        sourceClassName = sourceClassName.substring(sourceClassName.lastIndexOf('.') + 1);
        String sourceMethodName = record.getSourceMethodName();
        String logLevel = record.getLevel().getLocalizedName();
        String message = formatMessage(record);
        String thrown = "";

        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                thrown = System.lineSeparator() + sw.toString();
            }
            catch (Exception ex) {
            }
        }

        String formattedLogEntry = String.format(formatString,
                date,
                logLevel,
                sourceClassName + "." + sourceMethodName,
                message,
                thrown);

        return formattedLogEntry;
    }
}
