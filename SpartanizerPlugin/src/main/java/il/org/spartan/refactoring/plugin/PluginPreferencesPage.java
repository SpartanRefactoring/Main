package il.org.spartan.refactoring.plugin;

import static il.org.spartan.refactoring.plugin.PluginPreferencesResources.*;
import static il.org.spartan.refactoring.plugin.PluginPreferencesResources.WringGroup.*;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.ui.*;

import il.org.spartan.refactoring.wring.*;

public class PluginPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  public static final String WRING_COMBO_OPTIONS[][] = { { "Enabled", "on" }, { "Disabled", "off" } };
  private final SpartanPropertyListener listener;

  public PluginPreferencesPage() {
    super(GRID);
    listener = new SpartanPropertyListener();
  }

  /** Build the preferences page by adding controls */
  @Override public void createFieldEditors() {
    // Add the startup behavior combo box
    addField(new ComboFieldEditor(PLUGIN_STARTUP_BEHAVIOR_ID, PLUGIN_STARTUP_BEHAVIOR_TEXT,
        PluginPreferencesResources.PLUGIN_STARTUP_BEHAVIOR_OPTIONS, getFieldEditorParent()));
    // Add the enabled for new projects checkbox
    addField(new BooleanFieldEditor(NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT, getFieldEditorParent()));
    // Create and fill the "enabled spartanizations" group box
    final GroupFieldEditor g = new GroupFieldEditor("Enabled spartanizations", getFieldEditorParent());
    for (final WringGroup w : WringGroup.values())
      g.add(new ComboFieldEditor(w.id, w.label, WRING_COMBO_OPTIONS, g.getFieldEditor()));
    addField(g);
    g.init();
  }

  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(WringGroup.store());
    setDescription(PAGE_DESCRIPTION);
    store().addPropertyChangeListener(listener);
  }

  /** An event handler used to re-initialize the {@link Trimmer} spartanization
   * once a wring preference was modified. */
  static class SpartanPropertyListener implements IPropertyChangeListener {
    @Override public void propertyChange(@SuppressWarnings("unused") final PropertyChangeEvent __) {
      // Recreate the toolbox's internal instance, adding only enabled wrings
      Toolbox.generate();
      try {
        Plugin.refreshAllProjects();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }
}
