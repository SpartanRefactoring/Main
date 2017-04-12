package il.org.spartan.spartanizer.cmdline;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.plugin.*;

/** TODO Matteo Orru' please add a description
 * @author Matteo Orru'
 * @since 2016 */
public final class Tips2 {
  private static final AbstractGUIApplicator[] all = { //
      new Trimmer(), //
  };
  private final AbstractGUIApplicator value;

  private Tips2(final AbstractGUIApplicator value) {
    this.value = value;
  }

  /** @return ? */
  public AbstractGUIApplicator value() {
    return value;
  }

  @SuppressWarnings("synthetic-access") //
  private static final Map<String, AbstractGUIApplicator> map = new HashMap<String, AbstractGUIApplicator>() {
    static final long serialVersionUID = -0x7BD03E391481791EL;
    {
      as.list(all).forEach(λ -> put(λ.getName(), λ));
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
    return Stream.of(all).filter(λ -> λ.getClass().equals(¢)).map(λ -> (T) λ).findFirst().orElse(null);
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
    as.list(all).forEach(λ -> map.put(λ.getName(), λ));
  }
}
