package jetpackft.core.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * A Log class. The Log class handles logging and basic output to the console.
 * The Log class also writes contents to a log file for later inspection.
 */
public class Log {

    /**
     * The output directory of log data.
     */
    private File outputPath;
    /**
     * The path of the log file.
     */
    private File logPath;
    /**
     * Should this Log output to the console?
     */
    private boolean outputToConsole;

    /**
     * The minimum log level that can be displayed on the console.
     */
    private static final Level MIN_LOG_LEVEL = Level.FINE;

    /**
     * The Date on which this Log started.
     */
    private Date logStart = new Date();
    /**
     * The format of the log file name. (The log files are named by time.)
     */
    private static final DateFormat LOG_FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    /**
     * The format of the logging date. (Written on the start of log lines.)
     */
    private static final DateFormat LOGGING_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Creates a new Log.
     * @throws IOException If the user home directory cannot be found/the log cannot be created.
     */
    public Log() throws IOException {
        outputPath = new File(new File(System.getProperty("user.home")), ".jft");
        outputToConsole = true;
        validateOutputPath();
        createLog();
    }

    /**
     * Creates a new Log, with the option to output to console or not.
     * @param _outputToConsole Should the Log output to the console?
     * @throws IOException If the user home directory cannot be found/the log cannot be created.
     */
    public Log(boolean _outputToConsole) throws IOException {
        outputPath = new File(new File(System.getProperty("user.home")), ".jft");
        outputToConsole = _outputToConsole;
        validateOutputPath();
        createLog();
    }

    /**
     * Creates a new Log, with the option to set the output directory.
     * @param logPath The directory where the log will be written.
     * @throws IOException If the log directory cannot be found/the log cannot be created.
     */
    public Log(String logPath) throws IOException {
        outputPath = new File(logPath);
        outputToConsole = true;
        validateOutputPath();
        createLog();
    }

    /**
     * Creates a new log, with the option to output to console or not and
     * to set the output directory.
     * @param logPath The directory where the log will be written.
     * @param _outputToConsole Should the Log output to the console?
     * @throws IOException If the log directory cannot be found/the log cannot be created.
     */
    public Log(String logPath, boolean _outputToConsole) throws IOException {
        outputToConsole = _outputToConsole;
        outputPath = new File(logPath);
        validateOutputPath();
        createLog();
    }

    /**
     * Checks if the output path exists and is a directory.
     * @throws IOException If the output path could not be created/the output path is a file.
     */
    private void validateOutputPath() throws IOException {
        if (!outputPath.exists()) {
            if (!outputPath.mkdirs())
                throw new IOException("Could not create directories.");
            else if (outputPath.exists() && !outputPath.isDirectory())
                throw new IOException("Object is not a directory.");
        }
    }

    /**
     * Creates a new log file.
     */
    private void createLog() {
        logPath = new File(outputPath, LOG_FILE_DATE_FORMAT.format(logStart) + ".log");
        try {
            //noinspection ResultOfMethodCallIgnored
            logPath.createNewFile();
        } catch (IOException e) {
            appendToLog(Level.SEVERE, "Could not create log file.");
        }

        appendToLog(Level.INFO, "Log Manager", "Started logging: " + LOGGING_DATE_FORMAT.format(logStart));
    }

    /**
     * Append a message to the log.
     * @param level The log level of the message.
     * @param data The message contents.
     */
    @SuppressWarnings("WeakerAccess")
    public void appendToLog(Level level, String data) {
        try {
            String contents = "[" + LOGGING_DATE_FORMAT.format(new Date()) + "][" + level.getLocalizedName().toUpperCase() + "] " + data;
            if (outputToConsole && (level.intValue() > MIN_LOG_LEVEL.intValue()))
                System.out.println(contents);
            appendToFile(logPath, contents);
        } catch (IOException ignored) {
            if (outputToConsole && (level.intValue() > MIN_LOG_LEVEL.intValue()))
                System.out.println("[" + LOGGING_DATE_FORMAT.format(new Date()) + "][" + Level.SEVERE.getLocalizedName().toUpperCase() + "] Failed to output to log.");
        }
    }

    /**
     * Append a message to the log, with a special tag to indicate the logger's module.
     * @param level The log level of the message.
     * @param tag The tag of the logger. This should be the module or class name.
     * @param data The message contents.
     */
    public void appendToLog(Level level, String tag, String data) {
        try {
            String contents = "[" + LOGGING_DATE_FORMAT.format(new Date()) + "][" + level.getLocalizedName().toUpperCase() + "][" + tag + "] " + data;
            if (outputToConsole && (level.intValue() > MIN_LOG_LEVEL.intValue()))
                System.out.println(contents);
            appendToFile(logPath, contents);
        } catch (IOException ignored) {
            if (outputToConsole && (level.intValue() > MIN_LOG_LEVEL.intValue()))
                System.out.println("[" + LOGGING_DATE_FORMAT.format(new Date()) + "][" + Level.SEVERE.getLocalizedName().toUpperCase() + "][" + tag + "] Failed to output to log.");
        }
    }

    /**
     * Append a message to a file.
     * @param file The file to append to.
     * @param contents The contents to be appended to the file.
     * @throws IOException If the file cannot be written on.
     */
    private void appendToFile(File file, String contents) throws IOException {
        if (!file.canWrite())
            throw new IOException("File is not writable.");

        FileOutputStream fos = new FileOutputStream(file, true);
        byte[] out = (contents + "\n").getBytes();

        fos.write(out, 0, out.length);
        fos.flush();
        fos.close();
    }

}
