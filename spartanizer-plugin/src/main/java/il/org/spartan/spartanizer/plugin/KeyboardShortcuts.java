package il.org.spartan.spartanizer.plugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import fluent.ly.note;
import fluent.ly.the;

/** A shortcut to textually insert a new special character/s.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-02-10 */
@SuppressWarnings("boxing")
public class KeyboardShortcuts extends AbstractHandler {
  private static final Map<String, Character> shortcutsMap = new HashMap<>();
  static {
    shortcutsMap.put("ItShortcut", '¢');
    shortcutsMap.put("LambdaShortcut", 'λ');
  }

  @Override public Object execute(final ExecutionEvent e) {
    if (e == null || !(e.getTrigger() instanceof Event))
      return null;
    final ISelection $ = Selection.Util.getSelection();
    final Selection s = Selection.Util.getCurrentCompilationUnit();
    return !($ instanceof ITextSelection) || s == null || s.isEmpty() ? null
        : insertCharacter(shortcutsMap.get(getCharacter(e)), s.setTextSelection((ITextSelection) $));
  }
  private static String getCharacter(final ExecutionEvent $) {
    try {
      return $.getCommand().getName();
    } catch (@SuppressWarnings("unused") final NotDefinedException x) {
      return null;
    }
  }
  /** Insert a character at the given location. If the text selection contains a
   * range of characters, all of them would be replaced.
   * @param c the character to write in the file
   * @param s the file and text location selected
   * @return null */
  private static Void insertCharacter(final char c, final Selection $) {
    if ($ == null || $.isEmpty() || $.textSelection == null)
      return null;
    final ICompilationUnit u = the.onlyOneOf($.getCompilationUnits());
    if (u == null)
      return null;
    final MultiTextEdit m = new MultiTextEdit();
    m.addChild(new DeleteEdit($.textSelection.getOffset(), $.textSelection.getLength()));
    m.addChild(new InsertEdit($.textSelection.getOffset(), c + ""));
    try {
      u.applyTextEdit(m, new NullProgressMonitor());
    } catch (final JavaModelException ¢) {
      return note.bug(¢);
    }
    return clearSelection($.textSelection.getOffset() + 1);
  }
  /** Change text selection to empty text selection at the end of the original
   * selection.
   * @param i text selection required location
   * @return null */
  private static Void clearSelection(final int i) {
    final IEditorPart p = Selection.Util.getEditorPart();
    if (!(p instanceof ITextEditor))
      return null;
    final ITextEditor e = (ITextEditor) p;
    e.getSelectionProvider().setSelection(new TextSelection(i, 0));
    return null;
  }
}
