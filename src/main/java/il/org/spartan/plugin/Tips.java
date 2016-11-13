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
public final class Tips {
  private static AbstractGUIApplicator[] all = { //
      new Trimmer(), //
  };
  @SuppressWarnings("synthetic-access") //
  private static final Map<String, AbstractGUIApplicator> map = new HashMap<String, AbstractGUIApplicator>() {
    static final long serialVersionUID = -8921699276699040030L;
    {
      for (final AbstractGUIApplicator ¢ : all)
        put(¢.getName(), ¢);
    }
  };

  /** @return all the registered spartanization refactoring objects */
  public static Iterable<AbstractGUIApplicator> all() {
    return map.values();
  }
  /** @return Iteration over all {@link @GUIApplicator) class instances */
  public static Iterable<AbstractGUIApplicator> allAvailablespartanizations() {
    return as.iterable(all);
  }
  /** @return all the registered spartanization refactoring objects names */
  public static Set<String> allRulesNames() {
    return map.keySet();
  }
  /** @param tipper rule
   * @return spartanization class rule instance */
  @SuppressWarnings("unchecked") //
  public static <T extends AbstractGUIApplicator> T findInstance(final Class<? extends T> ¢) {
    for (final AbstractGUIApplicator $ : all)
      if ($.getClass().equals(¢))
        return (T) $;
    return null;
  }
  /** @param name the name of the applicator
   * @return an instance of the class */
  public static AbstractGUIApplicator get(final String name) {
    assert name != null;
    return map.get(name);
  }
  /** Resets the enumeration with the current values from the preferences file.
   * Letting the rules notification decisions be updated without restarting
   * eclipse. */
  public static void reset() {
    map.clear();
    for (final AbstractGUIApplicator ¢ : all)
      map.put(¢.getName(), ¢);
  }

  private final AbstractGUIApplicator value;

  private Tips(final AbstractGUIApplicator value) {
    this.value = value;
  }
  /** @return ? */
  public AbstractGUIApplicator value() {
    return value;
  }
}
