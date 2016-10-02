package il.org.spartan.plugin;

import org.eclipse.core.runtime.*;

import il.org.spartan.spartanizer.tipping.*;

/** Our way of dealing with logs, exceptions, NPE, Eclipse bugs, and other
 * unusual situations.
 * @author Yossi Gil
 * @year 2016 */
public enum monitor {
  /** Not clear why we need this */
  LOG_TO_STDOUT {
    @Override public monitor debugMessage(final String message) {
      return info(message);
    }

    @Override public monitor error(final String message) {
      System.out.println(message);
      return this;
    }
  },
  /** Used for real headless run; logs are simply ignore */
  OBLIVIOUS {
    @Override public monitor error(@SuppressWarnings("unused") final String __) {
      return this;
    }
  },
  /** For release versions, we keep a log of errors in stderr, but try to
   * proceed */
  PRODUCTION {
    @Override public monitor error(final String message) {
      System.err.println(message);
      return this;
    }
  },
  /** Used for debugging; program exits immediately with the first logged
   * message */
  SUPER_TOUCHY {
    @Override public monitor debugMessage(final String message) {
      return info(message);
    }

    @Override public monitor error(final String message) {
      System.err.println(message);
      System.exit(1);
      throw new RuntimeException(message);
    }
  },
  /** Used for debugging; program throws a {@link RuntimeException} with the
   * first logged message */
  TOUCHY {
    @Override public monitor debugMessage(final String message) {
      return info(message);
    }

    @Override public monitor error(final String message) {
      throw new RuntimeException(message);
    }
  };
  public static final monitor now = monitor.PRODUCTION;

  public static String className(final Class<?> ¢) {
    return ¢.getSimpleName() + "[" + ¢.getCanonicalName() + "]";
  }

  public static String className(final Object ¢) {
    return className(¢.getClass());
  }

  public static void debug(Object o, Throwable t) {
    debug(//
        "An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  /** @param string
   * @return */
  public static monitor debug(final String message) {
    return now.debugMessage(message);
  }

  public static monitor infoIOException(final Exception x, final String message) {
    return now.info(//
        "   Got an exception of type : " + x.getClass().getSimpleName() + //
            "\n      (probably I/O exception)" + "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n        >>>'" + message + "'<<<" //
    );
  }

  /** logs an error in the plugin
   * @param tipper an error */
  public static void log(final Throwable ¢) {
    now.error(¢ + "");
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  public static void logCancellationRequest(final Object o, final Exception x) {
    now.info(//
        "An instance of " + className(o) + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'");
  }

  public static monitor logEvaluationError(final Object o, final Throwable x) {
    System.err.println(//
        "An instance of " + className(o) + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            "\n      exeption, probably due to unusual " + //
            "\n      Java constructs in the input " + //
            "\n   x = '" + x + "'" + //
            "\n   o = " + o + "'");
    return now;
  }

  public static monitor logEvaluationError(Throwable t) {
    return logEvaluationError(now, t);
  }

  public static void logProbableBug(final Object o, final Throwable t) {
    now.error(//
        "An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception, which may indicate a bug somwhwere." + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  public abstract monitor error(String message);

  public monitor info(final String message) {
    System.out.println(message);
    return this;
  }

  monitor debugMessage(@SuppressWarnings("unused") final String __) {
    return this;
  }
}
