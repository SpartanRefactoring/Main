package il.org.spartan.spartanizer.plugin.widget.operations;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;


import il.org.spartan.spartanizer.plugin.widget.*;

/** TODO Yuval Simon: document class 
 * 
 * @author Yuval Simon
 * @since 2017-04-28 */
public class CmdOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x541BB50C344FDBF4L;
  
  @Override public String imageURL() {
    return null;
  }

  @Override public String description() {
    return "Execute a CMD command";
  }
  
  @Override public void onMouseUp(WidgetContext __) throws Throwable {
    createGui();
    super.onMouseUp(__);
  }
  
  class CmdWindow {
    protected Shell shell;
    Display display;
    Text text;

    public void open() {
      display = Display.getDefault();
      shell = new Shell(display, SWT.PRIMARY_MODAL);
      createContents();
      shell.open();
      shell.layout();
      shell.setLocation(200, 200); // TODO: change to widget location
      display.addListener(SWT.FocusOut, new Listener() {
        @Override public void handleEvent(@SuppressWarnings("unused") Event __) {display.close();}
      });
      
      while (!shell.isDisposed())
        if (!display.readAndDispatch())
          display.sleep();
    }

    protected void createContents() {
      shell.setSize(317, 42);
      shell.setText("SWT Application");
      
      Composite composite = new Composite(shell, SWT.NONE);
      composite.setBounds(0, 0, 315, 40);
      
      text = new Text(composite, SWT.BORDER);
      text.setBounds(10, 10, 228, 21);
      text.addKeyListener(new KeyListener() {
        @Override public void keyReleased(@SuppressWarnings("unused") KeyEvent __) {/***/}
        
        @Override public void keyPressed(KeyEvent ¢) {
          if(¢.character == SWT.CR /*|| e.character == SWT.LF*/)
            go(text.getText());
        }
      });
      
      Button btnExecute = new Button(composite, SWT.NONE);
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
