package fluent.ly;

import static java.lang.String.*;
import static java.util.stream.Collectors.*;

import static fluent.ly.English.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;

import org.eclipse.jdt.annotation.*;

/** Our way of dealing with logs, exceptions, NPE, Eclipse bugs, and other
 * unusual situations.
 * @author Yossi Gil
 * @since Nov 13, 2016 */
public interface note {
  class __ {
    static String trace(final Throwable ¢) {
      return separate.these(Stream.of(¢.getStackTrace()).map(StackTraceElement::toString).collect(toList())).by(";\n");
    }
    static String trace() {
      return trace(new Throwable());
    }
  }

  String FILE_SEPARATOR = "\n**\n";
  String FILE_SUB_SEPARATOR = "\n********\n";
  /** @formatter:off */
  Stack<Level> levels = new Stack<>();

  Logger logger = anonymous.ly(()->{
    final Logger $ = Logger.getGlobal();
    final ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL);
    $.addHandler(handler);
    return $;
  });

  static <@Nullable T> T bug() {
    return bug("");
  }

  static <@Nullable T> T bug(final Object instance) {
    return bug(//
        "Instance involved is %s\n" + //
            "toString() = \n",
        English.indefinite(instance), instance);
  }

  static <@Nullable T> T bug(final Object o, final Throwable t) {
    return nulling.ly(() -> logger.info(format(//
        "An instance of %s was hit by %s exception.\n" + //
            "This is an indication of a bug.\n", //
        English.name(o), English.indefinite(t) //
    ) + //
        format(" %s = '%s'\n", English.name(o), o) + //
        format(" %s = '%s'\n", English.name(t), t) + //
        format(" trace(%s) = '%s'\n", English.name(t), __.trace(t)) //
    ));
  }

  static <@Nullable T> T bug(final String format, final Object... os) {
    return nulling.ly(() -> logger.info(format(//
        "A bug was detected in the vicinty of %s; trace =%s\n",
        __.trace(),//
        system.myCallerFullClassName()) + //
        format(format, os)));
  }

  static <@Nullable T> T bug(final Throwable ¢) {
    return nulling.ly(() -> logger.info(//
        "A static method was hit by " + indefinite(¢) + " exception.\n" + //
            "This is an indication of a bug.\n" + //
            format("%s = '%s'\n", English.name(¢), ¢) + //
            format("trace(%s) = '%s'\n", English.name(¢), __.trace(¢)) //
    ));
  }
  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param ¢ JD */
  static <@Nullable T> T cancel(final Exception ¢) {
    return nulling.ly(() -> logger.info(//
        " " + English.name(¢) + //
            " (probably cancellation) exception." + //
            "\n x = '" + ¢ + "'" //
    ));
  }

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  static <@Nullable T> T cancel(final Object o, final Exception x) {
    return nulling.ly(() -> logger.info(//
        "An instance of " + English.name(o) + //
            "\n was hit by " + indefinite(x) + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'"));
  }

  static <@Nullable T> T ignore(final Class<?> o, final Throwable t) {
    return info(//
        "A static method of " + English.name(o) + //
            "was hit by " + indefinite(t) + "\n" + //
            "exception. This is expected and printed only for the purpose of debugging" + //
            "x = '" + t + "'" + //
            "o = " + o + "'");
  }

  static <@Nullable T> T ignore(final Object o, final Throwable t) {
    return info(//
        "An instance of " + English.name(o) + //
            "\n was hit by " + indefinite(t) + //
            " exception. This is expected and printed only for the purpose of debugging" + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  static <@Nullable T> T info(final String message) {
    return nulling.ly(() -> logger.info(message));
  }

  static <@Nullable T> T io(final Exception ¢) {
    return nulling.ly(() -> logger.config(//
        "   Got an exception of __ : " + English.name(¢) + //
            "\n      (probably I/O exception)" //
            + "\n   The exception says: '" + ¢ + "'" //
    ));
  }

  static <@Nullable T> T io(final Exception x, final String message) {
    return nulling.ly(() -> logger.info(//
        "   Got an exception of __ : " + English.name(x) + //
            "\n      (probably I/O exception)" + //
            "\n   The exception says: '" + x + "'" + //
            "\n   The associated message is " + //
            "\n       >>>'" + message + "'<<<" //
    ));
  }

  static <@Nullable T> T io(final IOException ¢) {
    return nulling.ly(() -> logger.info(//
        "   Got an exception of type : " + English.name(¢) + //
            "\n      (probably I/O exception)\n   The exception says: '" + ¢ + "'" //
    ));
  }

  /** logs an error in the plugin into an external file
   * @param tipper an error */
  static <@Nullable T> T logToFile(final Throwable t, final Object... os) {
    final StringWriter w = new StringWriter();
    t.printStackTrace(new PrintWriter(w));
    final Object[] nos = new Object[os.length + 2];
    System.arraycopy(os, 0, nos, 2, os.length);
    nos[0] = t + "";
    nos[1] = (w + "").trim();
    info(separate.these(nos).by(FILE_SUB_SEPARATOR)); //
    info(FILE_SEPARATOR);
    return the.nil();
  }
  static void set(final Level ¢) {
    levels.push(¢);
  }

  static void unset() {
    levels.pop();
  }
}
