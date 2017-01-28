package il.org.spartan.plugin.preferences;

import static il.org.spartan.plugin.preferences.PreferencesResources.TipperGroup.*;

import java.util.*;

import il.org.spartan.plugin.preferences.PreferencesResources.*;

/** This class is called by Eclipse when the plugin is first loaded and has no
 * default preference values. These are set by the values specified here.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016/03/28 */
public final class PreferencesDefaultValuesInitializer extends AbstractPreferenceInitializer {
  @Override public void initializeDefaultPreferences() {
    final IPreferenceStore s = store();
    // s.setDefault(PLUGIN_STARTUP_BEHAVIOR_ID, "remember");
    s.setDefault(PreferencesResources.NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, true);
    Arrays.asList(TipperGroup.values()).forEach(λ -> s.setDefault(λ.id, true));
  }
}
