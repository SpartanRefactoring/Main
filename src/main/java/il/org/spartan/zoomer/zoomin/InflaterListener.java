package il.org.spartan.zoomer.zoomin;

import java.util.*;
import java.util.List;
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

import il.org.spartan.plugin.*;
import il.org.spartan.zoomer.zoomin.SingleFlater.*;

import static il.org.spartan.lisp.*;

public class InflaterListener implements MouseWheelListener, KeyListener {
  private static final BiFunction<Device, Color, Color> INFLATE_COLOR = (d, o) -> new Color(d, (int) (0.9 * o.getRed()), (int) (0.9 * o.getGreen()),
      o.getBlue());
  private static final BiFunction<Device, Color, Color> DEFLATE_COLOR = (d, o) -> new Color(d, (int) (0.9 * o.getRed()), o.getGreen(),
      (int) (0.9 * o.getBlue()));
  static final int CURSOR_IMAGE = SWT.CURSOR_CROSS;
  final StyledText text;
  final ITextEditor editor;
  final List<Listener> externalListeners;
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
    externalListeners = new ArrayList<>();
    for (final Listener ¢ : text.getListeners(SWT.MouseWheel))
      externalListeners.add(¢);
    final Display display = PlatformUI.getWorkbench().getDisplay();
    activeCursor = new Cursor(display, CURSOR_IMAGE);
    inactiveCursor = text.getCursor();
    working = new AtomicBoolean(false);
    originalBackground = text.getBackground();
  }

  @Override public void mouseScrolled(final MouseEvent ¢) {
    if (!active || working.get())
      return;
    windowInformation = WindowInformation.of(editor);
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
    text.setBackground(INFLATE_COLOR.apply(Display.getCurrent(), originalBackground));
    final WrappedCompilationUnit wcu = first(selection.inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new InflaterProvider()).limit(windowInformation),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, editor, windowInformation);
  }

  private void deflate() {
    text.setBackground(DEFLATE_COLOR.apply(Display.getCurrent(), originalBackground));
    final WrappedCompilationUnit wcu = first(selection.inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new DeflaterProvider()).limit(windowInformation),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, editor, windowInformation);
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
    text.setBackground(originalBackground);
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
