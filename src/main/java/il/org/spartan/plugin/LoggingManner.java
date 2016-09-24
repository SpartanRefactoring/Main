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
}
