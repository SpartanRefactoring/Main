import java.util.function.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class RunMyMainAndRightClickOnMyButtons {
  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new GridLayout());
    applyOnce(shell);
    applyRepeatedly(shell);
    suppress(shell);
    fully(shell);
    shell.open();
    while (!shell.isDisposed())
      if (!display.readAndDispatch())
        display.sleep();
  }

  private static void applyOnce(Shell s) {
    Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Apply (single scan)...");
    Menu popupMenu = new Menu(bn);
    MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("To &Function");
    MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("To &Window");
    addAction(i2, λ -> {/**/});
    MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("To &Project ");
    addAction(i3, λ -> {/**/});
    bn.setMenu(popupMenu);
  }


  private static void fully(Shell s) {
    Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Fully spartanize...");
    Menu popupMenu = new Menu(bn);
    MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("&Function");
    MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("&Window");
    addAction(i2, λ -> {/**/});
    MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("&Project ");
    addAction(i3, λ -> {/**/});
    bn.setMenu(popupMenu);
  }


  private static void applyRepeatedly(Shell s) {
    Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Repeatedly apply ...");
    Menu popupMenu = new Menu(bn);
    MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("to &Function");
    MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("to &Window");
    addAction(i2, λ -> {/**/});
    MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("to &Project ");
    addAction(i3, λ -> {/**/});
    bn.setMenu(popupMenu);
  }


  private static void suppress(Shell s) {
    Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Silence...");
    Menu popupMenu = new Menu(bn);
    MenuItem i0 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i0, λ -> {/**/});
    i0.setText("&Tipper");
    MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("Tipping on &Function");
    MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("Tipping on &Class");
    addAction(i2, λ -> {/**/});
    MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("The &Spartanizer");
    addAction(i3, λ -> {/**/});
    MenuItem i4 = new MenuItem(popupMenu, SWT.NONE);
    i4.setText("Mark as @&UnderConstruction)");
    addAction(i4, λ -> {/**/});
    bn.setMenu(popupMenu);
  }

  private static void addAction(MenuItem i, Consumer<SelectionEvent> c) {
    i.addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(SelectionEvent ¢) {
        c.accept(¢);
      }

      @Override public void widgetDefaultSelected(SelectionEvent ¢) {
        widgetSelected(¢);
      }
    });
  }
}