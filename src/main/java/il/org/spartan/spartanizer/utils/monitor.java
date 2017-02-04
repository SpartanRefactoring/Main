package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.utils.fault.*;

import java.io.*;
import java.text.*;
import java.util.*;

import il.org.spartan.*;
import org.jetbrains.annotations.NotNull;

/** Our way of dealing with logs, exceptions, NPE, Eclipse bugs, and other
 * unusual situations.
 * @year 2016
 * @author Yossi Gil
 * @since Nov 13, 2016 */
public enum monitor {
  /** Not clear why we need this */
  LOG_TO_STDOUT {
    @Override @NotNull public monitor debugMessage(final String message) {
      return info(message);
    }

    @Override @NotNull public monitor error(final String message) {
      System.out.println(message);
      return this;
    }
  },
  /** Log to external file if in debug mode, see issue #196 */
  LOG_TO_FILE {
    final String FILE_NAME = "logs" + File.separator + "log_spartan_" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".txt";
    final boolean FILE_EXISTING = new File("logs").exists();

    @Override @NotNull public monitor debugMessage(final String message) {
      return logToFile(message);
    }

    @Override @NotNull public monitor error(final String message) {
      return logToFile(message);
    }

    @NotNull monitor logToFile(final String s) {
      if (!FILE_EXISTING)
        return this;
      try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME, true), "utf-8"))) {
        w.write(s + "\n");
      } catch (@NotNull final IOException ¢) {
        log(¢);
      }
      return this;
    }
  },
  /** Used for real headless system; logs are simply ignore */
  OBLIVIOUS {
    @Override @NotNull public monitor error(@SuppressWarnings("unused") final String __) {
      return this;
    }
  },
  /** For release versions, we keep a log of errors in stderr, but try to
   * proceed */
  PRODUCTION {
    @Override @NotNull public monitor error(final String message) {
      System.err.println(message);
      return this;
    }
  },
  /** Used for debugging; program exits immediately with the first logged
   * message */
  SUPER_TOUCHY {
    @Override @NotNull public monitor debugMessage(final String message) {
      return info(message);
    }

    @Override @NotNull public monitor error(final String message) {
      System.err.println(message);
      System.exit(1);
      throw new RuntimeException(message);
    }
  },
  /** Used for debugging; program throws a {@link RuntimeException} with the
   * first logged message */
  TOUCHY {
    @Override @NotNull public monitor debugMessage(final String message) {
      return info(message);
    }

    @Override @NotNull public monitor error(final String message) {
      throw new RuntimeException(message);
    }
  };
  public static final String FILE_SUB_SEPARATOR = "\n------------------------------------------------------------------------------------------------------\n";
  public static final String FILE_SEPARATOR = "######################################################################################################";
  public static final monitor now = monitor.PRODUCTION;

  @NotNull public static String className(@NotNull final Class<?> ¢) {
    final String $ = ¢.getCanonicalName();
    return ¢.getSimpleName() + "[" + ($ == null ? ¢ : $) + "]";
  }

  @NotNull public static String className(@NotNull final Object ¢) {
    return className(¢.getClass());
  }

  public static void debug(@NotNull final Object o, @NotNull final Throwable t) {
    debug(//
        "An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  @NotNull public static monitor debug(final String message) {
    return now.debugMessage(message);
  }

  @NotNull public static monitor infoIOException(@NotNull final Exception x, final String message) {
    return now.info(//
        "   Got an exception of type : " + x.getClass().getSimpleName() + //
            "\n      (probably I/O exception)" + "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n       >>>'" + message + "'<<<" //
    );
  }

  /** logs an error in the plugin
   * @param tipper an error */
  public static void log(final Throwable ¢) {
    now.error(¢ + "");
  }

  /** logs an error in the plugin into an external file
   * @param tipper an error */
  public static void logToFile(@NotNull final Throwable t, @NotNull final Object... os) {
    final StringWriter w = new StringWriter();
    t.printStackTrace(new PrintWriter(w));
    final Object[] nos = new Object[os.length + 2];
    System.arraycopy(os, 0, nos, 2, os.length);
    nos[0] = t + "";
    nos[1] = (w + "").trim();
    LOG_TO_FILE.debugMessage(separate.these(nos).by(FILE_SUB_SEPARATOR)) //
        .debugMessage(FILE_SEPARATOR);
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  public static void logCancellationRequest(@NotNull final Object o, @NotNull final Exception x) {
    now.info(//
        "An instance of " + className(o) + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'");
  }

  @NotNull public static monitor logEvaluationError(@NotNull final Object o, @NotNull final Throwable t) {
    System.err.println(//
        dump() + //
            "\n An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            "\n      exeption, probably due to unusual Java constructs in the input:" + //
            "\n   x = '" + t + "'" + //
            "\n   o = " + o + "'" + //
            done(t) //
    );
    return now;
  }

  @NotNull public static monitor logEvaluationError(@NotNull final Throwable ¢) {
    return logEvaluationError(now, ¢);
  }

  public static void logProbableBug(@NotNull final Object o, @NotNull final Throwable t) {
    now.error(//
        "An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception, which may indicate a bug somwhwere." + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  @NotNull public abstract monitor error(String message);

  @NotNull public monitor info(final String message) {
    System.out.println(message);
    return this;
  }

  @NotNull monitor debugMessage(@SuppressWarnings("unused") final String __) {
    return this;
  }
}
