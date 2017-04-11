package il.org.spartan.utils;

import static il.org.spartan.utils.English.*;
import static il.org.spartan.utils.fault.*;
import static java.lang.String.*;

import static java.util.stream.Collectors.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;
import java.util.stream.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Our way of dealing with logs, exceptions, NPE, Eclipse bugs, and other
 * unusual situations.
 * @author Yossi Gil
 * @since Nov 13, 2016 */
public interface monitor {
  static <T> T bug() {
    return bug("");
  }

  static <T> T bug(final Object instance) {
    return bug(//
        "Instance involved is of class %s\n" + //
            "toString() = \n",
        English.name(instance), instance);
  }

  static <T> T bug(final Object o, final Throwable t) {
    return robust.nullify(() -> logger.info(format(//
        "An instance of %s was hit by %s exception.\n" + //
            "This is an indication of a bug.\n", //
        English.name(o), English.indefinite(t) //
    ) + //
        format(" %s = '%s'\n", English.name(o), o) + //
        format(" %s = '%s'\n", English.name(t), t) + //
        format(" trace(%s) = '%s'\n", English.name(t), trace(t)) //
    ));
  }

  static <T> T bug(final String format, final Object... os) {
    return robust.nullify(() -> logger.info(format(//
        "A bug was detected in the vicinty of %s\n", system.myCallerFullClassName()) + //
        format(format, os)));
  }

  static <T> T bug(final Throwable ¢) {
    return bug(logger, ¢);
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  static <T> T cancel(final Object o, final Exception x) {
    return robust.nullify(() -> logger.info(//
        "An instance of " + English.name(o) + //
            "\n was hit by " + indefinite(x) + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'"));
  }

  static ConsoleHandler consoleHandler() {
    final ConsoleHandler $ = new ConsoleHandler();
    $.setFormatter(new Formatter() {
      @Override @SuppressWarnings("boxing") public String format(final LogRecord ¢) {
        return String.format("%2d. %s %s#%s %s: %s\n", //
            ¢.getSequenceNumber(), //
            new SimpleDateFormat("yy-MM-dd hh:mm:ss").format(new Date(¢.getMillis())), //
            namer.lastComponent(¢.getSourceClassName()), //
            ¢.getSourceMethodName(), //
            ¢.getLevel(), //
            formatMessage(¢)//
        );
      }
    });
    return $;
  }

  static <T> T debug(final Class<?> o, final Throwable t) {
    return debug(//
        "A static method of " + English.name(o) + //
            "was hit by " + indefinite(t) + "\n" + //
            "exception. This is expected and printed only for the purpose of debugging" + //
            "x = '" + t + "'" + //
            "o = " + o + "'");
  }

  static <T> T debug(final Object o, final Throwable t) {
    return debug(//
        "An instance of " + English.name(o) + //
            "\n was hit by " + indefinite(t) + //
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  static <T> T debug(final String message) {
    return robust.nullify(() -> logger.info(message));
  }

  static <T> T infoIOException(final Exception ¢) {
    return robust.nullify(() -> logger.info(//
        "   Got an exception of type : " + English.name(¢) + //
            "\n      (probably I/O exception)" //
            + "\n   The exception says: '" + ¢ + "'" //
    ));
  }

  static <T> T infoIOException(final Exception x, final String message) {
    return robust.nullify(() -> logger.info(//
        "   Got an exception of type : " + English.name(x) + //
            "\n      (probably I/O exception)" + //
            "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n       >>>'" + message + "'<<<" //
    ));
  }

  static <T> T infoIOException(final IOException ¢) {
    return robust.nullify(() -> logger.info(//
        "   Got an exception of type : " + English.name(¢) + //
            "\n      (probably I/O exception)\n   The exception says: '" + ¢ + "'" //
    ));
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param ¢ JD */
  static <T> T logCancellationRequest(final Exception ¢) {
    return robust.nullify(() -> logger.info(//
        " " + English.name(¢) + //
            " (probably cancellation) exception." + //
            "\n x = '" + ¢ + "'" //
    ));
  }

  static <T> T logProbableBug(final Throwable ¢) {
    return robust.nullify(() -> logger.info(//
        "A static method was hit by " + indefinite(¢) + " exception.\n" + //
            "This is an indication of a bug.\n" + //
            format("%s = '%s'\n", English.name(¢), ¢) + //
            format("trace(%s) = '%s'\n", English.name(¢), trace(¢)) //
    ));
  }

  static <T> T logToFile(final String message) {
    try {
      if (MyLogger.writer() != null) {
        MyLogger.writer().write(message + "\n");
        MyLogger.writer().flush();
      }
    } catch (final IOException ¢) {
      ¢.printStackTrace();
    }
    return null¢();
  }

  /** logs an error in the plugin into an external file
   * @param tipper an error */
  static <T> T logToFile(final Throwable t, final Object... os) {
    final StringWriter w = new StringWriter();
    t.printStackTrace(new PrintWriter(w));
    final Object[] nos = new Object[os.length + 2];
    System.arraycopy(os, 0, nos, 2, os.length);
    nos[0] = t + "";
    nos[1] = (w + "").trim();
    debug(separate.these(nos).by(FILE_SUB_SEPARATOR)); //
    debug(FILE_SEPARATOR);
    return null¢();
  }

  static void main(final String[] args) {
    System.out.println(MyLogger.fileName());
  }

  static Logger makeLogger() {
    final Logger $ = Logger.getGlobal();
    for (final Handler ¢ : $.getHandlers())
      $.removeHandler(¢);
    for (final Handler ¢ : $.getParent().getHandlers())
      $.getParent().removeHandler(¢);
    $.addHandler(consoleHandler());
    return $;
  }

  static <T> T null¢() {
    return null;
  }

  static void set(final Level ¢) {
    levels.push(¢);
  }

  static String trace(final Throwable ¢) {
    return separate.these(Stream.of(¢.getStackTrace()).map(StackTraceElement::toString).collect(toList())).by(";\n");
  }

  static void unset() {
    levels.pop();
  }

  String FILE_SEPARATOR = "\n**\n";
  String FILE_SUB_SEPARATOR = "\n********\n";
  /** @formatter:off */
  Stack<Level> levels = new Stack<>();
  Logger logger = makeLogger();
  class MyLogger {
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
        return writer = writer != null ? writer : new BufferedWriter(new OutputStreamWriter(outputStream(), system.UTF_8));
      } catch (final UnsupportedEncodingException ¢) {
        assert fault.unreachable() : specifically(String.format("Encoding '%s' should not be invalid", system.UTF_8), //
            ¢, file, fileName, file(), fileName());
        return null;
      }
    }
    private static File file;
    private static String fileName;
    private static OutputStream outputStream;
    private static Writer writer;
  }
}
