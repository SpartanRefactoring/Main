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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO Ori Roth: document class
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-21 */
public class SpartanWidgetHandler extends AbstractHandler {
  private static final int R = 70;
  private static final int TRANSPERACY = 100;
  private static final Point MINIMAL_BUTTON_SIZE = new Point(9 * R / 10, R / 2 - R / 20);
  private static final String IMAGE_ID = "widget";
  static final AtomicBoolean active = new AtomicBoolean(false);

  @Nullable
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    if (!active.get()) {
      active.set(true);
      launchWidget(λ -> new Point(λ.x - R, λ.y - R));
    }
    return null;
  }

  public static void launchWidget(@NotNull final Function<Point, Point> startLocation) {
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return;
    final Display display = w.getDisplay();
    if (display == null)
      return;
    final Shell originalShell = display.getActiveShell();
    if (originalShell == null || originalShell.isDisposed())
      return;
    @NotNull final Shell shell = new Shell(display, SWT.ON_TOP | SWT.NO_TRIM);
    @NotNull final Button closeButton = new Button(shell, SWT.PUSH | SWT.WRAP);
    closeButton.setText("close");
    expandControl(closeButton, MINIMAL_BUTTON_SIZE);
    closeButton.setLocation(R / 2, 2 * R - closeButton.getSize().y / 2);
    closeButton.addListener(SWT.Selection,  __ -> {
      shell.close();
      active.set(false);
    });
    @NotNull final AtomicBoolean widgetFocus = new AtomicBoolean(true);
    @NotNull final Listener setTransparent = λ -> {
      shell.setAlpha(TRANSPERACY);
      widgetFocus.set(false);
      originalShell.forceFocus();
    }, setSolid = λ -> {
      shell.setAlpha(255);
      widgetFocus.set(true);
    };
    setControl(shell, setSolid, setTransparent);
    setMovable(display, shell, shell);
    setControl(closeButton, setSolid, setTransparent);
    @NotNull final Canvas canvas = createImage(shell);
    setControl(canvas, setSolid, setTransparent);
    setMovable(display, canvas, shell);
    @NotNull final Region region = new Region();
    region.add(circle(R));
    region.add(closeButton.getBounds());
    final Rectangle size = region.getBounds();
    shell.setSize(size.width, size.height);
    shell.setRegion(region);
    shell.setLocation(startLocation.apply(Eclipse.mouseLocation()));
    shell.open();
    originalShell.forceFocus();
    originalShell.addShellListener(new ShellListener() {
      @Override public void shellIconified(@SuppressWarnings("unused") final ShellEvent __) {
        //
      }

      @Override public void shellDeiconified(@SuppressWarnings("unused") final ShellEvent __) {
        //
      }

      @Override public void shellDeactivated(@SuppressWarnings("unused") final ShellEvent __) {
        if (shell.isDisposed() || widgetFocus.get())
          return;
        shell.setVisible(false);
        widgetFocus.set(false);
      }

      @Override public void shellClosed(@SuppressWarnings("unused") final ShellEvent __) {
        if (shell.isDisposed())
          return;
        shell.setVisible(false);
        widgetFocus.set(false);
        active.set(false);
        shell.dispose();
      }

      @Override public void shellActivated(@SuppressWarnings("unused") final ShellEvent __) {
        if (!shell.isDisposed())
          shell.setVisible(true);
      }
    });
  }

  private static void setControl(@NotNull final Control c, final Listener onEnter, final Listener onExit) {
    c.addListener(SWT.MouseEnter, onEnter);
    c.addListener(SWT.MouseExit, onExit);
  }

  static void setMovable(@NotNull final Display d, @NotNull final Control source, @NotNull final Shell target) {
    @NotNull final Listener l = new Listener() {
      @Nullable Point origin;

      @Override public void handleEvent(@NotNull final Event e) {
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

  @NotNull
  static int[] circle(final int r) {
    @NotNull final int[] $ = new int[8 * r + 4];
    for (int i = 0; i <= 2 * r; ++i) {
      final int x = i - r, y = (int) Math.sqrt(r * r - x * x);
      $[2 * i] = r + x;
      $[2 * i + 1] = r + y;
      $[8 * r - 2 * i - 2] = r + x;
      $[8 * r - 2 * i - 1] = r - y;
    }
    return $;
  }

  static void expandControl(@Nullable final Control c, @NotNull final Point minimalButtonSize) {
    if (c == null)
      return;
    final Point s = c.getSize();
    c.setSize(s == null ? minimalButtonSize : new Point(Math.max(s.x, minimalButtonSize.x), Math.max(s.y, minimalButtonSize.y)));
  }

  @NotNull
  static Canvas createImage(final Shell s) {
    final int w = R, h = R, fixX = -10 * R / 100;
    final Image i = Dialogs.image(Dialogs.ICON, IMAGE_ID, λ -> λ.scaledTo(-w, h));
    @NotNull final Canvas $ = new Canvas(s, SWT.NO_REDRAW_RESIZE);
    $.addPaintListener(¢ -> {
      ¢.gc.drawImage(i, 0, 0);
      $.setSize(w, h);
    });
    $.setLocation(R / 2 - fixX, R / 2);
    $.pack();
    return $;
  }
}
