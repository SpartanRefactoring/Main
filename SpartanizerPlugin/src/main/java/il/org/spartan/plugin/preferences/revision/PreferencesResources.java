package il.org.spartan.plugin.preferences.revision;

import java.util.stream.*;

import org.eclipse.jface.preference.*;

import fluent.ly.*;
import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Daniel Mittelman please add a description
 * @author Daniel Mittelman
 * @since Jan 11, 2017 */
public enum PreferencesResources {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Page description **/
  public static final String PAGE_DESCRIPTION = "Preferences for the Spartanizer plug-in";
  public static final String ZOOMER_PAGE_DESCRIPTION = "Preferences for the Zoomer tool";
  public static final String WIDGET_PAGE_DESCRIPTION = "Preferences for the Athenizer Widget";
  /** General preferences **/
  public static final String PLUGIN_STARTUP_BEHAVIOR_ID = "pref_startup_behavior";
  public static final String PLUGIN_STARTUP_BEHAVIOR_TEXT = "Plugin startup behavior:";
  public static final String[][] PLUGIN_STARTUP_BEHAVIOR_OPTIONS = { //
      { "Remember individual project settings", "remember" }, //
      { "Enable for all projects", "always_on" }, //
      { "Disable for all projects", "always_off" } //
  };
  public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_ID = "Preference_enable_by_default_for_new_projects";
  public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT = "Enable by default for newly created projects";
  public static final String ZOOMER_REVERT_METHOD_ID = "ZOOMER_REVERT_METHOD";
  public static final String ZOOMER_REVERT_METHOD_TEXT = "Compund zoom text edits";
  public static final String WIDGET_SHORTCUT_METHOD_ID = "WIDGET_SHORTCUT_METHOD";
  public static final String WIDGET_SHORTCUT_METHOD_TEXT = "Enable widget shorcut - ctrl-w";
  public static final int WIDGET_MIN_SIZE = 60;
  public static final int WIDGET_MAX_SIZE = 100;
  public static final int WIDGET_MAX_OPS = 7;
  // NOT SAFE
  public static final String TIPPER_CATEGORY_PREFIX = "il.org.spartan";
  public static final Bool NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE = new Bool(true);
  public static final Bool ZOOMER_REVERT_METHOD_VALUE = new Bool(false);
  public static final Bool WIDGET_SHORTCUT_METHOD_VALUE = new Bool(true);

  public static String getLabel(final Class<? extends ExpanderCategory> $) {
    return English.name($);
  }
  public static IPreferenceStore store() {
    return Plugin.plugin().getPreferenceStore();
  }
  

  /** An enum holding together all the "enabled spartanizations" options, also
   * allowing to get the set preference value for each of them */
  public enum TipperGroup {
    Abbreviation(TipperCategory.Abbreviation.class), //
    Arithmetic(TipperCategory.Arithmetics.class), //
    Anonymization(TipperCategory.Anonymization.class), //
    Collapse(TipperCategory.Collapse.class), //
    CommonFactoring(TipperCategory.CommnonFactoring.class), //
    Centification(TipperCategory.Centification.class), //
    Deadcode(TipperCategory.Deadcode.class), //
    Dollarization(TipperCategory.NameOfResult.class), //
    EarlyReturn(TipperCategory.EarlyReturn.class), //
    Idiomatic(TipperCategory.Idiomatic.class), //
    Inlining(TipperCategory.Inlining.class), //
    Loops(TipperCategory.Loops.class), //
    NOOP(TipperCategory.NOP.class), //
    Nanopatterns(TipperCategory.Nanos.class), //
    ScopeReduction(TipperCategory.ScopeReduction.class), //
    Sorting(TipperCategory.Sorting.class), //
    SyntacticBaggage(TipperCategory.SyntacticBaggage.class), //
    Ternarization(TipperCategory.Ternarization.class), //
    Bloater(TipperCategory.Bloater.class), //
    Shortcut(TipperCategory.Shortcircuit.class), //
    Thrashing(TipperCategory.EmptyCycles.class), //
    NOOPOnBooleans(TipperCategory.NOP.onBooleans.class), //
    NOOPOnStrings(TipperCategory.NOP.onStrings.class), //
    NOOPOnNumbers(TipperCategory.NOP.onNumbers.class), //
    ;
    public static TipperGroup find(final TipperCategory ¢) {
      return find(¢.getClass());
    }
    private static TipperGroup find(final Class<? extends TipperCategory> ¢) {
      return Stream.of(TipperGroup.values()).filter(λ -> λ.clazz.isAssignableFrom(¢)).findFirst().orElse(null);
    }

    private final Class<? extends TipperCategory> clazz;
    public final String id;
    public final String label;

    TipperGroup(final Class<? extends TipperCategory> clazz) {
      this.clazz = clazz;
      id = clazz.getCanonicalName();
      label = clazz.getSimpleName();
    }
    @SuppressWarnings("static-method") public boolean isEnabled() {
      // This preferences implementation is deprecated. Will be removed soon.
      // --or
      // TODO Roth: deprecate old preferences implementation
      // return Plugin.plugin() == null || store().getBoolean(id);
      return true;
    }
  }
}
