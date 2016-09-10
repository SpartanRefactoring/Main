package il.org.spartan.plugin;

import static il.org.spartan.plugin.PreferencesResources.*;
import static il.org.spartan.plugin.PreferencesResources.WringGroup.*;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.wring.*;

/** ??
 * @author Daniel Mittelman
 * @year 2016 */
public class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  public static final String WRING__COMBO__OPTIONS[][] = { { "Enabled", "on" }, { "Disabled", "off" } };
  private final SpartanPropertyListener listener;

  public PreferencesPage() {
    super(GRID);
    listener = new SpartanPropertyListener();
  }

  /** Build the preferences page by adding controls */
  @Override public void createFieldEditors() {
    // Add the startup behavior combo box
    addField(new ComboFieldEditor(PLUGIN__STARTUP__BEHAVIOR__ID, PLUGIN__STARTUP__BEHAVIOR__TEXT, PLUGIN__STARTUP__BEHAVIOR__OPTIONS, getFieldEditorParent()));
    // Add the enabled for new projects checkbox
    addField(new BooleanFieldEditor(NEW__PROJECTS__ENABLE__BY__DEFAULT__ID, NEW__PROJECTS__ENABLE__BY__DEFAULT__TEXT, getFieldEditorParent()));
    // Create and fill the "enabled spartanizations" group box
    final GroupFieldEditor g = new GroupFieldEditor("Enabled spartanizations", getFieldEditorParent());
    for (final WringGroup w : WringGroup.values())
      g.add(new ComboFieldEditor(w.id, w.label, WRING__COMBO__OPTIONS, g.getFieldEditor()));
    addField(g);
    g.init();
  }

  @Override public void init(@SuppressWarnings("unused") final IWorkbench ____) {
    setPreferenceStore(WringGroup.store());
    setDescription(PAGE__DESCRIPTION);
    store().addPropertyChangeListener(listener);
  }

  /** An event handler used to re-initialize the {@link Trimmer} spartanization
   * once a wring preference was modified. */
  static class SpartanPropertyListener implements IPropertyChangeListener {
    @Override public void propertyChange(@SuppressWarnings("unused") final PropertyChangeEvent ____) {
      // Recreate the toolbox's internal instance, adding only enabled wrings
      Toolbox.refresh();
      try {
        Plugin.refreshAllProjects();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }
}
