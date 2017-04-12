package il.org.spartan.bloater;

import static il.org.spartan.spartanizer.plugin.Eclipse.*;

import static java.util.stream.Collectors.*;

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

import il.org.spartan.*;
import il.org.spartan.bloater.SingleFlater.*;
import il.org.spartan.bloater.collateral.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** Handler for the Bloater project's feature (global Bloater). Uses
 * {@link BloatApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class InflateHandler extends AbstractHandler {
  private static final English.Activity OPERATION_ACTIVITY = English.Activity.simple("Zoom");
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
      openedTextEditors().forEach(InflateHandler::addListener);
      s.addPartListener(pageListener);
    }
    return null;
  }

  private static Void goAggressiveAction(final Selection ¢) {
    applicator().selection(¢).passes(SpartanizationHandler.PASSES).go();
    return null;
  }

  private static List<Listener> getListeners(final StyledText t) {
    final List<Listener> $ = new ArrayList<>();
    if (t == null)
      return $;
    final List<Listener> ls = as.list(t.getListeners(SWT.KeyDown));
    if (ls == null)
      return $;
    $.addAll(
        ls.stream().filter(λ -> λ instanceof TypedListener && ((TypedListener) λ).getEventListener() instanceof InflaterListener).collect(toList()));
    return $;
  }

  private static StyledText getText(final ITextEditor ¢) {
    if (¢ == null)
      return null;
    final Control $ = ¢.getAdapter(Control.class);
    return !($ instanceof StyledText) ? null : (StyledText) $;
  }

  public static BatchApplicator applicator() {
    return (BatchApplicator) SpartanizationHandler.applicator(OPERATION_ACTIVITY).setRunAction(
        ¢ -> Integer.valueOf(as.bit(SingleFlater.commitChanges(SingleFlater.in(¢.buildWithBinding().compilationUnit).from(new InflaterProvider() {
          @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
            return λ -> λ;
          }
        }), ASTRewrite.create(¢.compilationUnit.getAST()), ¢, null, null, null, false)))).name(OPERATION_ACTIVITY.getIng())
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
    openedTextEditors().forEach(InflateHandler::removeListener);
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
    text.getDisplay().addFilter(SWT.MouseWheel, l);
    text.addKeyListener(l);
  }

  private static void removeListener(final ITextEditor e) {
    final StyledText text = getText(e);
    if (text == null)
      return;
    final List<Listener> ls = getListeners(text);
    ls.stream().filter(λ -> λ instanceof TypedListener && ((TypedListener) λ).getEventListener() instanceof InflaterListener).findFirst()
        .ifPresent(λ -> ((InflaterListener) ((TypedListener) λ).getEventListener()).finilize());
    ls.forEach(λ -> text.getDisplay().removeFilter(SWT.MouseWheel, (Listener) ((TypedListener) λ).getEventListener()));
    ls.forEach(λ -> text.removeKeyListener((KeyListener) ((TypedListener) λ).getEventListener()));
  }
}
