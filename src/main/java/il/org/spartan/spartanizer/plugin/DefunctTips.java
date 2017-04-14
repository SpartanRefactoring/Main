package il.org.spartan.spartanizer.plugin;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.trimming.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code> (v2)
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (original /
 *         30.05.2014) (v3)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (original /
 *         30.05.2014) (v3)
 * @since 2013/07/01 */
public final class DefunctTips {
  static final GUIConfigurationApplicator[] all = { //
      new TrimmerImplementation(), //
  };


  /** @return Iteration over all {@link @GUIApplicator) class instances */
  public static Iterable<GUIConfigurationApplicator> allAvailablespartanizations() {
    return as.iterable(all);
  }



  /** @param tipper rule
   * @return spartanization class rule instance */
  @SuppressWarnings("unchecked") //
  public static <T extends GUIConfigurationApplicator> T findInstance(final Class<? extends T> ¢) {
    return Stream.of(all).filter(λ -> λ.getClass().equals(¢)).map(λ -> (T) λ).findFirst().orElse(null);
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
