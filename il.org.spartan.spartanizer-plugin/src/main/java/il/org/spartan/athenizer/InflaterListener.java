package il.org.spartan.athenizer;

import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jdt.internal.ui.javaeditor.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.text.undo.*;
import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import fluent.ly.*;
import il.org.spartan.athenizer.SingleFlater.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** Listener for code inflation/deflation using mouse and CTRL key.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-30 */
public class InflaterListener implements KeyListener, Listener {
  // XXX: Ori Roth why so many fields? --yg
  // GUI class, all SWT look like this. --or
  private static final int NO_COMPUND_EDIT = 0;
  private static final int ZOOMIN_COMPUND_EDIT = -1;
  private static final int ZOOMOUT_COMPUND_EDIT = 1;
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
  private final IDocumentUndoManager undoManager;
  private int editDirection;
  private final boolean compoundEditing;
  private ISourceViewer viewer;
  @SuppressWarnings("boxing") private static final List<Integer> activating_keys = Arrays.asList(SWT.CTRL);
  @SuppressWarnings("boxing") private final List<Boolean> active_keys = activating_keys.stream().map(λ -> false).collect(Collectors.toList());
  private static final List<Predicate<Event>> zoomer_keys = Arrays.asList(λ -> λ.keyCode == SWT.KEYPAD_ADD, λ -> λ.keyCode == '=',
      λ -> λ.type == SWT.MouseWheel && λ.count > 0, λ -> λ.keyCode == ']' && λ.type == SWT.KeyUp);
  private static final List<Predicate<Event>> spartan_keys = Arrays.asList(λ -> λ.keyCode == SWT.KEYPAD_SUBTRACT, λ -> λ.keyCode == '-',
      λ -> λ.type == SWT.MouseWheel && λ.count < 0, λ -> λ.keyCode == '[' && λ.type == SWT.KeyUp);

  public InflaterListener(final StyledText text, final ITextEditor editor, final Selection selection) {
    this.text = text;
    this.editor = editor;
    this.selection = selection;
    final Display display = PlatformUI.getWorkbench().getDisplay();
    activeCursor = new Cursor(display, CURSOR_IMAGE);
    inactiveCursor = text.getCursor();
    originalBackground = text.getSelectionBackground();
    undoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(Eclipse.document(editor));
    compoundEditing = PreferencesResources.ZOOMER_REVERT_METHOD_VALUE.get();
    if (editor instanceof CompilationUnitEditor)
      viewer = ((CompilationUnitEditor) editor).getViewer();
  }
  @Override public void handleEvent(final Event ¢) {
    if (!active || !text.getBounds().contains(text.toControl(Eclipse.mouseLocation())))
      return;
    final int t = checkEvent(¢);
    if (t == 0)
      return;
    ¢.doit = false;
    ¢.type = SWT.NONE;
    ¢.count = 0;
    if (working.get())
      return;
    windowInformation = WindowInformation.of(viewer);
    working.set();
    if (t <= 0) {
      if (compoundEditing && editDirection != ZOOMIN_COMPUND_EDIT) {
        if (editDirection != NO_COMPUND_EDIT)
          undoManager.endCompoundChange();
        undoManager.beginCompoundChange();
      }
      editDirection = ZOOMIN_COMPUND_EDIT;
      Eclipse.runAsynchronouslyInUIThread(() -> {
        deflate();
        working.clear();
      });
    } else {
      if (compoundEditing && editDirection != ZOOMOUT_COMPUND_EDIT) {
        if (editDirection != NO_COMPUND_EDIT)
          undoManager.endCompoundChange();
        undoManager.beginCompoundChange();
      }
      editDirection = ZOOMOUT_COMPUND_EDIT;
      Eclipse.runAsynchronouslyInUIThread(() -> {
        inflate();
        working.clear();
      });
    }
  }
  /** Returns 1 if event corresponds to a bloater shortcut, -1 if even
   * corresponds to spartanizer shortcut and 0 otherwise. */
  private static int checkEvent(final Event e) {
    return zoomer_keys.stream().anyMatch(λ -> λ.test(e)) ? 1 : spartan_keys.stream().anyMatch(λ -> λ.test(e)) ? -1 : 0;
  }
  private void inflate() {
    text.setSelectionBackground(INFLATE_COLOR.apply(Display.getCurrent()));
    final WrappedCompilationUnit wcu = the.firstOf(selection.inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new InflaterProvider()).limit(windowInformation),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, viewer, editor, windowInformation, compoundEditing);
  }
  private void deflate() {
    text.setSelectionBackground(DEFLATE_COLOR.apply(Display.getCurrent()));
    final WrappedCompilationUnit wcu = the.firstOf(selection.inner).build();
    SingleFlater.commitChanges(SingleFlater.in(wcu.compilationUnit).from(new DeflaterProvider()).limit(windowInformation),
        ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, viewer, editor, windowInformation, compoundEditing);
  }
  @Override @SuppressWarnings("boxing") public void keyPressed(final KeyEvent ¢) {
    final int index = activating_keys.indexOf(¢.keyCode);
    if (index < 0 || active)
      return;
    active_keys.set(index, true);
    if (active_keys.stream().allMatch(λ -> λ))
      activate();
  }
  @Override @SuppressWarnings("boxing") public void keyReleased(final KeyEvent ¢) {
    final int index = activating_keys.indexOf(¢.keyCode);
    if (index < 0)
      return;
    active_keys.set(index, false);
    if (active)
      deactivate();
  }
  private void activate() {
    active = true;
    editDirection = NO_COMPUND_EDIT;
    if (text.isDisposed())
      return;
    text.setCursor(activeCursor);
    Optional.ofNullable(text.getVerticalBar()).ifPresent(λ -> λ.setEnabled(false));
  }
  private void deactivate() {
    text.setSelectionBackground(originalBackground);
    active = false;
    if (compoundEditing && editDirection != NO_COMPUND_EDIT)
      undoManager.endCompoundChange();
    editDirection = NO_COMPUND_EDIT;
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
