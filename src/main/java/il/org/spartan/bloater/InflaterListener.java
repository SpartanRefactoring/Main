package il.org.spartan.bloater;

import static il.org.spartan.lisp.*;

import java.util.*;
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
import il.org.spartan.utils.*;

/** Listener for code inflation/deflation using mouse and CTRL key.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-30 */
public class InflaterListener implements KeyListener, Listener {
  // XXX: Ori Roth why so many fields? --yg
  // GUI class, all SWT look like this. --or
  private static final Function<Device, Color> INFLATE_COLOR = λ -> new Color(λ, 200, 200, 255);
  private static final Function<Device, Color> DEFLATE_COLOR = λ -> new Color(λ, 200, 255, 200);
  static final int CURSOR_IMAGE = SWT.CURSOR_CROSS;
  final StyledText text;
  final ITextEditor editor;
  final Cursor activeCursor;
  final Cursor inactiveCursor;
  final Selection selection;
  boolean active;
  final Bool working = new Bool();
  WindowInformation windowInformation;
  private final Color originalBackground;

  public InflaterListener(final StyledText text, final ITextEditor editor, final Selection selection) {
    this.text = text;
    this.editor = editor;
    this.selection = selection;
    final Display display = PlatformUI.getWorkbench().getDisplay();
    activeCursor = new Cursor(display, CURSOR_IMAGE);
    inactiveCursor = text.getCursor();
    originalBackground = text.getSelectionBackground();
  }

  @Override public void handleEvent(final Event ¢) {
    if (¢.type != SWT.MouseWheel || !active || !text.getBounds().contains(text.toControl(Eclipse.mouseLocation())))
      return;
    ¢.doit = false;
    ¢.type = SWT.NONE;
    final int c = ¢.count;
    ¢.count = 0;
    if (working.get())
      return;
    windowInformation = WindowInformation.of(text);
    working.set();
    if (c > 0)
      SpartanizationHandler.runAsynchronouslyInUIThread(() -> {
        inflate();
        working.clear();
      });
    else if (c < 0)
      SpartanizationHandler.runAsynchronouslyInUIThread(() -> {
        deflate();
        working.clear();
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
    if (text.isDisposed())
      return;
    text.setCursor(activeCursor);
    Optional.ofNullable(text.getVerticalBar()).ifPresent(λ -> λ.setEnabled(false));
  }

  private void deactivate() {
    text.setSelectionBackground(originalBackground);
    active = false;
    if (text.isDisposed())
      return;
    text.setCursor(inactiveCursor);
    Optional.ofNullable(text.getVerticalBar()).ifPresent(λ -> λ.setEnabled(true));
  }

  public void finilize() {
    if (active)
      deactivate();
  }

  public Listener find(final Iterable<Listener> ls) {
    TypedListener $ = null;
    for (final Listener ¢ : ls)
      if (¢ instanceof TypedListener && equals(((TypedListener) ¢).getEventListener()))
        $ = (TypedListener) ¢;
    return $;
  }
}
