package il.org.spartan.plugin;

import org.eclipse.jface.preference.*;

import il.org.spartan.spartanizer.wring.*;

/** ??
 * @author Daniel Mittelman
 * @since 2016 */
public class PreferencesResources {
  /** Page description **/
  public static final String PAGE__DESCRIPTION = "Preferences for the spartanizer plug-in";
  /** General preferences **/
  public static final String PLUGIN__STARTUP__BEHAVIOR__ID = "pref__startup__behavior";
  public static final String PLUGIN__STARTUP__BEHAVIOR__TEXT = "Plugin startup behavior:";
  public static final String[][] PLUGIN__STARTUP__BEHAVIOR__OPTIONS = { { "Remember individual project settings", "remember" }, //
      { "Enable for all projects", "always__on" }, //
      { "Disable for all projects", "always__off" }, //
  };
  public static final String NEW__PROJECTS__ENABLE__BY__DEFAULT__ID = "pref__enable__by__default__for__new__projects";
  public static final String NEW__PROJECTS__ENABLE__BY__DEFAULT__TEXT = "Enable by default for newly created projects";

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
    public static WringGroup find(final Kind ¢) {
      return find(¢.getClass());
    }

    static IPreferenceStore store() {
      return Plugin.plugin().getPreferenceStore();
    }

    private static WringGroup find(final Class<? extends Kind> c) {
      for (final WringGroup $ : WringGroup.values())
        if ($.clazz.isAssignableFrom(c))
          return $;
      return null;
    }

    private static Object getLabel(final Class<? extends Kind> c) {
      try {
        return c.getField("label").get(null);
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
        e.printStackTrace();
        return null;
      }
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
}