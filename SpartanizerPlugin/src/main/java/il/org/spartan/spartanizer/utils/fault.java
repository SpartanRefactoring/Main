package il.org.spartan.spartanizer.utils;

/** Fluent API
 * @author Yossi Gil
 * @since 2016 */
public interface fault {
  static String done() {
    return "\n-----this is all I know.";
  }

  static String dump() {
    return "\n FAULT: this should not happen!\n-----To help you fix the code, here is some info";
  }

  static boolean unreachable() {
    return false;
  }
}
