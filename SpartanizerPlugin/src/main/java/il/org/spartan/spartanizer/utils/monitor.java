package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.utils.fault.*;

import java.io.*;
import java.text.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Our way of dealing with logs, exceptions, NPE, Eclipse bugs, and other
 * unusual situations.
 * @author Yossi Gil
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
        if (Logger.writer() != null)
          Logger.writer().write(message + "\n");
      } catch (@SuppressWarnings("unused") final IOException __) {
        // Ignore exceptions since we cannot log them
      }
      return null¢();
    }
  },
  /** Not clear why we need this */
  LOG_TO_STDOUT {
    @Override public <T> T debugMessage(final String message) {
      return info(message);
    }

    @Override public <T> T error(final String message) {
      System.out.println(message);
      return null¢();
    }
  },
  /** Used for real headless system; logs are simply ignore */
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
  public static monitor now = monitor.LOG_TO_FILE;

  public static String className(final Class<?> ¢) {
    final String $ = ¢.getCanonicalName();
    return ¢.getSimpleName() + "[" + ($ == null ? ¢ : $) + "]";
  }

  public static String className(final Object ¢) {
    return className(¢.getClass());
  }

  public static <T> T debug(final Class<?> o, final Throwable t) {
    return debug(//
        "A static method of " + className(o) + //
            "\n was hit by a " + className(t.getClass()) + // 
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }


  public static <T> T debug(final Object o, final Throwable t) {
    return debug(//
        "An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  public static <T> T debug(final String message) {
    return now.debugMessage(message);
  }

  public static <T> T infoIOException(final Exception ¢) {
    return now.info(//
        "   Got an exception of type : " + className(¢) + //
            "\n      (probably I/O exception)" //
            + "\n   The exception says: '" + ¢ + "'" //
    );
  }

  public static <T> T infoIOException(final Exception x, final String message) {
    return now.info(//
        "   Got an exception of type : " + className(x) + //
            "\n      (probably I/O exception)" + //
            "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n       >>>'" + message + "'<<<" //
    );
  }

  public static <T> T infoIOException(final IOException ¢) {
    return now.info(//
        "   Got an exception of type : " + className(¢) + //
            "\n      (probably I/O exception)" + "\n   The exception says: '" + ¢ + "'" //
    );
  }

  /** logs an error in the plugin
   * @param tipper an error */
  public static <T> T log(final Throwable ¢) {
    return now.error(¢ + "");
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param ¢ JD */
  public static <T> T logCancellationRequest(final Exception ¢) {
    return now.info(//
        " " + className(¢) + //
            " (probably cancellation) exception." + //
            "\n x = '" + ¢ + "'" //
    );
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  public static <T> T logCancellationRequest(final Object o, final Exception x) {
    return now.info(//
        "An instance of " + className(o) + //
            "\n was hit by a " + className(x) + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'");
  }

  public static <T> T logEvaluationError(final Object o, final Throwable t) {
    System.err.println(//
        dump() + //
            "\n An instance of " + className(o) + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            "\n      exeption, probably due to unusual Java constructs in the input:" + //
            "\n   x = '" + t + "'" + //
            "\n   o = " + o + "'" + //
            done(t) //
    );
    return null¢();
  }

  public static <T> T logEvaluationError(final Throwable ¢) {
    return logEvaluationError(now, ¢);
  }

  public static <T> T logProbableBug(final Object o, final Throwable t) {
    return now.error(//
        "An instance of " + className(o) + //
            "\n was hit by a " + className(t) + //
            " exception, which may indicate a bug somwhwere." + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  public static <T> T logProbableBug(final Throwable x) {
    return now.error(//
        "A static method was hit by a " + className(x) + //
            " exception, which may indicate a bug somwhwere." + //
            "\n x = '" + x + "'" //
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
    System.out.println(message);
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
      fileName = system.tmp() + File.separator + "spartanizer"
          + new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".txt";
      System.out.flush();
      System.err.flush();
      System.err.format("\n --- Your error log will be found in \n\t%s\n", fileName);
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
}
