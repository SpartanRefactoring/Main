package il.org.spartan.plugin;

/** Fluent API
 * @author Yossi Gil
 * @since 2016 */
public interface fault {
  static String done() {
    return "\n-----this is all I know.";
  }

  static String dump() {
    return "fault: this should not happen! To help you fix the code, here is some info";
  }

  static boolean unreachable() {
    return false;
  }
}
