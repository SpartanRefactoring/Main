package il.org.spartan.plugin.preferences.revision;

import org.eclipse.jface.preference.*;

import fluent.ly.*;
import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.plugin.*;
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
  public static final String ZOOMER_AUTO_ACTIVISION_ID = "ZOOMER_AUTO_ACTIVISION";
  public static final String ZOOMER_AUTO_ACTIVISION_ID_TEXT = "Activate zommer on startup";
  public static final String WIDGET_SHORTCUT_METHOD_ID = "WIDGET_SHORTCUT_METHOD";
  public static final int WIDGET_MIN_SIZE = 60;
  public static final int WIDGET_MAX_SIZE = 100;
  public static final int WIDGET_MAX_OPS = 7;
  // NOT SAFE
  public static final String TIPPER_CATEGORY_PREFIX = "il.org.spartan";
  public static final Bool NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE = new Bool(true);
  public static final Bool ZOOMER_AUTO_ACTIVISION_VALUE = new Bool(false);
  public static final Bool ZOOMER_REVERT_METHOD_VALUE = new Bool(false);
  public static final Bool WIDGET_SHORTCUT_METHOD_VALUE = new Bool(true);
  public static final String WIDGET_SIZE = "WIDGET_SIZE";
  static final String WIDGET_OPERATION_CONFIGURATION = "WIDGET_OPERATION_CONFIGURATION";

  public static String getLabel(final Class<? extends ExpanderCategory> $) {
    return English.name($);
  }
  public static IPreferenceStore store() {
    return Plugin.plugin().getPreferenceStore();
  }
}
