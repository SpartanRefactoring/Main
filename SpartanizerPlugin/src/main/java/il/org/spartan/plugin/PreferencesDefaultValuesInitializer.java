package il.org.spartan.plugin;

import static il.org.spartan.plugin.PreferencesResources.*;
import static il.org.spartan.plugin.PreferencesResources.WringGroup.*;

import org.eclipse.core.runtime.preferences.*;
import org.eclipse.jface.preference.*;

import il.org.spartan.plugin.PreferencesResources.*;

/** This class is called by Eclipse when the plugin is first loaded and has no
 * default preference values. These are set by the values specified here.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016/03/28 */
public class PreferencesDefaultValuesInitializer extends AbstractPreferenceInitializer {
  @Override public void initializeDefaultPreferences() {
    final IPreferenceStore s = store();
    s.setDefault(PLUGIN__STARTUP__BEHAVIOR__ID, "remember");
    s.setDefault(NEW__PROJECTS__ENABLE__BY__DEFAULT__ID, true);
    for (final WringGroup g : WringGroup.values())
      s.setDefault(g.id, "on");
  }
}
