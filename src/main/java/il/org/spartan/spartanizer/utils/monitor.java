package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.cmdline.system.*;
import static il.org.spartan.spartanizer.engine.nominal.English.*;
import static il.org.spartan.spartanizer.utils.fault.*;
import static java.lang.String.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Our way of dealing with logs, exceptions, NPE, Eclipse bugs, and other
 * unusual situations.
 * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
 * @since Nov 13, 2016 */
public enum monitor {
  /** Log to external file if in debug mode, see issue #196 */
  LOG_TO_FILE {
    @Override public <T> T debugMessage(final String message) {
      return logToFile(message);
    }

    @Override public <T> T error(final String message) {
      return logToFile(message);
    }

    <T> T logToFile(final String message) {
      try {
        if (Logger.writer() != null) {
          Logger.writer().write(message + "\n");
          Logger.writer().flush();
        }
      } catch (final IOException ¢) {
        ¢.printStackTrace();
      }
      return null¢();
    }
  },
  INTERACTIVE_TDD {
    @Override public <T> T debugMessage(final String message) {
      return info(message);
    }

    @Override public <T> T error(final String message) {
      System.err.println(message);
      return null¢();
    }
  },
  /** Used for real headless system; logs are simply ignored */
  OBLIVIOUS {
    @Override public <T> T error(final String message) {
      return null¢(message);
    }
  },
  /** For release versions, we keep a log of errors in stderr, but try to
   * proceed */
  PRODUCTION {
    @Override public <T> T error(final String message) {
      System.err.println(message);
      return null¢();
    }
  },
  /** Used for debugging; program exits immediately with the first logged
   * message */
  SUPER_TOUCHY {
    @Override public <T> T debugMessage(final String message) {
      return info(message);
    }

    @Override public <T> T error(final String message) {
      System.err.println(message);
      System.exit(1);
      throw new RuntimeException(message);
    }
  },
  /** Used for debugging; program throws a {@link RuntimeException} with the
   * first logged message */
  TOUCHY {
    @Override public <T> T debugMessage(final String message) {
      return info(message);
    }

    @Override public <T> T error(final String message) {
      throw new RuntimeException(message);
    }
  };
  public static final String FILE_SEPARATOR = "######################################################################################################";
  public static final String FILE_SUB_SEPARATOR = "\n------------------------------------------------------------------------------------------------------\n";

  public static <T> T debug(final Class<?> o, final Throwable t) {
    return debug(//
        "A static method of " + system.className(o) + //
            "was hit by " + indefinite(t) + "\n" + //
            "exception. This is expected and printed only for the purpose of debugging" + //
            "x = '" + t + "'" + //
            "o = " + o + "'");
  }

  public static <T> T debug(final Object o, final Throwable t) {
    return debug(//
        "An instance of " + system.className(o) + //
            "\n was hit by " + indefinite(t) + //
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  public static <T> T debug(final String message) {
    return now().debugMessage(message);
  }

  public static <T> T infoIOException(final Exception ¢) {
    return now().info(//
        "   Got an exception of type : " + system.className(¢) + //
            "\n      (probably I/O exception)" //
            + "\n   The exception says: '" + ¢ + "'" //
    );
  }

  public static <T> T infoIOException(final Exception x, final String message) {
    return now().info(//
        "   Got an exception of type : " + system.className(x) + //
            "\n      (probably I/O exception)" + //
            "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n       >>>'" + message + "'<<<" //
    );
  }

  public static <T> T infoIOException(final IOException ¢) {
    return now().info(//
        "   Got an exception of type : " + system.className(¢) + //
            "\n      (probably I/O exception)\n   The exception says: '" + ¢ + "'" //
    );
  }

  /** logs an error in the plugin
   * @param tipper an error */
  public static <T> T log(final Throwable ¢) {
    return now().error(¢ + "");
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param ¢ JD */
  public static <T> T logCancellationRequest(final Exception ¢) {
    return now().info(//
        " " + system.className(¢) + //
            " (probably cancellation) exception." + //
            "\n x = '" + ¢ + "'" //
    );
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  public static <T> T logCancellationRequest(final Object o, final Exception x) {
    return now().info(//
        "An instance of " + system.className(o) + //
            "\n was hit by " + indefinite(x) + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'");
  }

  public static <T> T logEvaluationError(final Object o, final Throwable t) {
    System.err.println(//
        dump() + //
            "An instance of " + system.className(o) + "\n" + //
            "\n was hit by " + indefinite(t) + //
            "\n      exeption, probably due to unusual Java constructs in the input:" + //
            "\n   x = '" + t + "'" + //
            "\n   o = " + o + "'" + //
            done(t) //
    );
    return null¢();
  }

  public static <T> T logEvaluationError(final Throwable ¢) {
    return logEvaluationError(now(), ¢);
  }

  public static <T> T logProbableBug(final Object o, final Throwable t) {
    return now().error(format(//
        "An instance of %s was hit by %s exception.\n" + //
            "This is an indication of a bug.\n", //
        indefinite(o), indefinite(t) //
    ) + //
        format(" %s = '%s'\n", className(o), o) + //
        format(" %s = '%s'\n", className(t), t) + //
        format(" trace(%s) = '%s'\n", className(t), trace(t)) //
    );
  }

  private static String trace(final Throwable ¢) {
    return separate.these(Stream.of(¢.getStackTrace()).map(StackTraceElement::toString).collect(toList())).by(";\n");
  }

  public static <T> T logProbableBug(final Throwable ¢) {
    return now().error(//
        "A static method was hit by " + indefinite(¢) + " exception.\n" + //
            "This is an indication of a bug.\n" + //
            format("%s = '%s'\n", className(¢), ¢) + //
            format("trace(%s) = '%s'\n", className(¢), trace(¢)) //
    );
  }

  /** logs an error in the plugin into an external file
   * @param tipper an error */
  public static void logToFile(final Throwable t, final Object... os) {
    final StringWriter w = new StringWriter();
    t.printStackTrace(new PrintWriter(w));
    final Object[] nos = new Object[os.length + 2];
    System.arraycopy(os, 0, nos, 2, os.length);
    nos[0] = t + "";
    nos[1] = (w + "").trim();
    LOG_TO_FILE.debugMessage(separate.these(nos).by(FILE_SUB_SEPARATOR)); //
    LOG_TO_FILE.debugMessage(FILE_SEPARATOR);
  }

  public static void main(final String[] args) {
    System.out.println(Logger.fileName());
  }

  public static <T> T null¢(@SuppressWarnings("unused") final Object... __) {
    return null;
  }

  @SuppressWarnings("static-method") <T> T debugMessage(final String message) {
    return null¢(message);
  }

  public abstract <T> T error(String message);

  @SuppressWarnings("static-method") public <T> T info(final String message) {
    System.err.println(message);
    return null¢();
  }

  private static class Logger {
    private static final String UTF_8 = "utf-8";
    private static OutputStream outputStream;
    private static String fileName;
    private static File file;
    private static Writer writer;

    public static File file() {
      return file = file != null ? file : new File(fileName());
    }

    public static String fileName() {
      if (fileName != null)
        return fileName;
      fileName = system.tmp + "spartanizer" + new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".txt";
      System.out.flush();
      System.err.flush();
      System.err.format("\n --- Your secret log file will be found in \n\t%s\n", fileName);
      System.out.flush();
      return fileName;
    }

    public static OutputStream outputStream() {
      try {
        return outputStream = outputStream != null ? outputStream : new FileOutputStream(file(), true);
      } catch (@SuppressWarnings("unused") final FileNotFoundException __) {
        try {
          return outputStream = new FileOutputStream(fileName(), true);
        } catch (@SuppressWarnings("unused") final FileNotFoundException ___) {
          return outputStream = null;
        }
      }
    }

    public static Writer writer() {
      if (outputStream() == null)
        return null;
      try {
        return writer = writer != null ? writer : new BufferedWriter(new OutputStreamWriter(outputStream(), UTF_8));
      } catch (final UnsupportedEncodingException ¢) {
        assert fault.unreachable() : specifically(String.format("Encoding '%s' should not be invalid", UTF_8), //
            ¢, file, fileName, file(), fileName());
        return null;
      }
    }
  }

  private static final Stack<monitor> states = new Stack<>();

  public static void set(final monitor interactiveTdd) {
    states.push(interactiveTdd);
  }

  public static monitor now() {
    return !states.empty() ? states.peek() : monitor.PRODUCTION;
  }

  public static monitor unset() {
    return states.pop();
  }
}
