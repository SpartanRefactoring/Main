package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.plugin.*;

/** Handler for the Athenizer project's feature (global athenizer). Uses
 * {@link AthensApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class InflateHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final ITextEditor e = getTextEditor();
    final StyledText text = getText(e);
    if (text == null)
      return null;
    final List<Listener> ls = getListeners(text);
    if (ls == null || ls.isEmpty()) {
      final InflaterListener l = new InflaterListener(text, e);
      text.addMouseWheelListener(l);
      text.addKeyListener(l);
    } else {
      for (final Listener ¢ : ls)
        if (¢ instanceof TypedListener && ((TypedListener) ¢).getEventListener() instanceof InflaterListener) {
          ((InflaterListener) ((TypedListener) ¢).getEventListener()).finilize();
          break;
        }
      // XXX seams to be a bug
      removeListeners(text, ls, SWT.MouseWheel/* , SWT.KeyUp, SWT.KeyDown */);
      // replacement:
      for (final Listener ¢ : ls)
        text.removeKeyListener((KeyListener) ((TypedListener) ¢).getEventListener());
    }
    return null;
  }

  protected static List<Listener> getListeners(final StyledText t) {
    final ArrayList<Listener> $ = new ArrayList<>();
    if (t == null)
      return $;
    final List<Listener> ls = Arrays.asList(t.getListeners(SWT.MouseWheel));
    if (ls == null)
      return $;
    for (final Listener ¢ : ls)
      if (¢ instanceof TypedListener && ((TypedListener) ¢).getEventListener() instanceof InflaterListener)
        $.add(¢);
    return $;
  }

  protected static void addListeners(final StyledText t, final List<Listener> ls, final int... types) {
    if (t != null && ls != null)
      for (final int i : types)
        for (final Listener ¢ : ls)
          t.addListener(i, ¢);
  }

  protected static void removeListeners(final StyledText t, final List<Listener> ls, final int... types) {
    if (t != null && ls != null)
      for (final Listener ¢ : ls)
        for (final int i : types)
          t.removeListener(i, ¢);
  }

  protected static IEditorPart getEditorPart() {
    final IWorkbench w = PlatformUI.getWorkbench();
    if (w == null)
      return null;
    final IWorkbenchWindow ww = w.getActiveWorkbenchWindow();
    if (ww == null)
      return null;
    final IWorkbenchPage $ = ww.getActivePage();
    return $ == null ? null : $.getActiveEditor();
  }
  
  protected static ITextEditor getTextEditor() {
    final IEditorPart $ = getEditorPart();
    return $ == null || !($ instanceof ITextEditor) ? null : (ITextEditor) $;
  }

  protected static StyledText getText(ITextEditor ¢) {
    if (¢ == null)
      return null;
    final Control $ = ¢.getAdapter(org.eclipse.swt.widgets.Control.class);
    return $ == null || !($ instanceof StyledText) ? null : (StyledText) $;
  }
}
