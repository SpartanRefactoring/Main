package il.org.spartan.plugin.preferences.revision;

import org.eclipse.core.runtime.preferences.*;

/** This class is called by Eclipse when the plugin is first loaded and has no
 * default preference values. These are set by the values specified here.
 * Tippers configurations for projects are managed externally, thus not included
 * here.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @author Ori Roth
 * @since 2016/03/28 */
public final class PreferencesDefaultValuesInitializer extends AbstractPreferenceInitializer {
  @Override public void initializeDefaultPreferences() {
    PreferencesResources.store().setDefault(PreferencesResources.NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, true);
    PreferencesResources.store().setDefault(PreferencesResources.ZOOMER_REVERT_METHOD_ID, false);
    PreferencesResources.store().setDefault(PreferencesResources.ZOOMER_AUTO_ACTIVISION_ID, true);
  }
}
