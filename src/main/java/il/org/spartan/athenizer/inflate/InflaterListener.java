package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

public class InflaterListener implements MouseWheelListener, KeyListener {
  static final int CURSOR_IMAGE = SWT.CURSOR_CROSS;
  final StyledText text;
  final List<Listener> externalListeners;
  final Cursor activeCursor;
  final Cursor inactiveCursor;
  boolean active;

  public InflaterListener(final StyledText text) {
    this.text = text;
    externalListeners = new LinkedList<>();
    for (final Listener ¢ : text.getListeners(SWT.MouseWheel))
      externalListeners.add(¢);
    final Display display = PlatformUI.getWorkbench().getDisplay();
    activeCursor = new Cursor(display, CURSOR_IMAGE);
    inactiveCursor = text.getCursor();
  }

  @Override public void mouseScrolled(final MouseEvent ¢) {
    if (active)
      if (¢.count > 0)
        inflate();
      else if (¢.count < 0)
        deflate();
  }

  private static void inflate() {
    System.out.println("inflating " + Selection.Util.current());
  }

  private static void deflate() {
    System.out.println("deflating " + Selection.Util.current());
  }

  @Override public void keyPressed(final KeyEvent ¢) {
    if (¢.keyCode == SWT.CTRL && !active)
      activate();
  }

  @Override public void keyReleased(final KeyEvent ¢) {
    if (¢.keyCode == SWT.CTRL && active)
      deactivate();
  }
  
  private void activate() {
    active = true;
    InflateHandler.removeListeners(text, externalListeners, SWT.MouseWheel);
    if (!text.isDisposed())
      text.setCursor(activeCursor);
  }
  
  private void deactivate() {
    active = false;
    InflateHandler.addListeners(text, externalListeners, SWT.MouseWheel);
    if (!text.isDisposed())
      text.setCursor(inactiveCursor);
  }
  
  public void finilize() {
    if (active)
      deactivate();
  }
}
