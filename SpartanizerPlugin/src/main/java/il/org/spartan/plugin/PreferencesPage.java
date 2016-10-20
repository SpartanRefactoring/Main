package il.org.spartan.plugin;

import static il.org.spartan.plugin.PreferencesResources.*;
import static il.org.spartan.plugin.PreferencesResources.TipperGroup.*;

import java.util.*;
import java.util.List;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.swt.events.*;
import org.eclipse.ui.*;

import il.org.spartan.*;
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
    for (final TipperGroup ¢ : TipperGroup.values()) {
      final GroupFieldEditor g = new GroupFieldEditor(null, getFieldEditorParent());
      g.add(new BooleanFieldEditor(¢.id, ¢.label, g.getFieldEditor()));
      g.add(getListEditor(¢, g));
      g.init();
      addField(g);
    }
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

  private static FieldEditor getListEditor(final TipperGroup g, final GroupFieldEditor e) {
    return new TipsListEditor(g.label, "Available tippers", g, e);
  }

  static class TipsListEditor extends ListEditor {
    static final String DELIMETER = "|";
    final List<String> alive;
    final List<String> dead;
    final Selection selection;

    public TipsListEditor(final String name, final String labelText, final TipperGroup g, final GroupFieldEditor e) {
      super(name, labelText, e.getFieldEditor());
      alive = Toolbox.get(g);
      dead = new LinkedList<>();
      selection = new Selection();
      getAddButton().setText("Add");
      getDownButton().setEnabled(false);
      getDownButton().setVisible(false);
      getUpButton().setEnabled(false);
      getUpButton().setVisible(false);
      getRemoveButton().addSelectionListener(new SelectionAdapter() {
        /** [[SuppressWarningsSpartan]] */
        @SuppressWarnings("synthetic-access") @Override public void widgetSelected(SelectionEvent x) {
          if (x == null)
            return;
          if (getRemoveButton().equals(x.widget)) {
            final int i = selection.index;
            if (i >= 0) {
              final String r = selection.text;
              if (alive.contains(r)) {
                alive.remove(r);
                dead.add(r);
              }
            }
          }
        }
      });
      getList().addSelectionListener(new SelectionAdapter() {
        @SuppressWarnings("synthetic-access") @Override public void widgetSelected(SelectionEvent x) {
          if (x == null)
            return;
          selection.index = getList().getSelectionIndex();
          if (selection.index >= 0 && selection.index < getList().getItemCount())
            selection.text = getList().getItem(selection.index);
        }
      });
    }

    @Override protected String[] parseString(String stringList) {
      return stringList != null && !"".equals(stringList) ? stringList.split(DELIMETER) : alive.toArray(new String[alive.size()]);
    }

    @Override protected String getNewInputObject() {
      return dead.isEmpty() ? null : dead.remove(0);
    }

    @Override protected String createList(String[] items) {
      return separate.these(items).by(DELIMETER);
    }

    @Override public void createSelectionListener() {
      super.createSelectionListener();
    }

    static class Selection {
      int index = -1;
      String text;
    }
  }
}
