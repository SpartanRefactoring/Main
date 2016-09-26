package il.org.spartan.plugin;

/** Our way of dealing with logging,
 * @author Yossi Gil
 * @year 2016 */
public enum LoggingManner {
  ABORT_ON_ERROR {
    @Override public LoggingManner log(final String message) {
      throw new RuntimeException(message);
    }
  },
  IGNORE {
    @Override public LoggingManner log(@SuppressWarnings("unused") final String __) {
      return this;
    }
  },
  LOG_TO_STDERR {
    @Override public LoggingManner log(final String message) {
      System.err.println(message);
      return this;
    }
  },
  LOG_TO_STDOUT {
    @Override public LoggingManner log(final String message) {
      System.out.println(message);
      return this;
    }
  };
  public static final LoggingManner now = ABORT_ON_ERROR;

  public static String dump() {
    return "MISSING_CASE: ";
  }

  public static String endDump() {
    return "\n-----this is all I know.";
  }

  public static LoggingManner info(final String message) {
    return nonAbortingManner().log(message);
  }

  public static LoggingManner infoIOException(final Exception x, final String message) {
    return info(//
        "   Got an exception of type : " + x.getClass().getSimpleName() + //
            "\n      (probably I/O exception)" + "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n        >>>'" + message + "'<<<" //
    );
  }

  /** logs an error in the plugin
   * @param t an error */
  public static void log(final Throwable ¢) {
    now.log(¢ + "");
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  public static void logCancellationRequest(final Object o, final Exception x) {
    now.log(//
        "An instane of " + o.getClass().getSimpleName() + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'");
  }

  public static void logEvaluationError(final Object o, final Exception x) {
    System.err.println(//
        "An instane of " + o.getClass().getSimpleName() + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            "\n      exeption, probably due to unusual " + //
            "\n      Java constructs in the input " + //
            "\n   x = '" + x + "'" + //
            "\n   o = " + o + "'");
  }

  public static void logProbableBug(final Object o, final Throwable t) {
    now.log(//
        "An instane of " + o.getClass().getSimpleName() + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception, which may indicate a bug somwhwere." + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  public static LoggingManner nonAbortingManner() {
    return now != ABORT_ON_ERROR ? now : LOG_TO_STDOUT;
  }

  public abstract LoggingManner log(String message);
}
