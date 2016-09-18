package il.org.spartan.plugin;

import java.util.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code> (v2)
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (original /
 *         30.05.2014) (v3)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (original /
 *         30.05.2014) (v3)
 * @since 2013/07/01 */
public final class Spartanizations {
  private static Applicator[] all = { //
      new Trimmer(), //
  };
  @SuppressWarnings("synthetic-access") //
  private static final Map<String, Applicator> map = new HashMap<String, Applicator>() {
    static final long serialVersionUID = -8921699276699040030L;
    {
      for (final Applicator ¢ : all)
        put(¢.getName(), ¢);
    }
  };

  /** @return all the registered spartanization refactoring objects */
  public static Iterable<Applicator> all() {
    return map.values();
  }

  /** @return Iteration over all Spartanization class instances */
  public static Iterable<Applicator> allAvailableSpartanizations() {
    return as.iterable(all);
  }

  /** @return all the registered spartanization refactoring objects names */
  public static Set<String> allRulesNames() {
    return map.keySet();
  }

  /** @param t Spartanization rule
   * @return Spartanization class rule instance */
  @SuppressWarnings("unchecked") //
  public static <T extends Applicator> T findInstance(final Class<? extends T> ¢) {
    for (final Applicator $ : all)
      if ($.getClass().equals(¢))
        return (T) $;
    return null;
  }

  /** @param name the name of the spartanization
   * @return an instance of the spartanization */
  public static Applicator get(final String name) {
    assert name != null;
    return map.get(name);
  }

  /** Resets the enumeration with the current values from the preferences file.
   * Letting the rules notification decisions be updated without restarting
   * eclipse. */
  public static void reset() {
    map.clear();
    for (final Applicator ¢ : all)
      map.put(¢.getName(), ¢);
  }

  private final Applicator value;

  private Spartanizations(final Applicator value) {
    this.value = value;
  }

  /** @return Spartanization class rule instance */
  public Applicator value() {
    return value;
  }
}
