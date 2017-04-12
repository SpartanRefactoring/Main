package il.org.spartan.spartanizer.plugin;

import java.util.*;
import java.util.List;
import java.util.function.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.text.edits.*;
import org.eclipse.ui.*;
import org.eclipse.ui.texteditor.*;

import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** A shortcut to textually insert a new special character/s.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-02-10 */
@SuppressWarnings("boxing")
public class CharacterShortcut extends AbstractHandler {
  private static final Function<Character, Character> key = λ -> (char) ((int) λ - (int) 'a' + 1);
  private static final Map<Character, Character> shortcutsMap = new HashMap<>();
  static {
    shortcutsMap.put(key.apply('c'), '¢');
    shortcutsMap.put(key.apply('l'), 'λ');
  }

  /* (non-Javadoc)
   *
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
   * ExecutionEvent) */
  @Override public Object execute(final ExecutionEvent e) {
    if (e == null)
      return null;
    final Object $ = e.getTrigger();
    if (!($ instanceof Event))
      return null;
    final ISelection is = Selection.Util.getSelection();
    final Selection s = Selection.Util.getCurrentCompilationUnit();
    return !(is instanceof ITextSelection) || s == null || s.isEmpty() ? null
        : insertCharacter(shortcutsMap.get(getCharacter((Event) $)), s.setTextSelection((ITextSelection) is));
  }

  private static char getCharacter(final Event $) {
    return $.type != 13 ? $.character : key.apply('c'); // Hack, obviously
  }

  /** Insert a character at the given location. If the text selection contains a
   * range of characters, all of them would be replaced.
   * @param c the character to write in the file
   * @param s the file and text location selected
   * @return null */
  private static Void insertCharacter(final char c, final Selection $) {
    if ($ == null || $.isEmpty() || $.textSelection == null)
      return null;
    final List<ICompilationUnit> us = $.getCompilationUnits();
    if (us == null || us.size() != 1)
      return null;
    final ICompilationUnit u = us.get(0);
    final MultiTextEdit m = new MultiTextEdit();
    m.addChild(new DeleteEdit($.textSelection.getOffset(), $.textSelection.getLength()));
    m.addChild(new InsertEdit($.textSelection.getOffset(), c + ""));
    try {
      u.applyTextEdit(m, new NullProgressMonitor());
    } catch (final JavaModelException ¢) {
      return note.bug(¢);
    }
    return fixSelection($.textSelection.getOffset() + 1);
  }

  /** Change text selection to empty text selection at the end of the original
   * selection.
   * @param i text selection required location
   * @return null */
  private static Void fixSelection(final int i) {
    final IEditorPart p = Selection.Util.getEditorPart();
    if (!(p instanceof ITextEditor))
      return null;
    final ITextEditor e = (ITextEditor) p;
    e.getSelectionProvider().setSelection(new TextSelection(i, 0));
    return null;
  }
}
