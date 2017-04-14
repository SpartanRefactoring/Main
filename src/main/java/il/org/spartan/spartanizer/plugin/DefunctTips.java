package il.org.spartan.spartanizer.plugin;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code> (v2)
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (original /
 *         30.05.2014) (v3)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (original /
 *         30.05.2014) (v3)
 * @since 2013/07/01 */
public final class DefunctTips {
  static final GUIConfigurationApplicator[] all = { //
      new Trimmer(), //
  };
  private static final Map<String, GUIConfigurationApplicator> map = new HashMap<String, GUIConfigurationApplicator>() {
    static final long serialVersionUID = -0x7BD03E391481791EL;
    {
      as.list(all).forEach(λ -> put(λ.getName(), λ));
    }
  };

  /** @return all the registered spartanization refactoring objects */
  public static Iterable<GUIConfigurationApplicator> all() {
    return map.values();
  }

  /** @return Iteration over all {@link @GUIApplicator) class instances */
  public static Iterable<GUIConfigurationApplicator> allAvailablespartanizations() {
    return as.iterable(all);
  }

  /** @return all the registered spartanization refactoring objects names */
  public static Set<String> allRulesNames() {
    return map.keySet();
  }

  /** @param tipper rule
   * @return spartanization class rule instance */
  @SuppressWarnings("unchecked") //
  public static <T extends GUIConfigurationApplicator> T findInstance(final Class<? extends T> ¢) {
    return Stream.of(all).filter(λ -> λ.getClass().equals(¢)).map(λ -> (T) λ).findFirst().orElse(null);
  }

  /** @param name the name of the applicator
   * @return an instance of the class */
  public static GUIConfigurationApplicator get(final String name) {
    assert name != null;
    return map.get(name);
  }

  /** Resets the enumeration with the current values from the preferences file.
   * Letting the rules notification decisions be updated without restarting
   * eclipse. */
  public static void reset() {
    map.clear();
    as.list(all).forEach(λ -> map.put(λ.getName(), λ));
  }

  private final GUIConfigurationApplicator value;

  private DefunctTips(final GUIConfigurationApplicator value) {
    this.value = value;
  }

  /** @return ? */
  public GUIConfigurationApplicator value() {
    return value;
  }
}
