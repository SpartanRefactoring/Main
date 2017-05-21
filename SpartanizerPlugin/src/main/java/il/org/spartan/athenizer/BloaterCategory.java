package il.org.spartan.athenizer;

import java.util.stream.*;

import org.eclipse.jface.preference.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.*;

/** Classification of Expanders
 * @author Raviv Rachmiel
 * @since 24-12-16 */
@FunctionalInterface
public interface BloaterCategory {
  String description();
  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * ExpanderGroup TODO Roth, add - {@link BloaterGroup} when you make the
   * expander preferencesResources
   * @return preference group this tipper belongs to */
  default BloaterGroup ExpanderGroup() {
    return BloaterGroup.find(this);
  }
  static String getLabel(final Class<? extends BloaterCategory> ¢) {
    return English.name(¢);
  }

  // TODO Roth, to preferences?
  @FunctionalInterface
  interface Nominal extends BloaterCategory {
    String label = "Nominal";
  }
  
  @FunctionalInterface
  interface Common extends BloaterCategory {
    String label = "Common code";
  }

  interface Clearification extends Nominal {
    String label = "adding brackets or some chars which might make the code clearer";

    @Override default String description() {
      return label;
    }
  }

  interface Explanation extends Nominal {
    String label = "expanding a statement to clearer information, though longer";

    @Override default String description() {
      return label;
    }
  }

  interface Ternarization extends Nominal {
    String label = "ternary arguments";

    @Override default String description() {
      return label;
    }
  }
  
  interface Splitting extends Common {
    String label = "split statement";

    @Override default String description() {
      return label;
    }
  }
  
  interface Unshortcut extends BloaterCategory {
    String label = "unshortcut";

    @Override default String description() {
      return label;
    }
  }
  
  interface Clarity extends BloaterCategory {
    String label = "clarify";

    @Override default String description() {
      return label;
    }
  }
  
  interface Structural extends BloaterCategory {
    String label = "structural";

    @Override default String description() {
      return label;
    }
  }

  /** An enum holding together all the "enabled expanders" options */
  // TODO Roth, please make a preferencesResources file for the expanders
  enum BloaterGroup {
    Abbreviation(BloaterCategory.Clearification.class), //
    Explanation(BloaterCategory.Explanation.class), //
    Ternarization(BloaterCategory.Ternarization.class), //
    ;
    public static BloaterGroup find(final BloaterCategory ¢) {
      return find(¢.getClass());
    }
    static IPreferenceStore store() {
      return Plugin.plugin().getPreferenceStore();
    }
    private static BloaterGroup find(final Class<? extends BloaterCategory> ¢) {
      return Stream.of(BloaterGroup.values()).filter(λ -> λ.clazz.isAssignableFrom(¢)).findFirst().orElse(null);
    }

    private final Class<? extends BloaterCategory> clazz;
    final String id;
    final String label;

    BloaterGroup(final Class<? extends BloaterCategory> clazz) {
      this.clazz = clazz;
      id = clazz.getCanonicalName();
      label = BloaterCategory.getLabel(clazz);
    }
    public boolean isEnabled() {
      return Plugin.plugin() == null || store().getBoolean(id);
    }
  }
}
