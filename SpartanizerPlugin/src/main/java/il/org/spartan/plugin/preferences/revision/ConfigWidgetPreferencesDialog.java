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
//TODO: Raviv Rachmiel, make use of the required column in configurations
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
    Composite $ = (Composite) super.createDialogArea(parent);
    List<Text> textLists = new ArrayList<>();
    List<Button> buttonLists = new ArrayList<>();
    List<Button[]> radioLists = new ArrayList<>();
    for (String[] comp : configurations) {
      if ("String".equals(comp[1]))
        textLists.add(createString($, comp[2]));
      if ("Boolean".equals(comp[1]))
        buttonLists.add(createBoolean($, comp[2]));
      if("List".equals(comp[1])) 
        radioLists.add(createList($,Arrays.copyOfRange(comp, 2, comp.length-1)));
        
    }
    return $;
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
  private static Text createString(Composite container, String name) {
    Label lbl = new Label(container, SWT.NONE);
    lbl.setText(name);
    GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    Text $ = new Text(container, SWT.BORDER);
    $.setLayoutData(dataRes);
    
    return $;
  }
  private static Button createBoolean(Composite container, String name) {
    Label lbl = new Label(container, SWT.NONE);
    lbl.setText(name);
    GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    Button $ = new Button(container, SWT.CHECK);
    $.setText(name);
    $.setLayoutData(dataRes);
    
    return $;
  }
  private static Button[] createList(Composite container, String[] options) {
    GridData dataRes = new GridData();
    dataRes.grabExcessHorizontalSpace = true;
    dataRes.horizontalAlignment = GridData.FILL;
    Button[] $ = new Button[options.length];
    int count = 0;
    for(String ¢ : options) {
      $[count] = new Button(container, SWT.RADIO);
      $[count].setSelection(false);
      $[count].setText(¢);
      $[count].setBounds(10, 25 * count + 5, 75, 30);
      ++count;
    } 
    
    return $;
  }
  @Override protected void okPressed() {
    //TODO: Raviv, add here the store preferences from the lists, change them to maps
    super.okPressed();
  }
}
