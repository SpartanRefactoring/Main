package il.org.spartan.plugin;

import static il.org.spartan.plugin.PreferencesResources.*;
import static il.org.spartan.plugin.PreferencesResources.TipperGroup.*;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.dispatch.*;

/** ??
 * @author Daniel Mittelman
 * @year 2016
 * @author Ori Roth
 * @since 2.6 */
public final class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  public static final String TIPPER_COMBO_OPTIONS[][] = { { "Enabled", "on" }, { "Disabled", "off" } };
  private final SpartanPropertyListener listener;
  private final MBoolean refreshNeeded;

  public PreferencesPage() {
    super(GRID);
    refreshNeeded = new MBoolean(false);
    listener = new SpartanPropertyListener(refreshNeeded);
  }

  @Override public boolean performOk() {
    refreshNeeded.is = false;
    final boolean $ = super.performOk();
    if (refreshNeeded.is)
      new Thread(() -> {
        Toolbox.refresh();
        try {
          RefreshAll.go();
        } catch (final Exception x) {
          monitor.logEvaluationError(this, x);
        }
      }).start();
    return $;
  }

  /** Build the preferences page by adding controls */
  @Override public void createFieldEditors() {
    addField(new ComboFieldEditor(PLUGIN_STARTUP_BEHAVIOR_ID, PLUGIN_STARTUP_BEHAVIOR_TEXT, PLUGIN_STARTUP_BEHAVIOR_OPTIONS, getFieldEditorParent()));
    addField(new BooleanFieldEditor(NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT, getFieldEditorParent()));
    final GroupFieldEditor g = new GroupFieldEditor("Enabled spartanizations", getFieldEditorParent());
    for (final TipperGroup ¢ : TipperGroup.values())
      g.add(new BooleanFieldEditor(¢.id, ¢.label, g.getFieldEditor()));
    addField(g);
    g.init();
  }

  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(TipperGroup.store());
    setDescription(PAGE_DESCRIPTION);
    store().addPropertyChangeListener(listener);
  }

  /** An event handler used to re-initialize the {@link Trimmer} spartanization
   * once a tipper preference was modified. */
  static class SpartanPropertyListener implements IPropertyChangeListener {
    private final MBoolean refreshNeeded;

    public SpartanPropertyListener(final MBoolean refreshNeeded) {
      this.refreshNeeded = refreshNeeded;
    }

    @Override public void propertyChange(final PropertyChangeEvent ¢) {
      if (¢ != null && ¢.getProperty() != null && ¢.getProperty().startsWith(TIPPER_CATEGORY_PREFIX))
        refreshNeeded.is = true;
      else if (¢ != null && ¢.getProperty() != null && ¢.getProperty().equals(NEW_PROJECTS_ENABLE_BY_DEFAULT_ID) && ¢.getNewValue() != null
          && ¢.getNewValue() instanceof Boolean)
        NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE.is = ((Boolean) ¢.getNewValue()).booleanValue();
    }
  }

  static class MBoolean {
    boolean is;

    public MBoolean(final boolean init) {
      is = init;
    }
  }
}
