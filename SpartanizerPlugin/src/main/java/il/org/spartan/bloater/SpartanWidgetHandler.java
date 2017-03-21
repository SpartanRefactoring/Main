package il.org.spartan.bloater;

import org.eclipse.core.commands.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

/** TODO Ori Roth: document class
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-21 */
public class SpartanWidgetHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }

  static int[] circle(final int r, final int offsetX, final int offsetY) {
    final int[] $ = new int[8 * r + 4];
    // x^2 + y^2 = r^2
    for (int i = 0; i <= 2 * r; ++i) {
      final int x = i - r, y = (int) Math.sqrt(r * r - x * x);
      $[2 * i] = offsetX + x;
      $[2 * i + 1] = offsetY + y;
      $[8 * r - 2 * i - 2] = offsetX + x;
      $[8 * r - 2 * i - 1] = offsetY - y;
    }
    return $;
  }

  public static void go() {
    final Display display = PlatformUI.getWorkbench().getDisplay();
    // Shell must be created with style SWT.NO_TRIM
    final Shell shell = new Shell(display, SWT.ON_TOP | SWT.NO_TRIM);
    shell.setBackground(display.getSystemColor(SWT.COLOR_RED));
    // define a region that looks like a key hole
    final Region region = new Region();
    region.add(circle(67, 67, 67));
    // region.subtract(circle(20, 67, 50));
    // region.subtract(new int[] { 67, 50, 55, 105, 79, 105 });
    // // define the shape of the shell using setRegion
    // shell.setRegion(region);
    final Rectangle size = region.getBounds();
    shell.setSize(size.width, size.height);
    // add ability to move shell around
    final Listener l = new Listener() {
      Point origin;

      @Override public void handleEvent(final Event e) {
        switch (e.type) {
          case SWT.MouseUp:
            origin = null;
            break;
          case SWT.MouseDown:
            origin = new Point(e.x, e.y);
            break;
          case SWT.MouseMove:
            if (origin != null) {
              final Point p = display.map(shell, null, e.x, e.y);
              shell.setLocation(p.x - origin.x, p.y - origin.y);
            }
            break;
          default:
            break;
        }
      }
    };
    shell.addListener(SWT.MouseDown, l);
    shell.addListener(SWT.MouseUp, l);
    shell.addListener(SWT.MouseMove, l);
    // add ability to close shell
    final Button b = new Button(shell, SWT.PUSH);
    b.setBackground(shell.getBackground());
    b.setText("close");
    b.pack();
    // b.setLocation(10, 68);
    b.setLocation(0, 0);
    b.addListener(SWT.Selection, __ -> shell.close());
    shell.addListener(SWT.MouseEnter, __ -> shell.setAlpha(255));
    shell.addListener(SWT.MouseExit, __ -> shell.setAlpha(100));
    shell.setAlpha(100);
    // define the shape of the shell using setRegion
    region.add(b.getBounds());
    shell.setRegion(region);
    shell.open();
  }
}
