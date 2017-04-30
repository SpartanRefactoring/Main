import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class RunMyMainAndRightClickOnMyButtons {
  public static void main(final String[] args) {
    final Display display = new Display();
    final Shell shell = new Shell(display);
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

  private static void applyOnce(final Shell s) {
    final Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Apply (single scan)...");
    final Menu popupMenu = new Menu(bn);
    final MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("To &Function");
    final MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("To &Window");
    addAction(i2, λ -> {/**/});
    final MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("To &Project ");
    addAction(i3, λ -> {/**/});
    bn.setMenu(popupMenu);
  }

  private static void fully(final Shell s) {
    final Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Fully spartanize...");
    final Menu popupMenu = new Menu(bn);
    final MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("&Function");
    final MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("&Window");
    addAction(i2, λ -> {/**/});
    final MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("&Project ");
    addAction(i3, λ -> {/**/});
    bn.setMenu(popupMenu);
  }

  private static void applyRepeatedly(final Shell s) {
    final Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Repeatedly apply ...");
    final Menu popupMenu = new Menu(bn);
    final MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("to &Function");
    final MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("to &Window");
    addAction(i2, λ -> {/**/});
    final MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("to &Project ");
    addAction(i3, λ -> {/**/});
    bn.setMenu(popupMenu);
  }

  private static void suppress(final Shell s) {
    final Button bn = new Button(s, SWT.FLAT);
    bn.setText("&Silence...");
    final Menu popupMenu = new Menu(bn);
    final MenuItem i0 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i0, λ -> {/**/});
    i0.setText("&Tipper");
    final MenuItem i1 = new MenuItem(popupMenu, SWT.NONE);
    addAction(i1, λ -> {/**/});
    i1.setText("Tipping on &Function");
    final MenuItem i2 = new MenuItem(popupMenu, SWT.NONE);
    i2.setText("Tipping on &Class");
    addAction(i2, λ -> {/**/});
    final MenuItem i3 = new MenuItem(popupMenu, SWT.NONE);
    i3.setText("The &Spartanizer");
    addAction(i3, λ -> {/**/});
    final MenuItem i4 = new MenuItem(popupMenu, SWT.NONE);
    i4.setText("Mark as @&UnderConstruction)");
    addAction(i4, λ -> {/**/});
    bn.setMenu(popupMenu);
  }

  private static void addAction(final MenuItem i, final Consumer<SelectionEvent> c) {
    i.addSelectionListener(new SelectionListener() {
      @Override public void widgetSelected(final SelectionEvent ¢) {
        c.accept(¢);
      }

      @Override public void widgetDefaultSelected(final SelectionEvent ¢) {
        widgetSelected(¢);
      }
    });
  }
}