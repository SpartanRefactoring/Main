package il.org.spartan.refactoring.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import il.org.spartan.refactoring.builder.Plugin;

public class PluginPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PluginPreferencesPage() {
		super(GRID);
	}
	
	/**
	 * Build the preferences page by adding controls
	 */
	public void createFieldEditors() {
		addField(new ComboFieldEditor(
					PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_ID, 
					PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_TEXT, 
					PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_OPTIONS, 
					getFieldEditorParent())
				);
		addField(new BooleanFieldEditor(
					PluginPreferencesStrings.NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, 
					PluginPreferencesStrings.NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT, 
					getFieldEditorParent())
				);
		
		for(String[] wring : PluginPreferencesStrings.getAllWringComboOptions()) {
			addField(new ComboFieldEditor(
						wring[0], 
						wring[1], 
						PluginPreferencesStrings.WRING_COMBO_OPTIONS, 
						getFieldEditorParent())
					);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
		setDescription(PluginPreferencesStrings.PAGE_DESCRIPTION);
	}
}
