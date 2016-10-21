package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.nio.charset.*;

/** Fluent API
 * @author Yossi Gil
 * @since 2016 */
public interface fault {
  static String done() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    new AssertionError().printStackTrace(ps);
    return new String(baos.toByteArray(), StandardCharsets.UTF_8) + "\n-----this is all I know.";
  }

  static String dump() {
    return "\n FAULT: this should not have happened!\n-----To help you fix the code, here is some info";
  }

  static boolean unreachable() {
    return false;
  }
}
