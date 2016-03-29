package il.org.spartan.refactoring.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import il.org.spartan.refactoring.builder.Plugin;

public class PluginPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private SpartanPropertyListener listener;
	
	
	public PluginPreferencesPage() {
		super(GRID);
		listener = new SpartanPropertyListener();
	}
	
	/**
	 * Build the preferences page by adding controls
	 */
	public void createFieldEditors() {
		// Add the startup behavior combo box
		addField(new ComboFieldEditor(
					PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_ID, 
					PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_TEXT, 
					PluginPreferencesStrings.PLUGIN_STARTUP_BEHAVIOR_OPTIONS, 
					getFieldEditorParent())
				);
		
		// Add the enabled for new projects checkbox
		addField(new BooleanFieldEditor(
					PluginPreferencesStrings.NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, 
					PluginPreferencesStrings.NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT, 
					getFieldEditorParent())
				);
		
		// Create and fill the "enabled spartanizations" group box
		GroupFieldEditor gr = new GroupFieldEditor("Enabled spartanizations", getFieldEditorParent());		
		
		for(String[] wring : PluginPreferencesStrings.getAllWringComboOptions()) {
			ComboFieldEditor cfe = new ComboFieldEditor(
					wring[0], 
					wring[1], 
					PluginPreferencesStrings.WRING_COMBO_OPTIONS, 
					gr.getFieldEditor());
			
			cfe.setPropertyChangeListener(listener);
			gr.add(cfe);
		}
		
		addField(gr);
		gr.init();
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
		setDescription(PluginPreferencesStrings.PAGE_DESCRIPTION);
	}
	
	/**
	 * An event handler used to re-initialize the Trimmer spartanization once a wring preference
	 * was modified.
	 */
	private static class SpartanPropertyListener implements IPropertyChangeListener {
		@Override public void propertyChange(PropertyChangeEvent event) {
			// TODO actually implement this
			System.out.println("Detected change in wring settings");
		}
	}
}
