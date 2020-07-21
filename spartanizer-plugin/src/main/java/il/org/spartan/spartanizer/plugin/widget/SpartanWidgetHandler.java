package il.org.spartan.spartanizer.plugin.widget;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.core.commands.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.plugin.*;

/** Spartanizer widget.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @author Niv Shalmon
 * @since 2017-03-21 */
public class SpartanWidgetHandler extends AbstractHandler {
  private static int R = 70;
  private static final int r = 20;
  private static final int TRANSPERACY = 100;
  private static final Point MINIMAL_BUTTON_SIZE = new Point(9 * R / 10, R / 2 - R / 20);
  private static final String IMAGE_ID = "widget";
  private static Point[] circles;
  private static List<WidgetOperationEntry> operations;
  static final int OPERATION_HOLD_INTERVAL = 500;
  static final AtomicBoolean active = new AtomicBoolean(false);

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    if (active.get())
      return null;
    active.set(true);
    launchWidget(λ -> new Point(λ.x - R, λ.y - R));
    return null;
  }
  public static void launchWidget(final Function<Point, Point> startLocation) {
    /* if the widget doesn't work due to a class not found exception uncomment
     * the next command, run the widget, disable an operation from the widget
     * preferences page and then close the runtime eclipse and remove this line.
     * That should override the old preferences and solve the issue also, go to
     * OperationListEditor line 375 */
     //WidgetPreferences.storeDefaultEntries();
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return;
    final Display display = w.getDisplay();
    if (display == null)
      return;
    final Shell originalShell = display.getActiveShell();
    if (originalShell == null || originalShell.isDisposed() || !setUpR() || !setUpOperations())
      return;
    final Shell shell = new Shell(display, SWT.ON_TOP | SWT.NO_TRIM);
    final Button closeButton = new Button(shell, SWT.PUSH | SWT.WRAP);
    closeButton.setText("close");
    expandControl(closeButton, MINIMAL_BUTTON_SIZE);
    closeButton.setLocation(r + R / 2, r + 2 * R - closeButton.getSize().y / 2);
    closeButton.addListener(SWT.Selection, __ -> {
      shell.close();
      active.set(false);
    });
    final AtomicBoolean widgetFocus = new AtomicBoolean(true);
    final Listener setTransparent = λ -> {
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
    final Canvas canvas = createImage(shell);
    setControl(canvas, setSolid, setTransparent);
    setMovable(display, canvas, shell);
    final Region region = new Region();
    region.add(circle(R, r + R, r + R));
    region.add(closeButton.getBounds());
    final Rectangle size = region.getBounds();
    shell.setSize(size.width, size.height);
    shell.setRegion(region);
    for (int ¢ = 0; ¢ < circles.length && ¢ < operations.size(); ++¢) {
      final WidgetOperationEntry e = operations.get(¢);
      final WidgetOperation o = e.getWidgetOp();
      o.configure(e.getConfigurationMap());
      setControl(createButton(shell, circles[mapButtonToPoint(¢,operations.size())], o, e.getName()), setSolid, setTransparent);
    }
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
  private static boolean setUpR() {
    final int widgetSize = WidgetPreferences.readSize();
    if (widgetSize < 60 || widgetSize > 100) {
      note.bug();
      return false;
    }
    R = widgetSize;
    circles = new Point[] { //
        new Point(2 * r, 2 * R)//
        , new Point(r, r + R)//
        , new Point(2 * r, 2 * r)//
        , new Point(r + R, r)//
        , new Point(2 * R, 2 * r)//
        , new Point(r + 2 * R, r + R)//
        , new Point(2 * R, 2 * R)//
    };
    return true;
  }
  private static boolean setUpOperations() {
    final List<WidgetOperationEntry> es = WidgetPreferences.readEntries();
    if (es == null) {
      note.bug();
      return false;
    }
    operations = an.empty.list();
    for (final WidgetOperationEntry ¢ : es) {
      if (operations.size() >= 7)
        break;
      if (¢.isEnabled())
        operations.add(¢);
    }
    return true;
  }
  private static void setControl(final Control c, final Listener onEnter, final Listener onExit) {
    c.addListener(SWT.MouseEnter, onEnter);
    c.addListener(SWT.MouseExit, onExit);
  }
  static void setMovable(final Display d, final Control source, final Shell target) {
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
  static int[] circle(@SuppressWarnings("hiding") final int r, final int offsetX, final int offsetY) {
    final int[] $ = new int[8 * r + 4];
    for (int i = 0; i <= 2 * r; ++i) {
      final int x = i - r, y = (int) Math.sqrt(r * r - x * x);
      $[2 * i] = x + offsetX;
      $[2 * i + 1] = y + offsetY;
      $[8 * r - 2 * i - 2] = x + offsetX;
      $[8 * r - 2 * i - 1] = offsetY - y;
    }
    return $;
  }
  static void expandControl(final Control c, final Point minimalButtonSize) {
    if (c == null)
      return;
    final Point s = c.getSize();
    c.setSize(s == null ? minimalButtonSize : new Point(Math.max(s.x, minimalButtonSize.x), Math.max(s.y, minimalButtonSize.y)));
  }
  static Canvas createImage(final Shell s) {
    final int w = R, h = R;
    final Image i = Dialogs.image("file:/plugin/pictures/athenizerBig.png", IMAGE_ID + R, λ -> λ.scaledTo(w, h));
    final Canvas $ = new Canvas(s, SWT.NO_REDRAW_RESIZE);
    $.addPaintListener((final PaintEvent ¢) -> {
      ¢.gc.drawImage(i, 0, 0);
      $.setSize(w, h);
    });
    $.setLocation(r + R / 2, r + R / 2);
    $.pack();
    return $;
  }
  /** creates a new {@link Canvas} which acts as a circular button in the
   * widget. Adds the shape of the button to the region of s and sets up the
   * listeners to {@link SWT} MouseUp, MouseDown, MouseDoubleClick.
   * @param s The main shell of the widget. Must have a region set to it (rather
   *        than use the default region)
   * @param x The x coordinate of the center of the circle
   * @param y The y coordinate of the center of the circle
   * @param o The widget operation assigned to this button. Calling with null
   *        creates an empty button that does nothing when pressed.
   * @return the canvas created. */
  private static Canvas createButton(final Shell s, final Point p, final WidgetOperation o, final String tooltip) {
    final Canvas $ = new Canvas(s, SWT.NO_REDRAW_RESIZE);
    final Image i = o == null ? null : o.image();
    $.addPaintListener((final PaintEvent ¢) -> {
      if (i != null)
        ¢.gc.drawImage(i, r / 2, r / 2);
      $.setSize(2 * r, 2 * r);
    });
    $.setLocation(p.x - r, p.y - r);
    $.pack();
    final Region rg = s.getRegion();
    rg.add(circle(r, p.x, p.y));
    s.setRegion(rg);
    $.setToolTipText(o != null ? tooltip : "No operation provided");
    if (o == null)
      return $;
    final Listener l = new Listener() {
      Timer t = new Timer(true);

      @Override public void handleEvent(final Event e) {
        final WidgetContext c = WidgetContext.generateContext();
        try {
          switch (e.type) {
            case SWT.MouseUp:
              t.cancel();
              t.purge();
              t = new Timer(true);
              o.onMouseUp(c);
              break;
            case SWT.MouseDown:
              t.schedule(new TimerTask() {
                @Override public void run() {
                  try {
                    o.onMouseHold(c);
                  } catch (final Throwable ¢) {
                    note.bug(¢);
                  }
                }
              }, 0, OPERATION_HOLD_INTERVAL);
              break;
            case SWT.MouseDoubleClick:
              o.onDoubleClick(c);
              break;
            default:
              break;
          }
        } catch (final Throwable ¢) {
          note.bug(¢);
        }
      }
    };
    $.addListener(SWT.MouseDown, l);
    $.addListener(SWT.MouseUp, l);
    $.addListener(SWT.MouseDoubleClick, l);
    return $;
  }
  
  private static int mapButtonToPoint(int index, int total) {
    switch(total) {
      case 1:
        return 3;
      case 2:
        return 2 * (2 * index - 1) + 3;
      case 3:
        return 2 * (index - 1) + 3;
      case 4:
        return 2 * index;
      case 5:
        return index < 2 ? index : index != 2 ? index + 2 : 3;
      case 6:
        return index <= 2 ? index : index + 1;
      case 7:
        return index;
    }
    return index;
  }
}
