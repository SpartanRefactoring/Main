package il.org.spartan.spartanizer.plugin.widget.operations;

import static java.awt.MouseInfo.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import il.org.spartan.spartanizer.plugin.widget.*;

/** Widget operation that opens a text window to execute a cmd commmand
 * @author Yuval Simon
 * @since 2017-04-28 */
public class CmdOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x541BB50C344FDBF4L;

  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.wb.rcp/icons/info/Action/action.gif";
  }

  @Override public String description() {
    return "Execute a CMD command";
  }

  @Override public void onMouseUp(final WidgetContext xx) throws Throwable {
    createGui();
    super.onMouseUp(xx);
  }

  class CmdWindow {
    Shell shell;
    Display display;
    Text text;
    boolean blnMouseDown;
    int xPos;
    int yPos;

    public void open() {
      display = Display.getDefault();
      shell = new Shell(display, SWT.PRIMARY_MODAL);
      shell.setLocation(getPointerInfo().getLocation().x, getPointerInfo().getLocation().y);
      createContents();
      shell.open();
      shell.layout();
      shell.addMouseListener(new MouseListener() {
        @Override public void mouseUp(@SuppressWarnings("unused") final MouseEvent arg0) {
          blnMouseDown = false;
        }

        @Override public void mouseDown(final MouseEvent ¢) {
          blnMouseDown = true;
          xPos = ¢.x;
          yPos = ¢.y;
        }

        @Override public void mouseDoubleClick(@SuppressWarnings("unused") final MouseEvent arg0) {/***/
        }
      });
      shell.addMouseMoveListener(λ -> {
        if (blnMouseDown)
          shell.setLocation(λ.x + shell.getLocation().x - xPos, λ.y + shell.getLocation().y - yPos);
      });
      shell.addListener(SWT.FocusOut | SWT.Deactivate, (final Event __) -> display.close());
      while (!shell.isDisposed())
        if (!display.readAndDispatch())
          display.sleep();
    }

    protected void createContents() {
      shell.setSize(317, 42);
      shell.setText("SWT Application");
      text = new Text(shell, SWT.BORDER);
      text.setBounds(10, 10, 228, 21);
      text.addKeyListener(new KeyListener() {
        @Override public void keyReleased(@SuppressWarnings("unused") final KeyEvent __) {/***/
        }

        @Override public void keyPressed(final KeyEvent ¢) {
          if (¢.character == SWT.CR /* || e.character == SWT.LF */)
            go(text.getText());
        }
      });
      final Button btnExecute = new Button(shell, SWT.NONE);
      btnExecute.addSelectionListener(new SelectionAdapter() {
        @Override public void widgetSelected(@SuppressWarnings("unused") final SelectionEvent __) {
          go(text.getText());
          shell.close();
        }
      });
      btnExecute.setBounds(244, 8, 60, 25);
      btnExecute.setText("Execute");
    }
  }

  /** [[SuppressWarningsSpartan]] */
  static void go(final String command) {
    if (command == null || command.isEmpty())
      return;
    try {
      final Process pr = Runtime.getRuntime().exec(command);
      pr.waitFor();
      final BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String output;
      while ((output = input.readLine()) != null)
        System.out.println(output);
    } catch (final Exception ¢) {
      System.out.println(¢ + "");
    }
  }

  private void createGui() {
    try {
      new CmdWindow().open();
    } catch (final Exception ¢) {
      ¢.printStackTrace();
    }
  }
}
