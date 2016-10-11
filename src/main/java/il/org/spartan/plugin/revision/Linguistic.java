package il.org.spartan.plugin.revision;

/** @author Ori Roth
 * @since 2016 */
public class Linguistic {
  public static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  public static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }
}
