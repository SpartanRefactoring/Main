package il.org.spartan.plugin;

import static il.org.spartan.plugin.PreferencesResources.*;
import static il.org.spartan.plugin.PreferencesResources.TipperGroup.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.*;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.*;

import il.org.spartan.*;
import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;

/** ??
 * @author Daniel Mittelman
 * @year 2016
 * @author Ori Roth
 * @since 2.6 */
public final class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  public static final String TIPPER_COMBO_OPTIONS[][] = { { "Enabled", "on" }, { "Disabled", "off" } };
  private final SpartanPropertyListener listener;
  private final AtomicBoolean refreshNeeded;

  public PreferencesPage() {
    super(GRID);
    refreshNeeded = new AtomicBoolean(false);
    listener = new SpartanPropertyListener(refreshNeeded);
  }
  @Override public boolean performOk() {
    refreshNeeded.set(false);
    final boolean $ = super.performOk();
    if (refreshNeeded.get())
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
    // addField(new ComboFieldEditor(PLUGIN_STARTUP_BEHAVIOR_ID,
    // PLUGIN_STARTUP_BEHAVIOR_TEXT, PLUGIN_STARTUP_BEHAVIOR_OPTIONS,
    // getFieldEditorParent()));
    addField(new BooleanFieldEditor(NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT, getFieldEditorParent()));
    final GroupFieldEditor g = new GroupFieldEditor("Enabled spartanizations", getFieldEditorParent());
    for (final TipperGroup ¢ : TipperGroup.values())
      g.add(new BooleanFieldEditor(¢.id, ¢.label, g.getFieldEditor()));
    addField(g);
    g.init();
    // XXX: this is an experiment I made in order to improve preferences page.
    // Aftermath: consider using {@link PreferencePage} or a view (part)
    // instead.
    // for (final TipperGroup ¢ : TipperGroup.values()) {
    // final GroupFieldEditor g = new GroupFieldEditor(null,
    // getFieldEditorParent());
    // g.add(new BooleanFieldEditor(¢.id, ¢.label, g.getFieldEditor()));
    // g.add(getListEditor(¢, g));
    // g.init();
    // addField(g);
    // }
  }
  @Override public void init(@SuppressWarnings("unused") final IWorkbench __) {
    setPreferenceStore(TipperGroup.store());
    setDescription(PAGE_DESCRIPTION);
    store().addPropertyChangeListener(listener);
  }

  /** An event handler used to re-initialize the {@link Trimmer} spartanization
   * once a tipper preference was modified. */
  static class SpartanPropertyListener implements IPropertyChangeListener {
    private final AtomicBoolean refreshNeeded;

    public SpartanPropertyListener(final AtomicBoolean refreshNeeded) {
      this.refreshNeeded = refreshNeeded;
    }
    @Override public void propertyChange(final PropertyChangeEvent ¢) {
      if (¢ != null && ¢.getProperty() != null && ¢.getProperty().startsWith(TIPPER_CATEGORY_PREFIX))
        refreshNeeded.set(true);
      else if (¢ != null && ¢.getProperty() != null && ¢.getProperty().equals(NEW_PROJECTS_ENABLE_BY_DEFAULT_ID) && ¢.getNewValue() != null
          && ¢.getNewValue() instanceof Boolean)
        NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE.set(((Boolean) ¢.getNewValue()).booleanValue());
    }
  }

  @SuppressWarnings("unused") private static FieldEditor getListEditor(final TipperGroup g, final GroupFieldEditor e) {
    return new TipsListEditor(g.label, "Available tippers", g, e);
  }

  static class TipsListEditor extends ListEditor {
    static final String DELIMETER = "|";
    final Composite composite;
    final List<String> alive;
    final List<String> dead;
    final Selection selection;

    public TipsListEditor(final String name, final String labelText, final TipperGroup g, final GroupFieldEditor e) {
      super(name, labelText, e.getFieldEditor());
      composite = e.getFieldEditor();
      alive = Toolbox.get(g);
      dead = new LinkedList<>();
      selection = new Selection();
      getAddButton().setText("Add");
      getAddButton().setEnabled(false);
      getDownButton().setEnabled(false);
      getDownButton().setVisible(false);
      getUpButton().setEnabled(false);
      getUpButton().setVisible(false);
      getRemoveButton().addSelectionListener(new SelectionAdapter() {
        /** [[SuppressWarningsSpartan]] */
        @SuppressWarnings("synthetic-access") @Override public void widgetSelected(final SelectionEvent x) {
          if (x == null)
            return;
          if (getRemoveButton().equals(x.widget)) {
            final int i = selection.index;
            if (i >= 0) {
              final String r = selection.text;
              if (alive.contains(r)) {
                alive.remove(r);
                dead.add(r);
                getAddButton().setEnabled(true);
                if (getList().getItemCount() > 0)
                  selection.text = getList().getItem(selection.index);
                else {
                  selection.index = -1;
                  selection.text = null;
                }
              }
            }
          }
        }
      });
      getList().addSelectionListener(new SelectionAdapter() {
        @SuppressWarnings("synthetic-access") @Override public void widgetSelected(final SelectionEvent x) {
          if (x == null)
            return;
          selection.index = getList().getSelectionIndex();
          if (selection.index >= 0 && selection.index < getList().getItemCount())
            selection.text = getList().getItem(selection.index);
        }
      });
    }
    @Override protected String[] parseString(final String stringList) {
      return stringList != null && !"".equals(stringList) ? stringList.split(DELIMETER) : alive.toArray(new String[alive.size()]);
    }
    @SuppressWarnings("unused") @Override protected String getNewInputObject() {
      if (dead.isEmpty() || composite == null)
        return null;
      final ListDialog d = new ListDialog(composite.getShell());
      d.setContentProvider(inputElement -> dead.toArray(new String[dead.size()]));
      d.setLabelProvider(new ILabelProvider() {
        @Override public void removeListener(final ILabelProviderListener __) {
          //
        }
        @Override public boolean isLabelProperty(final Object element, final String property) {
          return false;
        }
        @Override public void dispose() {
          //
        }
        @Override public void addListener(final ILabelProviderListener __) {
          //
        }
        @Override public String getText(final Object element) {
          return element + "";
        }
        @Override public Image getImage(final Object element) {
          return null;
        }
      });
      d.setBlockOnOpen(true);
      d.setTitle("Add tipper");
      d.setMessage("Select a tipper to activate:");
      d.setInput(dead);
      d.open();
      final Object[] os = d.getResult();
      if (os == null || os.length == 0)
        return null;
      final String $ = os[0] + "";
      dead.remove($);
      if (dead.isEmpty())
        getAddButton().setEnabled(false);
      alive.add($);
      return $;
    }
    @Override protected String createList(final String[] items) {
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
