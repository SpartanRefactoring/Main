package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.PreferencesResources.*;
import static il.org.spartan.plugin.preferences.PreferencesResources.TipperGroup.*;

import java.util.*;
import java.util.List;
import java.util.Map.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.plugin.preferences.revision.XMLSpartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO Ori Roth: document class {@link }
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-02-24 */
public class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  private final SpartanPropertyListener listener;
  private final AtomicBoolean refreshNeeded;
  private Changes changes;

  public PreferencesPage() {
    super(GRID);
    refreshNeeded = new AtomicBoolean(false);
    listener = new SpartanPropertyListener(refreshNeeded);
  }

  @Override public boolean performOk() {
    final boolean $ = super.performOk();
    changes.commit();
    return $;
  }

  /** Build the preferences page by adding controls */
  @Override public void createFieldEditors() {
    List<Entry<String, Object>> ps = getProjects();
    changes = new Changes(ps.stream().map(Entry::getValue).collect(Collectors.toList()));
    addField(new BooleanFieldEditor(NEW_PROJECTS_ENABLE_BY_DEFAULT_ID, NEW_PROJECTS_ENABLE_BY_DEFAULT_TEXT, getFieldEditorParent()));
    addField(new ListSelectionEditor("X", "Configure tips for projects:", getFieldEditorParent(), ps,
        p -> ProjectPreferencesHandler.execute((IProject) p, changes.getPreference((IProject) p), (pp, es) -> changes.update(pp, es)), //
        p -> changes.getAble((IProject) p), //
        p -> changes.update((IProject) p, changes.getAble((IProject) p).booleanValue() ? Boolean.FALSE : Boolean.TRUE) //
    ));
  }

  /** TODO Ori Roth: Stub 'PreferencesPage::getProjects' (created on
   * 2017-02-24)." );
   * <p>
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  private static List<Entry<String, Object>> getProjects() {
    List<Entry<String, Object>> $ = new LinkedList<>();
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    for (IProject p : workspaceRoot.getProjects()) {
      try {
        if (p.isOpen() && p.hasNature(JavaCore.NATURE_ID))
          $.add(new AbstractMap.SimpleEntry<>(p.getName(), p));
      } catch (CoreException x) {
        monitor.log(x);
      }
    }
    return $;
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

    SpartanPropertyListener(final AtomicBoolean refreshNeeded) {
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

  static class ListSelectionEditor extends ListEditor {
    static final String DELIMETER = ",";
    final List<Map.Entry<String, Object>> elements;
    Button configureButton;
    Button ableButton;

    ListSelectionEditor(final String name, final String labelText, final Composite parent, final List<Map.Entry<String, Object>> elements,
        final Consumer<Object> onConfigure, final Function<Object, Boolean> isAble, final Consumer<Object> onAble) {
      super(name, labelText, parent);
      this.elements = new ArrayList<>();
      this.elements.addAll(elements);
      Composite buttonBox = new Composite(parent, SWT.NULL);
      GridLayout layout = new GridLayout();
      layout.marginWidth = 0;
      buttonBox.setLayout(layout);
      buttonBox.addDisposeListener(event -> {
        configureButton = null;
        ableButton = null;
      });
      buttonBox.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
      ableButton = new Button(buttonBox, SWT.PUSH);
      ableButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      ableButton.setText("Enable/Disable tips");
      ableButton.setEnabled(false);
      ableButton.setVisible(true);
      ableButton.addSelectionListener(new SelectionListener() {
        @Override public void widgetSelected(@SuppressWarnings("unused") SelectionEvent __) {
          onSelection();
        }

        @Override public void widgetDefaultSelected(@SuppressWarnings("unused") SelectionEvent __) {
          onSelection();
        }

        @SuppressWarnings("synthetic-access") private void onSelection() {
          int i = getList().getSelectionIndex();
          if (i >= 0) {
            onAble.accept(elements.get(i).getValue());
            if (ableButton.getText().equals("Disable tips")) {
              ableButton.setText("Enable tips");
              configureButton.setEnabled(false);
            } else if (ableButton.getText().equals("Enable tips")) {
              ableButton.setText("Disable tips");
              configureButton.setEnabled(true);
            }
          }
        }
      });
      configureButton = new Button(buttonBox, SWT.PUSH);
      configureButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
      configureButton.setText("Configure tips");
      configureButton.setEnabled(false);
      configureButton.setVisible(true);
      configureButton.addSelectionListener(new SelectionListener() {
        @Override public void widgetSelected(@SuppressWarnings("unused") SelectionEvent __) {
          onSelection();
        }

        @Override public void widgetDefaultSelected(@SuppressWarnings("unused") SelectionEvent __) {
          onSelection();
        }

        @SuppressWarnings("synthetic-access") private void onSelection() {
          int i = getList().getSelectionIndex();
          if (i >= 0)
            onConfigure.accept(elements.get(i).getValue());
        }
      });
      parent.addDisposeListener(e -> {
        configureButton = null;
        ableButton = null;
      });
      getList().addSelectionListener(new SelectionListener() {
        @SuppressWarnings("synthetic-access") @Override public void widgetSelected(@SuppressWarnings("unused") SelectionEvent __) {
          int i = getList().getSelectionIndex();
          if (i >= 0)
            if (isAble.apply(elements.get(i).getValue()).booleanValue()) {
              ableButton.setText("Disable tips");
              configureButton.setEnabled(true);
            } else {
              ableButton.setText("Enable tips");
              configureButton.setEnabled(false);
            }
        }

        @Override public void widgetDefaultSelected(@SuppressWarnings("unused") SelectionEvent __) {
          //
        }
      });
    }

    @Override protected void doFillIntoGrid(Composite parent, int numColumns) {
      super.doFillIntoGrid(parent, numColumns);
      getButtonBoxControl(parent).dispose();
    }

    @Override protected String[] parseString(final String stringList) {
      if (stringList != null && !stringList.isEmpty())
        return stringList.split(DELIMETER);
      List<String> $ = new LinkedList<>();
      for (Entry<String, Object> e : elements)
        $.add(e.getKey());
      return $.toArray(new String[elements.size()]);
    }

    @Override protected String getNewInputObject() {
      return null;
    }

    @Override protected String createList(final String[] items) {
      return separate.these(items).by(DELIMETER);
    }

    @Override protected void selectionChanged() {
      if (getList() == null || getList().getSelectionIndex() < 0)
        return;
      if (ableButton != null)
        ableButton.setEnabled(true);
    }
  }

  static class Changes implements Cloneable {
    private Map<IProject, Map<SpartanCategory, SpartanTipper[]>> preferences1;
    private Map<IProject, Set<String>> preferences2;
    private Map<IProject, Boolean> ables;

    private Changes() {
      preferences1 = new HashMap<>();
      preferences2 = new HashMap<>();
      ables = new HashMap<>();
    }

    public Changes(List<Object> projects) {
      preferences1 = new HashMap<>();
      preferences2 = new HashMap<>();
      ables = new HashMap<>();
      for (Object p : projects) {
        preferences1.put((IProject) p, null);
        preferences2.put((IProject) p, null);
        ables.put((IProject) p, null);
      }
    }

    @Override protected Object clone() {
      Changes $ = new Changes();
      $.preferences1.putAll(preferences1);
      $.preferences2.putAll(preferences2);
      $.ables.putAll(ables);
      return $;
    }

    public Map<SpartanCategory, SpartanTipper[]> getPreference(IProject p) {
      Map<SpartanCategory, SpartanTipper[]> $ = preferences1.get(p);
      if ($ == null) {
        $ = XMLSpartan.getTippersByCategories(p, false);
        preferences1.put(p, $);
      }
      return $;
    }

    public Boolean getAble(IProject p) {
      Boolean $ = ables.get(p);
      if ($ == null) {
        try {
          $ = Boolean.valueOf(p.hasNature(Nature.NATURE_ID));
        } catch (CoreException x) {
          monitor.log(x);
          $ = Boolean.FALSE;
        }
      }
      return $;
    }

    public void update(IProject p, Boolean able) {
      ables.put(p, able);
    }

    public Void update(IProject p, Set<String> preference) {
      preferences2.put(p, preference);
      for (SpartanTipper[] ts : preferences1.get(p).values())
        for (SpartanTipper t : ts)
          t.enable(preference.contains(t.name()));
      return null;
    }

    public synchronized void commit() {
      ((Changes) clone()).commitSelf();
      for (IProject p : preferences1.keySet()) {
        preferences1.put(p, null);
        preferences2.put(p, null);
        ables.put(p, null);
      }
    }

    private void commitSelf() {
      new Job("Applying preferences changes") {
        @SuppressWarnings("synthetic-access") @Override protected IStatus run(IProgressMonitor m) {
          m.beginTask("Applying preferences changes", preferences2.keySet().size());
          for (IProject p : preferences2.keySet()) {
            if (preferences2.get(p) != null)
              ProjectPreferencesHandler.commit(p, preferences2.get(p));
            if (ables.get(p) != null)
              try {
                TipsOnOffToggle.toggleNature(p, ables.get(p).booleanValue());
              } catch (CoreException x) {
                monitor.log(x);
              }
            m.worked(1);
          }
          m.done();
          return Status.OK_STATUS;
        }
      }.schedule();
    }
  }
}
