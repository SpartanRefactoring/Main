package il.org.spartan.plugin;

import org.eclipse.jface.preference.*;

import il.org.spartan.spartanizer.wring.*;

/** ??
 * @author Daniel Mittelman
 * @since 2016 */
public class PreferencesResources {
  /** An enum holding together all the "enabled spartanizations" options, also
   * allowing to get the set preference value for each of them
   * @Author Daniel Mittelman
   * @since 2016 */
  public enum WringGroup {
    Abbreviation(Kind.Abbreviation.class), //
    Canonicalization(Kind.Canonicalization.class), //
    Centification(Kind.Centification.class), //
    DistributiveRefactoring(Kind.DistributiveRefactoring.class), //
    Dollarization(Kind.Dollarization.class), //
    Inlining(Kind.Inlining.class), //
    NoImpact(Kind.NoImpact.class), //
    ScopeReduction(Kind.ScopeReduction.class), //
    Sorting(Kind.Sorting.class), //
    SyntacticBaggage(Kind.SyntacticBaggage.class), //
    Ternarization(Kind.Ternarization.class), //
    UnusedArguments(Kind.UnusedArguments.class),//
    ;
    private static WringGroup find(final Class<? extends Kind> k) {
      for (final WringGroup $ : WringGroup.values())
        if ($.clazz.isAssignableFrom(k))
          return $;
      return null;
    }

    public static WringGroup find(final Kind ¢) {
      return find(¢.getClass());
    }

    private static Object getLabel(final Class<? extends Kind> k) {
      try {
        return k.getField("label").get(null);
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
        e.printStackTrace();
        return null;
      }
    }

    static IPreferenceStore store() {
      return Plugin.plugin().getPreferenceStore();
    }

    private final Class<? extends Kind> clazz;
    final String id;
    final String label;

    private WringGroup(final Class<? extends Kind> clazz) {
      this.clazz = clazz;
      id = clazz.getCanonicalName();
      label = getLabel(clazz) + "";
    }

    public boolean isEnabled() {
      return Plugin.plugin() == null || "on".equals(store().getString(id));
    }
  }

  /** Page description **/
  public static final String PAGE_DESCRIPTION = "Preferences for the spartanizer plug-in";
  /** General preferences **/
  public static final String PLUGIN_STARTUP_BEHAVIOR_ID = "pref_startup_behavior";
  public static final String PLUGIN_STARTUP_BEHAVIOR_TEXT = "Plugin startup behavior:";
  public static final String[][] PLUGIN_STARTUP_BEHAVIOR_OPTIONS = { { "Remember individual project settings", "remember" }, //
      { "Enable for all projects", "always_on" }, //
      { "Disable for all projects", "always_off" }, //
  };
  public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_ID = "pref_enable_by_default_for_new_projects";
  public static final String NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT = "Enable by default for newly created projects";
}