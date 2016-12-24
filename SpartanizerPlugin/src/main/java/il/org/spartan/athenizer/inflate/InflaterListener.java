package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.List;

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
import static il.org.spartan.lisp.*;

public class InflaterListener implements MouseWheelListener, KeyListener {
  static final int CURSOR_IMAGE = SWT.CURSOR_CROSS;
  final StyledText text;
  final ITextEditor editor;
  final List<Listener> externalListeners;
  final Cursor activeCursor;
  final Cursor inactiveCursor;
  boolean active;

  public InflaterListener(final StyledText text, final ITextEditor editor) {
    this.text = text;
    this.editor = editor;
    externalListeners = new ArrayList<>();
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

  private void inflate() {
    final WrappedCompilationUnit wcu = first(Selection.Util.current().inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new InflaterProvider()), ASTRewrite.create(wcu.compilationUnit.getAST()),
        wcu, editor);
    // Uncomment the next line in order to use the temp system
    // InflaterUtilities.commitChanges(wcu,
    // InflaterUtilities.selectedStatements(InflaterUtilities.getStatements(wcu)));
  }

  // .build.compilationUnit is used in order to take care of null compilation
  // unit
  private void deflate() {
    final WrappedCompilationUnit wcu = first(Selection.Util.current().inner).build();
    // SingleFlater.in(wcu.compilationUnit).from(new
    // DeflaterProvider()).go(ASTRewrite.create(wcu.compilationUnit.getAST()),
    // null);
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new DeflaterProvider()), ASTRewrite.create(wcu.compilationUnit.getAST()),
        wcu, editor);
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
