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
    @Override public LoggingManner log(@SuppressWarnings("unused") final String message) {
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

  public final LoggingManner info(final String message) {
    try {
      log(message);
    } catch (final Throwable t) {
      // ignore
    }
    return this;
  }

  public abstract LoggingManner log(String message);

  /** logs an error in the plugin
   * @param t an error */
  public static void xlog(final Throwable ¢) {
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
}
