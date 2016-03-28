package il.org.spartan.refactoring.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import il.org.spartan.refactoring.builder.Plugin;

/**
 * This class is called by Eclipse when the plugin is first loaded and has no default preference values.
 * These are set by the values specified here.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016/03/28
 *
 */
public class PluginPreferencesDefaultValuesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore ps = Plugin.getDefault().getPreferenceStore();
		
		ps.setDefault(PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_ID, "remember");
		ps.setDefault(PluginPreferencesStrings.NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, true);
		
		ps.setDefault(PluginPreferencesStrings.CONSOLIDATE_ASSIGNMENTS_STATEMENTS_ID, "on");
		ps.setDefault(PluginPreferencesStrings.SIMPLIFY_NESTED_BLOCKS_ID, "on");
		ps.setDefault(PluginPreferencesStrings.ELIMINATE_TEMP_ID, "on");
		ps.setDefault(PluginPreferencesStrings.REMOVE_REDUNDANT_PUNCTUATION_ID, "on");
		ps.setDefault(PluginPreferencesStrings.IF_TO_TERNARY_ID, "on");
		ps.setDefault(PluginPreferencesStrings.REFACTOR_INEFFECTIVE_ID, "on");
		ps.setDefault(PluginPreferencesStrings.REORDER_EXPRESSIONS_ID, "on");
		ps.setDefault(PluginPreferencesStrings.RENAME_PARAMETERS_ID, "on");
		ps.setDefault(PluginPreferencesStrings.RENAME_RETURN_VARIABLE_ID, "on");
	}
}
