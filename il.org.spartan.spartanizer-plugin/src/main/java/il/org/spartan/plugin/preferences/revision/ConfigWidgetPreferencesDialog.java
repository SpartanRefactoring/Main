package il.org.spartan.plugin.preferences.revision;

import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** A dialog to descrive a configuration of an operation widget
 * @author Raviv Rachmiel
 * @since 2017-05-10 */
// TODO: Raviv Rachmiel, make use of the required column in configurations
public class ConfigWidgetPreferencesDialog extends Dialog {
  String widgetName;
  IPreferenceStore store;
  long widgetSerialID;
  String[][] configurations;
  Map<String, String> confMap;
  WidgetOperationEntry woe;
  Text resName;

  public ConfigWidgetPreferencesDialog(final Shell parentShell, final WidgetOperationEntry woe, final IPreferenceStore store) {
    super(parentShell);
    widgetName = woe.getName();
    this.store = store;
    widgetSerialID = woe.widgetSUID;
    this.woe = woe;
    configurations = woe.getWidgetOp().configurationComponents();
    confMap = new HashMap<>();
  }
  @Override protected Control createDialogArea(final Composite parent) {
    final Composite $ = (Composite) super.createDialogArea(parent);
    resName = createString($, "Widget Name", widgetName);
    for (final String[] comp : configurations) {
      if ("String".equals(comp[1]))
        confMap.put(comp[0], createString($, comp[2], woe.getConfiguration().get(comp[0])).getText());
      if ("Boolean".equals(comp[1]))
        confMap.put(comp[0], createBoolean($, comp[2], woe.getConfiguration().get(comp[0])));
      else { // if("List".equals(comp[1]))
        final Button[] res = createList($, Arrays.copyOfRange(comp, 2, comp.length - 1), woe.getConfiguration().get(comp[0]));
        int count = 2;
        for (final Button ¢ : res) {
          if (¢.getSelection()) {
            confMap.put(comp[0], comp[count]);
            comp[count] += "-V";
          }
          ++count;
        }
      }
    }
    return $;
  }
  // overriding this methods allows you to set the
  // title of the custom dialog
  @Override protected void configureShell(final Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Configure The" + widgetName + " Widget Operation");
  }
  @Override protected Point getInitialSize() {
    return new Point(450, 300);
  }
  private static Text createString(final Composite container, final String name, final String defaultValue) {
    new Label(container, SWT.NONE).setText(name);
    final GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    final Text $ = new Text(container, SWT.BORDER);
    if(defaultValue!=null)
      $.setText(defaultValue);
    $.setLayoutData(dataRes);
    return $;
  }
  private static String createBoolean(final Composite container, final String name, final String defaultValue) {
    new Label(container, SWT.NONE).setText(name);
    final GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    final Button $ = new Button(container, SWT.CHECK);
    $.setLayoutData(dataRes);
    if(defaultValue!=null)
      $.setSelection("true".equals(defaultValue.toLowerCase()));
    return $.getSelection() ? "true" : "false";
  }
  private static Button[] createList(final Composite container, final String[] options, final String defaultValue) {
    final GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    final Button[] $ = new Button[options.length];
    int count = 0;
    for (final String ¢ : options) {
      $[count] = new Button(container, SWT.RADIO);
      $[count].setSelection(false);
      if (defaultValue!=null && ¢.equals(defaultValue))
        $[count].setSelection(true);
      $[count].setText(¢);
      $[count].setBounds(10, 25 * count + 5, 75, 30);
      ++count;
    }
    return $;
  }
  @Override protected void okPressed() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      new ObjectOutputStream(out).writeObject(configurations);
    } catch (@SuppressWarnings("unused") final IOException x) {
      note.bug();
    }
    final List<WidgetOperationEntry> l = WidgetPreferences.readEntries();
    l.get(l.indexOf(woe)).setConfMap(confMap);
    l.get(l.indexOf(woe)).setName(resName.getText());
    woe.setConfMap(confMap);
    woe.setName(resName.getText());
    WidgetPreferences.storeEntries(l);
    super.okPressed();
  }
}
