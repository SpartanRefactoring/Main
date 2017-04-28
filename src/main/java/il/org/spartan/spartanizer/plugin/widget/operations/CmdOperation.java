package il.org.spartan.spartanizer.plugin.widget.operations;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;
import static java.awt.MouseInfo.*;

import il.org.spartan.spartanizer.plugin.widget.*;

/** Widget operation that opens a text window to execute a cmd commmand
 * @author Yuval Simon
 * @since 2017-04-28 */
public class CmdOperation extends WidgetOperation {
  private static final long serialVersionUID = -6060636787579018228L;
  
  @Override public String imageURL() {
    return null;
  }

  @Override public String description() {
    return "Execute a CMD command";
  }
  
  @Override public void onMouseUp(WidgetContext xx) throws Throwable {
    createGui();
    super.onMouseUp(xx);
  }
  
  class CmdWindow {
    protected Shell shell;
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
        @Override
        public void mouseUp(@SuppressWarnings("unused") MouseEvent arg0) {
            blnMouseDown=false;
        }

        @Override
        public void mouseDown(MouseEvent ¢) {
            blnMouseDown=true;
            xPos=¢.x;
            yPos=¢.y;
        }

        @Override
        public void mouseDoubleClick(@SuppressWarnings("unused") MouseEvent arg0) {/***/}
      });
      shell.addMouseMoveListener(new MouseMoveListener() {
        @Override
        public void mouseMove(MouseEvent ¢) {
          if(blnMouseDown)
            shell.setLocation(shell.getLocation().x + (¢.x - xPos), shell.getLocation().y + (¢.y - yPos));
        }
      });

      shell.addListener(SWT.FocusOut | SWT.Deactivate, new Listener() {
        @Override public void handleEvent(@SuppressWarnings("unused") Event __) {display.close();}
      });
      
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
        @Override public void keyReleased(@SuppressWarnings("unused") KeyEvent __) {/***/}
        
        @Override public void keyPressed(KeyEvent ¢) {
          if(¢.character == SWT.CR /*|| e.character == SWT.LF*/)
            go(text.getText());
        }
      });
      
      Button btnExecute = new Button(shell, SWT.NONE);
      btnExecute.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(@SuppressWarnings("unused") SelectionEvent __) {
          go(text.getText());
        }
      });
      btnExecute.setBounds(244, 8, 60, 25);
      btnExecute.setText("Execute");
    }
    
    /**
     * [[SuppressWarningsSpartan]]
     */
    void go(String command) {
      try {
        Process pr = Runtime.getRuntime().exec(command);  
        pr.waitFor(); 
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String output;
        while ((output = input.readLine()) != null) {
          System.out.println(output);
        }
      } 
      catch (Exception ¢) {
        System.out.println(¢ + "");
      }
      shell.close();
    }
  }
  
  private void createGui() {
    try {
      (new CmdWindow()).open();
    } catch (Exception ¢) {
      ¢.printStackTrace();
    }
  }
}
