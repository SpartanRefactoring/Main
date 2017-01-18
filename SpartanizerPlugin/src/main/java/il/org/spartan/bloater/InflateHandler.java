package il.org.spartan.bloater;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;
import org.eclipse.ui.texteditor.*;

import il.org.spartan.bloater.SingleFlater.*;
import il.org.spartan.bloater.collateral.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Handler for the Athenizer project's feature (global athenizer). Uses
 * {@link AthensApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class InflateHandler extends AbstractHandler {
  private static final Linguistic.Activity OPERATION_ACTIVITY = Linguistic.Activity.simple("Zoom");
  public static final AtomicBoolean active = new AtomicBoolean(false);
  private static final IPartListener pageListener = pageListener();

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final Selection $ = Selection.Util.current().setUseBinding();
    return $.isTextSelection ? goWheelAction() : goAggressiveAction($);
  }

  public static Void goWheelAction() {
    final IPartService s = getPartService();
    if (s == null)
      return null;
    if (active.get()) {
      if (active.getAndSet(false))
        removePageListener(s);
    } else if (!active.getAndSet(true)) {
      (getOpenedEditors()).forEach(InflateHandler::addListener);
      s.addPartListener(pageListener);
    }
    return null;
  }

  private static Void goAggressiveAction(final Selection ¢) {
    applicator().selection(¢).passes(SpartanizationHandler.PASSES).go();
    return null;
  }

  protected static List<Listener> getListeners(final StyledText t) {
    final ArrayList<Listener> $ = new ArrayList<>();
    if (t == null)
      return $;
    final List<Listener> ls = Arrays.asList(t.getListeners(SWT.MouseWheel));
    if (ls == null)
      return $;
    $.addAll(ls.stream().filter(¢ -> ¢ instanceof TypedListener && ((TypedListener) ¢).getEventListener() instanceof InflaterListener)
        .collect(Collectors.toList()));
    return $;
  }

  protected static void addListeners(final StyledText t, final List<Listener> ls, final int... types) {
    if (t != null && ls != null)
      for (final int i : types)
        ls.forEach(¢ -> t.addListener(i, ¢));
  }

  protected static void removeListeners(final StyledText t, final List<Listener> ls, final int... types) {
    if (t != null && ls != null)
      for (final Listener ¢ : ls)
        for (final int i : types)
          t.removeListener(i, ¢);
  }

  protected static IEditorPart getEditorPart() {
    final IWorkbenchPage $ = getPage();
    return $ == null ? null : $.getActiveEditor();
  }

  protected static IWorkbenchPage getPage() {
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return null;
    final IWorkbenchWindow $ = w.getActiveWorkbenchWindow();
    return $ == null ? null : $.getActivePage();
  }

  protected static ITextEditor getTextEditor() {
    final IEditorPart $ = getEditorPart();
    return $ == null || !($ instanceof ITextEditor) ? null : (ITextEditor) $;
  }

  protected static StyledText getText(final ITextEditor ¢) {
    if (¢ == null)
      return null;
    final Control $ = ¢.getAdapter(org.eclipse.swt.widgets.Control.class);
    return $ == null || !($ instanceof StyledText) ? null : (StyledText) $;
  }

  public static GUIBatchLaconizer applicator() {
    return (GUIBatchLaconizer) SpartanizationHandler.applicator(OPERATION_ACTIVITY).setRunAction(
        ¢ -> Integer.valueOf(SingleFlater.commitChanges(SingleFlater.in(¢.buildWithBinding().compilationUnit).from(new InflaterProvider() {
          @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
            return l -> l;
          }
        }), ASTRewrite.create(¢.compilationUnit.getAST()), ¢, null, null) ? 1 : 0)).name(OPERATION_ACTIVITY.getIng())
        .operationName(OPERATION_ACTIVITY);
  }

  private static IPartService getPartService() {
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return null;
    final IWorkbenchWindow $ = w.getActiveWorkbenchWindow();
    return $ == null ? null : $.getPartService();
  }

  @SuppressWarnings("unused") private static IPartListener pageListener() {
    return new IPartListener() {
      @Override public void partActivated(final IWorkbenchPart __) {
        //
      }

      @Override public void partBroughtToTop(final IWorkbenchPart __) {
        //
      }

      @Override public void partClosed(final IWorkbenchPart __) {
        //
      }

      @Override public void partDeactivated(final IWorkbenchPart __) {
        //
      }

      @Override public void partOpened(final IWorkbenchPart ¢) {
        addListener(¢);
      }
    };
  }

  private static void removePageListener(final IPartService ¢) {
    ¢.removePartListener(pageListener);
    (getOpenedEditors()).forEach(InflateHandler::removeListener);
  }

  static void addListener(final IWorkbenchPart ¢) {
    if (¢ instanceof ITextEditor)
      addListener((ITextEditor) ¢);
  }

  private static void addListener(final ITextEditor ¢) {
    final StyledText text = getText(¢);
    if (text == null)
      return;
    final IEditorInput i = ¢.getEditorInput();
    if (i == null || !(i instanceof FileEditorInput))
      return;
    final IFile f = ((FileEditorInput) i).getFile();
    if (f == null)
      return;
    final InflaterListener l = new InflaterListener(text, ¢, Selection.of(JavaCore.createCompilationUnitFrom(f)).setUseBinding());
    text.addMouseWheelListener(l);
    text.addKeyListener(l);
  }

  private static void removeListener(final ITextEditor e) {
    final StyledText text = getText(e);
    if (text == null)
      return;
    final List<Listener> ls = getListeners(text);
    for (final Listener ¢ : ls)
      if (¢ instanceof TypedListener && ((TypedListener) ¢).getEventListener() instanceof InflaterListener) {
        ((InflaterListener) ((TypedListener) ¢).getEventListener()).finilize();
        break;
      }
    // XXX seams to be a bug
    removeListeners(text, ls, SWT.MouseWheel/* , SWT.KeyUp, SWT.KeyDown */);
    ls.forEach(¢ -> text.removeKeyListener((KeyListener) ((TypedListener) ¢).getEventListener()));
  }

  private static List<ITextEditor> getOpenedEditors() {
    final IWorkbenchPage p = getPage();
    final List<ITextEditor> $ = new LinkedList<>();
    if (p == null)
      return $;
    for (final IEditorReference r : p.getEditorReferences()) {
      final IEditorPart ep = r.getEditor(false);
      if (ep != null && ep instanceof ITextEditor)
        $.add((ITextEditor) ep);
    }
    return $;
  }
}
