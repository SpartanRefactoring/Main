package il.org.spartan.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.*;
import java.util.stream.*;

/** Fluent API
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public interface fault {
  @NotNull static String done() {
    return done(stackCapture());
  }

  @NotNull static String done(@NotNull final Throwable ¢) {
    return "\n   Stack trace: [[[.................\n\n" + //
        trace(¢) + //
        "\n   END stack trace: .................]]]" + //
        "\n-----this is all I know.";
  }

  @NotNull static Throwable stackCapture() {
    return new AssertionError();
  }

  @NotNull static String trace() {
    return trace(stackCapture());
  }

  @NotNull static String trace(@NotNull final Throwable ¢) {
    @NotNull final ByteArrayOutputStream $ = new ByteArrayOutputStream();
    ¢.printStackTrace(new PrintStream($));
    return new String($.toByteArray(), StandardCharsets.UTF_8);
  }

  @NotNull static String dump() {
    return dump("");
  }

  @NotNull static String dump(final String specfically) {
    return "\n FAULT: this should not have happened!" + specfically + "\n-----To help you fix the code, here is some info";
  }

  static boolean unreachable() {
    return false;
  }

  @NotNull static String specifically(final String explanation, final Object... os) {
    return dump("\n " + explanation) + Stream.of(os).map(λ -> dump(λ.getClass().getSimpleName(), λ)).reduce((x, y) -> x + y).get() + done();
  }

  @NotNull static String dump(final String name, final Object value) {
    return "\n " + name + "=[" + value + "]";
  }
}
