package il.org.spartan.plugin.revision;

import java.util.function.*;

/** @author Ori Roth
 * @since 2016 */
public class Linguistic {
  public static final String NAN = "???";

  public static String plurals(final String s, final Integer i) {
    return i == null ? NAN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  public static String plurales(final String s, final Integer i) {
    return i == null ? NAN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  public static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  public static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }

  public static <X> String nanable(X ¢) {
    return ¢ == null ? NAN : ¢ + "";
  }

  public static <X> String nanable(X x, Function<X, ?> f) {
    return x == null ? NAN : f.apply(x) + "";
  }
}
