package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;
import java.util.List;

import java.util.function.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** A widget containing a list of projects and some buttons. Used to configure
 * specific operations. "configure" button is used to open a dialog, allowing
 * the user to define configurations for the operation "en/disable" button
 * allows the user to toggle spartanization operations to appear or disappear
 * from the widget.
 * @author Raviv Rachmiel
 * @since 2017-05-10 */
public class OperationListEditor extends ListEditor {
  static final String DELIMETER = ",";
  List<Map.Entry<String, Object>> elements_list;
  Button configureButton;
  Button ableButton;
  public ListEditor resLE;

  public OperationListEditor(final String name, final String labelText, final Composite parent) {
    super(name, labelText, parent);
    final Composite buttonBox = new Composite(parent, SWT.NULL);
    final GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    buttonBox.setLayout(layout);
    buttonBox.addDisposeListener(λ -> {
      configureButton = null;
      ableButton = null;
    });
    buttonBox.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    ableButton = new Button(buttonBox, SWT.PUSH);
    ableButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    ableButton.setText("Enable/Disable operations");
    ableButton.setEnabled(false);
    ableButton.setVisible(true);
    configureButton = new Button(buttonBox, SWT.PUSH);
    configureButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    configureButton.setText("Configure operation");
    configureButton.setEnabled(false);
  }
  
  private int getRealChosenOpIndex(final int i, final List<WidgetOperationEntry> es) {
    WidgetOperationEntry chosen = es.get(0);
    for (final WidgetOperationEntry ¢ : es)
      if (¢.getName().equals(getList().getItem(i)))
        chosen = ¢;
    final int realIndex = es.indexOf(chosen);
    return realIndex;
  }
  
  public void addDefaultButtonsConfig() {
    getDownButton().addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @Override public void widgetDefaultSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @SuppressWarnings("synthetic-access") void onSelection() {
        final int i = getList().getSelectionIndex();
        if (i < 0)
          return;
        final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
        final int realIndex = getRealChosenOpIndex(i, l);
        if (l.size() - realIndex <= 1)
          return;
        // else
        Collections.swap(l, realIndex, realIndex + 1);
        WidgetPreferences.storeEntries(l);
        resLE.loadDefault();
      }
      
    });
    getUpButton().addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @Override public void widgetDefaultSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @SuppressWarnings("synthetic-access") void onSelection() {
        final int i = getList().getSelectionIndex();
        if (i < 0)
          return;
        final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
        // else
        WidgetOperationEntry chosen = l.get(0);
        for (final WidgetOperationEntry ¢ : l)
          if (¢.getName().equals(getList().getItem(i)))
            chosen = ¢;
        final int realIndex = l.indexOf(chosen);
        if (realIndex == 0)
          return;
        Collections.swap(l, realIndex - 1, realIndex);
        WidgetPreferences.storeEntries(l);
        resLE.loadDefault();
      }
    });
    getRemoveButton().addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @Override public void widgetDefaultSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      void onSelection() {
        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Not available yet",
            "will be available in later releases");
      //comment out next command in order to get remove functionality:
        //removeFunctionality();
      }
      @SuppressWarnings({ "unused", "synthetic-access" }) void removeFunctionality() {
        final int i = getList().getSelectionIndex();
        if (i < 0)
          return;
        final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
        WidgetOperationEntry chosen = l.get(0);
        for (final WidgetOperationEntry ¢ : l)
          if (¢.getName().equals(getList().getItem(i)))
            chosen = ¢;
        final int realIndex = l.indexOf(chosen);
        l.get(realIndex).disable();
        l.remove(realIndex);
        WidgetPreferences.storeEntries(l);
        resLE.loadDefault();
      }
    });
    //comment out next 2 commands in order to get remove functionality:
    getRemoveButton().setEnabled(false);
    getRemoveButton().setVisible(false);
  }
  public OperationListEditor lazyConstruct(final Composite parent, final List<Map.Entry<String, Object>> elements, final Consumer<Object> onConfigure,
      final Function<Object, Boolean> isAble, final Consumer<Object> onAble, final ListEditor e) {
    elements_list = as.list(elements);
    resLE = e;
    addDefaultButtonsConfig();
    ableButton.addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @Override public void widgetDefaultSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @SuppressWarnings("synthetic-access") void onSelection() {
        final int i = getList().getSelectionIndex();
        if (i < 0)
          return;
        final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
        int realIndex = getRealChosenOpIndex(i, l);
        onAble.accept(l.get(realIndex));
        if (isAble.apply(l.get(realIndex)).booleanValue()) {
          ableButton.setText("Disable operation");
          configureButton.setEnabled(true);
        } else {
          ableButton.setText("Enable operation");
          configureButton.setEnabled(false);
        }
      }
    });
    configureButton.setVisible(true);
    configureButton.addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @Override public void widgetDefaultSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        onSelection();
      }
      @SuppressWarnings("synthetic-access") void onSelection() {
        final int i = getList().getSelectionIndex();
        if (i >= 0)
          onConfigure.accept(elements_list.get(i).getValue()); // perform the on
                                                               // configure on
                                                               // widget op
        loadDefault();
      }
    });
    parent.addDisposeListener(λ -> {
      configureButton = null;
      ableButton = null;
    });
    getList().addSelectionListener(new SelectionListener() {
      @Override @SuppressWarnings("synthetic-access") public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        final int i = getList().getSelectionIndex();
        final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
        if (i >= 0)
          if (isAble.apply(l.get(getRealChosenOpIndex(i, l))).booleanValue()) {
            ableButton.setText("Disable operations");
            configureButton.setEnabled(true);
          } else {
            ableButton.setText("Enable operations");
            configureButton.setEnabled(false);
          }
      }
      @Override public void widgetDefaultSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        //
      }
    });
    return this;
  }
  @Override protected void doFillIntoGrid(final Composite parent, final int numColumns) {
    super.doFillIntoGrid(parent, numColumns);
    // getButtonBoxControl(parent).dispose(); // removing this will add the
    // ADD,REMOVE,DOWN,UP buttons
  }
  @Override protected String[] parseString(@SuppressWarnings("unused") final String stringList) {
    final List<String> $ = new ArrayList<>();
    final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
    for (final WidgetOperationEntry ¢ : l)
      if (¢ != null)
        $.add(¢.getName());
    // when you want to initialize all preferences - uncomment the next line:
     return $.toArray(new String[$.size()]);
    //return stringList != null && !stringList.isEmpty() ? stringList.split(DELIMETER) : $.toArray(new String[$.size()]);
  }
  @Override protected String getNewInputObject() {
    final AddNewWidgetPreferencesDialog $ = new AddNewWidgetPreferencesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    $.open();
    final String res = $.getResult() == null ? null : $.getName();
    if (res == null)
      return res;
    final long serialVersionUID = ObjectStreamClass.lookup($.getResult().getClass()).getSerialVersionUID();
    final WidgetOperationEntry woe = new WidgetOperationEntry(serialVersionUID, new HashMap<>(), res);
    woe.disable();
    if ($.getResult().defaultConfiguration() == null)
      new ConfigWidgetPreferencesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), woe, PreferencesResources.store()).open();
    elements_list.add(new AbstractMap.SimpleEntry<>(res, woe));
    final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
    l.add(woe);
    WidgetPreferences.storeEntries(l);
    resLE.loadDefault();
    loadDefault();
    return null;
  }
  @Override protected String createList(final String[] items) {
    return separate.these(items).by(DELIMETER);
  }
  @Override protected void selectionChanged() {
    if (getList() != null && getList().getSelectionIndex() >= 0 && ableButton != null)
      ableButton.setEnabled(true);
    final int index = getList().getSelectionIndex(), size = getList().getItemCount();
    getRemoveButton().setEnabled(index >= 0);
    getUpButton().setEnabled(size > 1 && index > 0);
    getDownButton().setEnabled(size > 1 && index >= 0 && index < size - 1);
  }
}