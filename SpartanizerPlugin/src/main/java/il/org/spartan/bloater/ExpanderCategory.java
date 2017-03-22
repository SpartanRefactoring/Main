package il.org.spartan.bloater;

import java.util.stream.*;

import org.eclipse.jface.preference.*;

import il.org.spartan.plugin.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** Classification of Expanders
 * @author Raviv Rachmiel
 * @since 24-12-16 */
@FunctionalInterface
public interface ExpanderCategory {
  @NotNull String description();

  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * ExpanderGroup TODO Roth, add - {@link ExpanderGroup} when you make the
   * expander preferencesResources
   * @return preference group this tipper belongs to */
  default ExpanderGroup tipperGroup() {
    return ExpanderGroup.find(this);
  }

  default ExpanderGroup ExpanderGroup() {
    return ExpanderGroup.find(this);
  }

  @NotNull
  static String getLabel(@NotNull final Class<? extends ExpanderCategory> ¢) {
    return system.className(¢);
  }

  // TODO Roth, to preferences?
  @FunctionalInterface
  interface Nominal extends ExpanderCategory {
    String label = "Nominal";
  }

  interface Clearification extends Nominal {
    String label = "adding brackets or some chars which might make the code clearer";

    @NotNull
    @Override default String description() {
      return label;
    }
  }

  interface Explanation extends Nominal {
    String label = "expanding a statement to clearer information, though longer";

    @NotNull
    @Override default String description() {
      return label;
    }
  }

  interface Ternarization extends Nominal {
    String label = "ternary arguments";

    @NotNull
    @Override default String description() {
      return label;
    }
  }

  /** An enum holding together all the "enabled expanders" options */
  // TODO Roth, please make a preferencesResources file for the expanders
  enum ExpanderGroup {
    Abbreviation(ExpanderCategory.Clearification.class), //
    Explanation(ExpanderCategory.Explanation.class), //
    Ternarization(ExpanderCategory.Ternarization.class), //
    ;
    public static ExpanderGroup find(@NotNull final ExpanderCategory ¢) {
      return find(¢.getClass());
    }

    static IPreferenceStore store() {
      return Plugin.plugin().getPreferenceStore();
    }

    private static ExpanderGroup find(@NotNull final Class<? extends ExpanderCategory> ¢) {
      return Stream.of(ExpanderGroup.values()).filter(λ -> λ.clazz.isAssignableFrom(¢)).findFirst().orElse(null);
    }

    @NotNull
    private final Class<? extends ExpanderCategory> clazz;
    final String id;
    @NotNull
    final String label;

    ExpanderGroup(@NotNull final Class<? extends ExpanderCategory> clazz) {
      this.clazz = clazz;
      id = clazz.getCanonicalName();
      label = ExpanderCategory.getLabel(clazz);
    }

    public boolean isEnabled() {
      return Plugin.plugin() == null || store().getBoolean(id);
    }
  }
}
