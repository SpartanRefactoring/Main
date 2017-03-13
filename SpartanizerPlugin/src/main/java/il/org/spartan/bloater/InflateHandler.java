package il.org.spartan.bloater;

import java.util.*;
import java.util.List;
import java.util.function.*;

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

import static java.util.stream.Collectors.*;

import il.org.spartan.*;
import il.org.spartan.bloater.SingleFlater.*;
import il.org.spartan.bloater.collateral.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Handler for the Bloater project's feature (global Bloater). Uses
 * {@link BloatApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class InflateHandler extends AbstractHandler {
  private static final english.Activity OPERATION_ACTIVITY = english.Activity.simple("Zoom");
  public static final Bool active = new Bool();
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
      active.clear();
      removePageListener(s);
    } else {
      active.set();
      getOpenedEditors().forEach(InflateHandler::addListener);
      s.addPartListener(pageListener);
    }
    return null;
  }

  private static Void goAggressiveAction(final Selection ¢) {
    applicator().selection(¢).passes(SpartanizationHandler.PASSES).go();
    return null;
  }

  protected static List<Listener> getListeners(final StyledText t) {
    final List<Listener> $ = new ArrayList<>();
    if (t == null)
      return $;
    final List<Listener> ls = as.list(t.getListeners(SWT.MouseWheel));
    if (ls == null)
      return $;
    $.addAll(
        ls.stream().filter(λ -> λ instanceof TypedListener && ((TypedListener) λ).getEventListener() instanceof InflaterListener).collect(toList()));
    return $;
  }

  protected static void addListeners(final StyledText t, final Iterable<Listener> ls, final Integer... types) {
    if (t != null && ls != null)
      as.list(types).forEach(i -> ls.forEach(λ -> t.addListener(i.intValue(), λ)));
  }

  protected static void removeListeners(final StyledText t, final Iterable<Listener> ls, final Integer... types) {
    if (t != null && ls != null)
      ls.forEach(¢ -> as.list(types).forEach(λ -> t.removeListener(λ.intValue(), ¢)));
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
    return !($ instanceof ITextEditor) ? null : (ITextEditor) $;
  }

  protected static StyledText getText(final ITextEditor ¢) {
    if (¢ == null)
      return null;
    final Control $ = ¢.getAdapter(Control.class);
    return !($ instanceof StyledText) ? null : (StyledText) $;
  }

  public static GUIBatchLaconizer applicator() {
    return (GUIBatchLaconizer) SpartanizationHandler.applicator(OPERATION_ACTIVITY).setRunAction(
        ¢ -> Integer.valueOf(as.bit(SingleFlater.commitChanges(SingleFlater.in(¢.buildWithBinding().compilationUnit).from(new InflaterProvider() {
          @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
            return λ -> λ;
          }
        }), ASTRewrite.create(¢.compilationUnit.getAST()), ¢, null, null, null)))).name(OPERATION_ACTIVITY.getIng())
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
    getOpenedEditors().forEach(InflateHandler::removeListener);
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
    if (!(i instanceof FileEditorInput))
      return;
    final IFile f = ((IFileEditorInput) i).getFile();
    if (f == null)
      return;
    final InflaterListener l = new InflaterListener(text, ¢, Selection.of(JavaCore.createCompilationUnitFrom(f)).setUseBinding());
    text.addMouseWheelListener(l);
    text.addKeyListener(l);
  }

  @SuppressWarnings("boxing") private static void removeListener(final ITextEditor e) {
    final StyledText text = getText(e);
    if (text == null)
      return;
    final List<Listener> ls = getListeners(text);
    ls.stream().filter(λ -> λ instanceof TypedListener && ((TypedListener) λ).getEventListener() instanceof InflaterListener).findFirst()
        .ifPresent(λ -> ((InflaterListener) ((TypedListener) λ).getEventListener()).finilize());
    // TODO: Ori Roth seems to be a bug --yg
    removeListeners(text, ls, SWT.MouseWheel/* , SWT.KeyUp, SWT.KeyDown */);
    ls.forEach(λ -> text.removeKeyListener((KeyListener) ((TypedListener) λ).getEventListener()));
  }

  private static Iterable<ITextEditor> getOpenedEditors() {
    final Collection<ITextEditor> $ = new ArrayList<>();
    final IWorkbenchPage p = getPage();
    if (p != null)
      for (final IEditorReference r : p.getEditorReferences()) {
        final IEditorPart ep = r.getEditor(false);
        if (ep instanceof ITextEditor)
          $.add((ITextEditor) ep);
      }
    return $;
  }
}
