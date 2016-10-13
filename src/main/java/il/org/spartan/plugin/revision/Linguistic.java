package il.org.spartan.plugin.revision;

import java.util.concurrent.atomic.*;
import java.util.function.*;

/** @author Ori Roth
 * @since 2016 */
public class Linguistic {
  public static final String NAN = "???";

  /** Cut string's suffix.
   * @param s JD
   * @param l JD
   * @return cut string */
  public static String trim(final String s, final int l) {
    return s == null || s.length() < l ? s : s.substring(0, l);
  }

  public static String plurals(final String s, final Integer i) {
    return i == null ? NAN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  public static String plurales(final String s, final Integer i) {
    return i == null ? NAN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }
  
  public static String plurals(final String s, final AtomicInteger i) {
    return i == null ? NAN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  public static String plurales(final String s, final AtomicInteger i) {
    return i == null ? NAN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  public static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  public static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }

  public static <X> String nanable(final X ¢) {
    return ¢ == null ? NAN : ¢ + "";
  }

  public static <X> String nanable(final X x, final Function<X, ?> f) {
    return x == null ? NAN : f.apply(x) + "";
  }
}
