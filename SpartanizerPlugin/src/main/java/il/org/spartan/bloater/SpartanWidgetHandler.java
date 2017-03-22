package il.org.spartan.bloater;

import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.core.commands.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

/** TODO Ori Roth: document class
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-21 */
public class SpartanWidgetHandler extends AbstractHandler {
  private static final int R = 100;
  private static final int TRANSPERACY = 100;
  private static final Point MINIMAL_BUTTON_SIZE = new Point(9 * R / 10, R / 2 - R / 20);
  private static final String IMAGE_ID = "widget";
  static final AtomicBoolean active = new AtomicBoolean(false);

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    if (!active.get()) {
      active.set(true);
      launchWidget(λ -> new Point(λ.x - R, λ.y - R));
    }
    return null;
  }

  public static void launchWidget(Function<Point, Point> startLocation) {
    final Display display = PlatformUI.getWorkbench().getDisplay();
    final Shell shell = new Shell(display, SWT.ON_TOP | SWT.NO_TRIM);
    setMovable(display, shell, shell);
    Button closeButton = new Button(shell, SWT.PUSH | SWT.WRAP);
    closeButton.setText("close");
    expandControl(closeButton, MINIMAL_BUTTON_SIZE);
    closeButton.setLocation(R / 2, 2 * R - closeButton.getSize().y / 2);
    closeButton.addListener(SWT.Selection, new Listener() {
      @Override public void handleEvent(@SuppressWarnings("unused") Event __) {
        shell.close();
        active.set(false);
      }
    });
    Shell originalShell = display.getActiveShell();
    AtomicBoolean widgetFocus = new AtomicBoolean(true);
    final Listener setTransparent = λ -> {
      shell.setAlpha(TRANSPERACY);
      widgetFocus.set(false);
      originalShell.forceFocus();
    }, setSolid = λ -> {
      shell.setAlpha(255);
      widgetFocus.set(true);
    };
    setControl(shell, setSolid, setTransparent);
    setControl(closeButton, setSolid, setTransparent);
    Canvas canvas = createImage(shell);
    setControl(canvas, setSolid, setTransparent);
    setMovable(display, canvas, shell);
    final Region region = new Region();
    region.add(circle(100));
    region.add(closeButton.getBounds());
    final Rectangle size = region.getBounds();
    shell.setSize(size.width, size.height);
    shell.setRegion(region);
    Point mouse = Eclipse.mouseLocation();
    shell.setLocation(startLocation.apply(mouse));
    shell.open();
    if (originalShell != null)
      originalShell.forceFocus();
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().addShellListener(new ShellListener() {
      @Override public void shellIconified(@SuppressWarnings("unused") ShellEvent __) {
        //
      }

      @Override public void shellDeiconified(@SuppressWarnings("unused") ShellEvent __) {
        //
      }

      @Override public void shellDeactivated(@SuppressWarnings("unused") ShellEvent __) {
        if (shell.isDisposed() || widgetFocus.get())
          return;
        shell.setVisible(false);
        widgetFocus.set(false);
      }

      @Override public void shellClosed(@SuppressWarnings("unused") ShellEvent __) {
        if (shell.isDisposed())
          return;
        shell.setVisible(false);
        widgetFocus.set(false);
        active.set(false);
        shell.dispose();
      }

      @Override public void shellActivated(@SuppressWarnings("unused") ShellEvent __) {
        if (!shell.isDisposed())
          shell.setVisible(true);
      }
    });
  }

  private static void setControl(Control c, Listener onEnter, Listener onExit) {
    c.addListener(SWT.MouseEnter, onEnter);
    c.addListener(SWT.MouseExit, onExit);
  }

  static void setMovable(Display d, Control source, Shell target) {
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
              final Point p = d.map(target, null, e.x, e.y);
              target.setLocation(p.x - origin.x, p.y - origin.y);
            }
            break;
          default:
            break;
        }
      }
    };
    source.addListener(SWT.MouseDown, l);
    source.addListener(SWT.MouseUp, l);
    source.addListener(SWT.MouseMove, l);
  }

  static int[] circle(final int r) {
    final int[] $ = new int[8 * r + 4];
    for (int i = 0; i <= 2 * r; ++i) {
      final int x = i - r, y = (int) Math.sqrt(r * r - x * x);
      $[2 * i] = r + x;
      $[2 * i + 1] = r + y;
      $[8 * r - 2 * i - 2] = r + x;
      $[8 * r - 2 * i - 1] = r - y;
    }
    return $;
  }

  static void expandControl(Control c, Point minimalButtonSize) {
    if (c == null)
      return;
    Point s = c.getSize();
    c.setSize(s == null ? minimalButtonSize : new Point(Math.max(s.x, minimalButtonSize.x), Math.max(s.y, minimalButtonSize.y)));
  }

  static Canvas createImage(Shell s) {
    final int w = R, h = R, fixX = -10 * R / 100;
    Image i = Dialogs.image(Dialogs.ICON, IMAGE_ID, λ -> λ.scaledTo(-w, h));
    Canvas $ = new Canvas(s, SWT.NO_REDRAW_RESIZE);
    $.addPaintListener(new PaintListener() {
      @Override public void paintControl(PaintEvent ¢) {
        ¢.gc.drawImage(i, 0, 0);
        $.setSize(w, h);
      }
    });
    $.setLocation(R / 2 - fixX, R / 2);
    $.pack();
    return $;
  }
}
