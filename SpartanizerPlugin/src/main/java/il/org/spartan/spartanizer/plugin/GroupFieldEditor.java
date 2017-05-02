package il.org.spartan.spartanizer.plugin;

import java.util.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

/** A {@link FieldEditor} designed to store multiple controls within a group
 * panel widget, to be used in conjunction with an
 * {@link FieldEditorPreferencePage} instance.
 * <p>
 * <u>Usage:</u><br>
 * 1) Create a new {@link GroupFieldEditor} object.<br>
 * 2) Add {@link FieldEditor} objects using the
 * {@link GroupFieldEditor#add(FieldEditor)} method. Each {@link FieldEditor}
 * should be initialized to have the return value of
 * {@link GroupFieldEditor#getFieldEditor()} as its parent.<br>
 * 3) Add the {@link GroupFieldEditor} to the parent as usual
 * @author alf (original)
 * @author Daniel Mittelman (fixed and revised)
 * @since 29/03/2016 */
public final class GroupFieldEditor extends FieldEditor {
  private static final int GROUP_PADDING = 8;
  private int numColumns;
  private final Collection<FieldEditor> members = an.empty.list();
  private final Group group;
  private final Composite parent;
  private boolean initialized;

  /** Create a group of {@link FieldEditor} objects
   * @param labelText (optional) the text that will appear in the top label. For
   *        no label, pass {@code null}
   * @param fieldEditorParent the widget's parent, usually
   *        {@link FieldEditorPreferencePage#getFieldEditorParent()} */
  public GroupFieldEditor(final String labelText, final Composite fieldEditorParent) {
    final String title = labelText == null ? "" : labelText;
    parent = fieldEditorParent;
    numColumns = 0;
    group = new Group(parent, SWT.SHADOW_OUT);
    group.setText(title);
  }

  /** Adds a new {@link FieldEditor} object to the group. Controls must be added
   * before the group is drawn to the parent. */
  public void add(final FieldEditor ¢) {
    if (initialized)
      throw new RuntimeException("The GroupFieldEditor has already been drawn, new fields cannot be added at this time");
    members.add(¢);
  }

  /** Returns the parent for all the FieldEditors inside of this group. In this
   * class, the actual {@link Group} object is returned
   * @return parent {@link Composite} object */
  public Composite getFieldEditor() {
    return group;
  }

  @Override public int getNumberOfControls() {
    return members.size();
  }

  /** Initializes using the currently added field editors. */
  public void init() {
    if (initialized)
      return;
    doFillintoGrid(getFieldEditor(), numColumns);
    initialized = true;
  }

  @Override public boolean isValid() {
    return members.stream().allMatch(FieldEditor::isValid);
  }

  @Override public void setEnabled(final boolean enabled, final Composite parentParam) {
    members.forEach(λ -> λ.setEnabled(enabled, parentParam));
  }

  @Override public void setFocus() {
    if (members != null && !members.isEmpty())
      members.iterator().next().setFocus();
  }

  @Override public void setPage(final DialogPage p) {
    members.forEach(λ -> λ.setPage(p));
  }

  @Override public void setPreferenceStore(final IPreferenceStore s) {
    super.setPreferenceStore(s);
    members.forEach(λ -> λ.setPreferenceStore(s));
  }

  @Override public void store() {
    doStore();
  }

  @Override protected void adjustForNumColumns(@SuppressWarnings("hiding") final int numColumns) {
    this.numColumns = numColumns;
  }

  /* (non-Javadoc) Method declared on FieldEditor. */
  protected void doFillintoGrid(final Composite parentParam, @SuppressWarnings("hiding") final int numColumns) {
    final Int c = new Int(numColumns);
    if (members == null || members.isEmpty())
      return;
    if (c.inner == 0)
      for (final FieldEditor ¢ : members) // NANO?
        c.inner = Math.max(c.inner, ¢.getNumberOfControls());
    gridData(c.inner);
    gridLayout(c.inner);
    members.forEach(λ -> λ.fillIntoGrid(parentParam, c.inner));
    parent.layout();
    parent.redraw();
  }

  @Override protected void doFillIntoGrid(final Composite __parent, final int __numColumns) {
    Object[] ____ = { __parent, box.it(__numColumns) };
    forget.em(____);
  }

  @Override protected void doLoad() {
    members.forEach(FieldEditor::load);
  }

  @Override protected void doLoadDefault() {
    members.forEach(FieldEditor::loadDefault);
  }

  @Override protected void doStore() {
    members.forEach(FieldEditor::store);
  }

  private void gridData(final int i) {
    final GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
    data.horizontalIndent = 2;
    data.verticalIndent = GROUP_PADDING;
    data.horizontalSpan = i;
    group.setLayoutData(data);
  }

  private void gridLayout(final int i) {
    final GridLayout groupLayout = new GridLayout();
    groupLayout.marginWidth = groupLayout.marginHeight = GROUP_PADDING;
    groupLayout.numColumns = i;
    group.setLayout(groupLayout);
  }
}
