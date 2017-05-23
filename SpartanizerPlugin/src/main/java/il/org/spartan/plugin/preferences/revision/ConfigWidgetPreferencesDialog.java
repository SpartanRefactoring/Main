package il.org.spartan.plugin.preferences.revision;

import static il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

import java.io.*;
import java.util.*;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import fluent.ly.*;

/** A dialog to descrive a configuration of an operation widget
 * @author Raviv Rachmiel
 * @since 2017-05-10 */
// TODO: Raviv Rachmiel, make use of the required column in configurations
public class ConfigWidgetPreferencesDialog extends Dialog {
  String widgetName;
  String[][] configurations;
  IPreferenceStore store;
  long widgetSerialID;

  public ConfigWidgetPreferencesDialog(final Shell parentShell, final String widgetName, final String[][] configurations, final long widgetSerialID,
      final IPreferenceStore store) {
    super(parentShell);
    this.widgetName = widgetName;
    this.configurations = configurations;
    this.store = store;
    this.widgetSerialID = widgetSerialID;
  }
  @Override protected Control createDialogArea(final Composite parent) {
    final Composite $ = (Composite) super.createDialogArea(parent);
    for (final String[] comp : configurations) {
      if ("String".equals(comp[1]))
        comp[2] = createString($, comp[2]);
      if ("Boolean".equals(comp[1]))
        comp[2] = createBoolean($, comp[2]) + "";
      else { // if("List".equals(comp[1]))
        final Button[] res = createList($, Arrays.copyOfRange(comp, 2, comp.length - 1));
        int count = 2;
        for (final Button ¢ : res) {
          if (¢.getSelection())
            comp[count] += "-V";
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
  private static String createString(final Composite container, final String name) {
    new Label(container, SWT.NONE).setText(name);
    final GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    final Text $ = new Text(container, SWT.BORDER);
    $.setLayoutData(dataRes);
    return $.getText();
  }
  @SuppressWarnings("boxing") private static Boolean createBoolean(final Composite container, final String name) {
    new Label(container, SWT.NONE).setText(name);
    final GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    final Button $ = new Button(container, SWT.CHECK);
    $.setText(name);
    $.setLayoutData(dataRes);
    return $.getSelection();
  }
  private static Button[] createList(final Composite container, final String[] options) {
    final GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    final Button[] $ = new Button[options.length];
    int count = 0;
    for (final String ¢ : options) {
      $[count] = new Button(container, SWT.RADIO);
      $[count].setSelection(false);
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
    store().setValue("CONF_" + widgetSerialID, String.valueOf(Base64.getEncoder().encode(out.toByteArray())));
    // TODO: Niv, in order to read, use - ByteArrayInputStream in = new
    // ByteArrayInputStream(Base64.getDecoder().decode(yourString.toCharArray()));
    super.okPressed();
  }
}
