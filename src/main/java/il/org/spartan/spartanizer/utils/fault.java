package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

/** Fluent API
 * @author Yossi Gil
 * @since 2016 */
public interface fault {
  static String done() {
    return done(stackCapture());
  }

  static String done(final Throwable ¢) {
    return "\n   Stack trace: [[[.................\n\n" + //
        trace(¢) + //
        "\n   END stack trace: .................]]]" + //
        "\n-----this is all I know.";
  }

  static Throwable stackCapture() {
    return new AssertionError();
  }

  static String trace() {
    return trace(stackCapture());
  }

  static String trace(final Throwable ¢) {
    final ByteArrayOutputStream $ = new ByteArrayOutputStream();
    ¢.printStackTrace(new PrintStream($));
    return new String($.toByteArray(), StandardCharsets.UTF_8);
  }

  static String dump() {
    return dump("");
  }

  static String dump(final String specfically) {
    return "\n FAULT: this should not have happened!" + specfically + "\n-----To help you fix the code, here is some info";
  }

  static boolean unreachable() {
    return false;
  }

  static String specifically(final String explanation, final Object... os) {
    return dump("\n " + explanation) + Arrays.asList(os).stream().map(¢ -> dump(¢.getClass().getSimpleName(), ¢)).reduce((x, y) -> x + y).get()
        + done();
  }

  static String dump(final String name, final Object value) {
    return "\n " + name + "=[" + value + "]";
  }
}
