package il.org.spartan.Leonidas.plugin.utils.logging;

import org.jetbrains.annotations.Nullable;

/**
 * Used as a frontend for the logging tool.
 * TODO @Team8 we need a way (preferably a gui) to control the settings of the logging tool, like default log level, log path, etc.
 *
 * @author RoeiRaz
 * @since 31-3-17
 */
@SuppressWarnings({"SameParameterValue", "unused"}) // These warning are suppressed because as a frontend,
// its ok for methods to be public and unused.
public class Logger {

    private static final LEVEL defaultLogLevel = LEVEL.INFO;
    private final Class<?> clz;
    private LEVEL logLevel;

    /**
     * @param clz - the class from which the logger will be used.
     */
    @SuppressWarnings("WeakerAccess")
    public Logger(Class<?> clz) {
        this.clz = clz;
        this.logLevel = defaultLogLevel;
    }

    /**
     * Logs a debug entry. Good for logs that should normally not be printed, but are useful for debugging.
     *
     * @param msg - the log message.
     */
    @SuppressWarnings("WeakerAccess")
    public void debug(String msg) {
        log(new BasicLogEntry(LEVEL.DEBUG, this.clz, msg));
    }

    /**
     * Logs an info entry. Used to log major landmarks in the application execution.
     *
     * @param msg - the log message.
     */
    @SuppressWarnings("WeakerAccess")
    public void info(String msg) {
        log(new BasicLogEntry(LEVEL.INFO, this.clz, msg));
    }

    /**
     * Logs a warning entry.
     *
     * @param msg - the log message.
     */
    @SuppressWarnings("WeakerAccess")
    public void warn(String msg) {
        warn(msg, null);
    }

    /**
     * Logs a warning entry.
     *
     * @param msg - the log message.
     * @param t   - a Throwable that caused the warning. (nullable)
     */
    @SuppressWarnings("WeakerAccess")
    public void warn(String msg, @Nullable Throwable t) {
        log(new BasicLogEntry(LEVEL.WARN, this.clz, msg, t));
    }

    /**
     * Logs an error.
     *
     * @param msg - the log message.
     */
    @SuppressWarnings("WeakerAccess")
    public void error(String msg) {
        error(msg, null);
    }


    /**
     * Logs an error.
     *
     * @param msg - the log message.
     * @param t   - a Throwable that caused the error. (nullable)
     */
    @SuppressWarnings("WeakerAccess")
    public void error(String msg, @Nullable Throwable t) {
        log(new BasicLogEntry(LEVEL.ERROR, this.clz, msg, t));
    }

    /**
     * Get the log level of this logger.
     * The log level affects which logs will be printed and which will be ignored.
     * The order of log importance is: ERROR > WARN > INFO > DEBUG. For example, if the
     * log level is INFO, DEBUG logs will be ignored.
     * NOLOG level means that no logs whatsoever will be printed.
     *
     * @return The current log level.
     */
    @SuppressWarnings("WeakerAccess")
    public LEVEL getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the log level of the logger.
     *
     * @param logLevel - updated logLevel
     */
    public void setLogLevel(LEVEL logLevel) {
        this.logLevel = logLevel;
    }

    private void log(BasicLogEntry e) {
        if (e.getLogLevel().greaterOrEqual(this.getLogLevel()))
            LogFileUtils.appendToLogFile(e.toString());
    }

    public enum LEVEL {
        DEBUG(0),
        INFO(1),
        WARN(2),
        ERROR(3),
        @SuppressWarnings("unused")NOLOG(4);

        private final int level;

        LEVEL(int level) {
            this.level = level;
        }

        /**
         * get the log level as a comparable type.
         *
         * @return the log level as a comparable type.
         */
        int getLevel() {
            return level;
        }

        /**
         * Log level is greater or equal than
         *
         * @param logLevel the log level to check against
         */
        boolean greaterOrEqual(LEVEL logLevel) {
            return getLevel() >= logLevel.getLevel();
        }

        @Override
        public String toString() {
            return "[" + this.name() + "]";
        }
    }
}