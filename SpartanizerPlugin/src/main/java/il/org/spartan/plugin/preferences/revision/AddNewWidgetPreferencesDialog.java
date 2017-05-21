package il.org.spartan.plugin.preferences.revision;

import java.util.*;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import il.org.spartan.spartanizer.plugin.widget.*;


/** A dialog for the new widgets
 * 
 * @author Raviv Rachmiel
 * @since 2017-05-21 */
public class AddNewWidgetPreferencesDialog extends Dialog {
  private List<WidgetOperation> widgetOps;
  Button[] radioButtons;
  private WidgetOperation result;

  protected AddNewWidgetPreferencesDialog(Shell parentShell, List<WidgetOperation> widgetOps) {
    super(parentShell);
   this.widgetOps = new ArrayList<>(widgetOps);
  }
  
  @Override
  protected Control createDialogArea(Composite parent) {
      Composite container = (Composite) super.createDialogArea(parent);
      final GridData dataRes = new GridData();
      dataRes.grabExcessHorizontalSpace = true;
      dataRes.horizontalAlignment = GridData.FILL;
      radioButtons = new Button[widgetOps.size()];
      int count = 0;
      for (final WidgetOperation ¢ : widgetOps) {
        radioButtons[count] = new Button(container, SWT.RADIO);
        radioButtons[count].setSelection(false);
        radioButtons[count].setText(¢.description());
        radioButtons[count].setBounds(10, 25 * count + 5, 75, 30);
        ++count;
      }

      return container;
  }

  // overriding this methods allows you to set the
  // title of the custom dialog
  @Override
  protected void configureShell(Shell newShell) {
      super.configureShell(newShell);
      newShell.setText("Add a new Widget Operation");
  }

  @Override
  protected Point getInitialSize() {
      return new Point(450, 300);
  }
  
  public WidgetOperation getResult() {
    return result;
  }
  
  @Override
  protected void okPressed() {
    for (final Button ¢ : radioButtons)
      if (¢.getSelection()) {
        for (WidgetOperation w : widgetOps)
          if (w.description().equals(¢.getText())) {
            result = w;
            break;
          }
        break;
      }
      super.okPressed();
  }

}
