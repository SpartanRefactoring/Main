/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Jan 15, 2017 */
package il.org.spartan.bloater;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.List;
import java.util.Map.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import il.org.spartan.bloater.SingleFlater.*;
import il.org.spartan.plugin.*;

public class InflaterListener implements MouseWheelListener, KeyListener {
  private static final Function<Device, Color> INFLATE_COLOR = λ -> new Color(λ, 200, 200, 255);
  private static final Function<Device, Color> DEFLATE_COLOR = λ -> new Color(λ, 200, 255, 200);
  private static final Integer[] wheelEvents = { Integer.valueOf(SWT.MouseHorizontalWheel), Integer.valueOf(SWT.MouseVerticalWheel),
      Integer.valueOf(SWT.MouseWheel) };
  static final int CURSOR_IMAGE = SWT.CURSOR_CROSS;
  final StyledText text;
  final ITextEditor editor;
  final Map<Integer, List<Listener>> externalListeners;
  final Cursor activeCursor;
  final Cursor inactiveCursor;
  final Selection selection;
  boolean active;
  final AtomicBoolean working;
  WindowInformation windowInformation;
  private final Color originalBackground;

  public InflaterListener(final StyledText text, final ITextEditor editor, final Selection selection) {
    this.text = text;
    this.editor = editor;
    this.selection = selection;
    externalListeners = new HashMap<>();
    updateListeners(); // not needed probably
    final Display display = PlatformUI.getWorkbench().getDisplay();
    activeCursor = new Cursor(display, CURSOR_IMAGE);
    inactiveCursor = text.getCursor();
    working = new AtomicBoolean(false);
    originalBackground = text.getSelectionBackground();
  }

  @Override public void mouseScrolled(final MouseEvent ¢) {
    if (!active || working.get())
      return;
    windowInformation = WindowInformation.of(text);
    working.set(true);
    if (¢.count > 0)
      SpartanizationHandler.runAsynchronouslyInUIThread(() -> {
        inflate();
        working.set(false);
      });
    else if (¢.count < 0)
      SpartanizationHandler.runAsynchronouslyInUIThread(() -> {
        deflate();
        working.set(false);
      });
  }

  private void inflate() {
    text.setSelectionBackground(INFLATE_COLOR.apply(Display.getCurrent()));
    final WrappedCompilationUnit wcu = first(selection.inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new InflaterProvider()).limit(windowInformation),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, text, editor, windowInformation);
  }

  private void deflate() {
    text.setSelectionBackground(DEFLATE_COLOR.apply(Display.getCurrent()));
    final WrappedCompilationUnit wcu = first(selection.inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new DeflaterProvider()).limit(windowInformation),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, text, editor, windowInformation);
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
    updateListeners();
    removeListeners();
    if (!text.isDisposed())
      text.setCursor(activeCursor);
  }

  private void deactivate() {
    text.setSelectionBackground(originalBackground);
    active = false;
    restoreListeners();
    if (!text.isDisposed())
      text.setCursor(inactiveCursor);
  }

  public void finilize() {
    if (active)
      deactivate();
  }

  private void updateListeners() {
    externalListeners.clear();
    for (final Integer i : wheelEvents) {
      final List<Listener> l = new LinkedList<>();
      Collections.addAll(l, text.getListeners(i.intValue()));
      TypedListener tl = null;
      for (final Listener ¢ : l)
        if (¢ instanceof TypedListener && equals(((TypedListener) ¢).getEventListener()))
          tl = (TypedListener) ¢;
      if (tl != null)
        l.remove(tl);
      externalListeners.put(i, l);
    }
  }

  private void removeListeners() {
    for (final Entry<Integer, List<Listener>> ¢ : externalListeners.entrySet())
      InflateHandler.removeListeners(text, ¢.getValue(), ¢.getKey());
  }

  private void restoreListeners() {
    for (final Entry<Integer, List<Listener>> ¢ : externalListeners.entrySet())
      InflateHandler.addListeners(text, ¢.getValue(), ¢.getKey());
  }
}
