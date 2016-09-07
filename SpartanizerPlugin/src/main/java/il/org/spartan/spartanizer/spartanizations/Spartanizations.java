package il.org.spartan.spartanizer.spartanizations;

import java.util.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.wring.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code> (v2)
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (original /
 *         30.05.2014) (v3)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (original /
 *         30.05.2014) (v3)
 * @since 2013/07/01 */
public class Spartanizations {
  private static Spartanization[] all = { //
      new Trimmer(), //
  };
  @SuppressWarnings("synthetic-access") //
  private static final Map<String, Spartanization> map = new HashMap<String, Spartanization>() {
    static final long serialVersionUID = -8921699276699040030L;
    {
      for (final Spartanization s : all)
        put(s.getClass().getSimpleName(), s);
    }
  };

  /** @return all the registered spartanization refactoring objects */
  public static Iterable<Spartanization> all() {
    return map.values();
  }

  /** @return Iteration over all Spartanization class instances */
  public static Iterable<Spartanization> allAvailableSpartanizations() {
    return as.iterable(all);
  }

  /** @return all the registered spartanization refactoring objects names */
  public static Set<String> allRulesNames() {
    return map.keySet();
  }

  /** @param c Spartanization rule
   * @return Spartanization class rule instance */
  @SuppressWarnings("unchecked") //
  public static <T extends Spartanization> T findInstance(final Class<? extends T> c) {
    for (final Spartanization $ : all)
      if ($.getClass().equals(c))
        return (T) $;
    return null;
  }

  /** @param name the name of the spartanization
   * @return an instance of the spartanization */
  public static Spartanization get(final String name) {
    assert name != null;
    return map.get(name);
  }

  /** Resets the enumeration with the current values from the preferences file.
   * Letting the rules notification decisions be updated without restarting
   * eclipse. */
  public static void reset() {
    map.clear();
    for (final Spartanization s : all)
      map.put(s.getClass().getSimpleName(), s);
  }

  private final Spartanization value;

  private Spartanizations(final Spartanization value) {
    this.value = value;
  }

  /** @return Spartanization class rule instance */
  public Spartanization value() {
    return value;
  }
}
