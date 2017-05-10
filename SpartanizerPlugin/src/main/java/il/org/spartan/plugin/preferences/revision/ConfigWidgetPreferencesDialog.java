package il.org.spartan.plugin.preferences.revision;

import java.util.*;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.*;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/** A dialog to descrive a configuration of an operation widget
 * @author Raviv Rachmiel
 * @since 2017-05-10 */
public class ConfigWidgetPreferencesDialog extends Dialog {
  String widgetName;
  String[][] configurations;
  IPreferenceStore store;

  public ConfigWidgetPreferencesDialog(Shell parentShell, String widgetName, String[][] configurations, IPreferenceStore store) {
    super(parentShell);
    this.widgetName = widgetName;
    this.configurations = configurations;
    this.store = store;
  }
  @Override protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    List<Text> textLists = new ArrayList<>();
    List<Button> buttonLists = new ArrayList<>();
    List<Button[]> radioLists = new ArrayList<>();
    for (String[] comp : configurations) {
      if ("String".equals(comp[1]))
        textLists.add(createString(container, comp[2], comp[3]));
      if ("Boolean".equals(comp[1]))
        buttonLists.add(createBoolean(container, comp[2], comp[3]));
      if("List".equals(comp[1])) 
        radioLists.add(createList(container,Arrays.copyOfRange(comp, 2, comp.length-1),comp[comp.length-1]));
        
    }
    return container;
  }
  // overriding this methods allows you to set the
  // title of the custom dialog
  @Override protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Configure The" + widgetName + " Widget Operation");
  }
  @Override protected Point getInitialSize() {
    return new Point(450, 300);
  }
  private static Text createString(Composite container, String name, String isRequired) {
    Label lbl = new Label(container, SWT.NONE);
    lbl.setText(name);
    GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    Text res = new Text(container, SWT.BORDER);
    res.setLayoutData(dataRes);
    Label lblReq = new Label(container, SWT.NONE);
    lblReq.setText(isRequired);
    return res;
  }
  private static Button createBoolean(Composite container, String name, String isRequired) {
    Label lbl = new Label(container, SWT.NONE);
    lbl.setText(name);
    GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    Button checkBox = new Button(container, SWT.CHECK);
    checkBox.setText(name);
    checkBox.setLayoutData(dataRes);
    Label lblReq = new Label(container, SWT.NONE);
    lblReq.setText(isRequired);
    return checkBox;
  }
  private static Button[] createList(Composite container, String[] options, String isRequired) {
    GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    Button[] radios = new Button[options.length];
    int count = 0;
    for(String n : options) {
      radios[count] = new Button(container, SWT.RADIO);
      radios[count].setSelection(false);
      radios[count].setText(n);
      radios[count].setBounds(10, 5+(25 * count), 75, 30);
      ++count;
    } 
    Label lblReq = new Label(container, SWT.NONE);
    lblReq.setText(isRequired);
    return radios;
  }
  @Override protected void okPressed() {
    //TODO: Raviv, add here the store preferences from the lists, change them to maps
    super.okPressed();
  }
}
