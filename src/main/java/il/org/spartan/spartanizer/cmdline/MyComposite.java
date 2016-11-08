package il.org.spartan.spartanizer.cmdline;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class MyComposite extends Composite {
  @SuppressWarnings("unused") public static void main(final String[] args) {
    new MyComposite(null, SWT.NONE);
  }
  public MyComposite(final Composite parent, final int style) {
    super(parent, style);
    final Button button = new Button(parent, SWT.PUSH);
    button.setText("Press me");
    button.addSelectionListener(new SelectionAdapter() {
      @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
        openDialogs(parent.getShell());
      }
    });
  }
  static void openDialogs(final Shell s) {
    // File standard dialog
    final FileDialog fileDialog = new FileDialog(s);
    // Set the text
    fileDialog.setText("Select File");
    // Set filter on .txt files
    fileDialog.setFilterExtensions(new String[] { "*.txt" });
    // Put in a readable name for the filter
    fileDialog.setFilterNames(new String[] { "Textfiles(*.txt)" });
    // Open Dialog and save result of selection
    final String selected = fileDialog.open();
    System.out.println(selected);
    // Directly standard selection
    final DirectoryDialog dirDialog = new DirectoryDialog(s);
    dirDialog.setText("Select your home directory");
    final String selectedDir = dirDialog.open();
    System.out.println(selectedDir);
    // Select Font
    final FontDialog fontDialog = new FontDialog(s);
    fontDialog.setText("Select your favorite font");
    final FontData selectedFont = fontDialog.open();
    System.out.println(selectedFont);
    // Select Color
    final ColorDialog colorDialog = new ColorDialog(s);
    colorDialog.setText("Select your favorite color");
    final RGB selectedColor = colorDialog.open();
    System.out.println(selectedColor);
    // Message
    MessageBox messageDialog = new MessageBox(s, SWT.ERROR);
    messageDialog.setText("Evil Error has happend");
    messageDialog.setMessage("This is fatal!!!");
    int returnCode = messageDialog.open();
    System.out.println(returnCode);
    // Message with ok and cancel button and info icon
    messageDialog = new MessageBox(s, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
    messageDialog.setText("My info");
    messageDialog.setMessage("Do you really want to do this.");
    returnCode = messageDialog.open();
    System.out.println(returnCode);
  }
}