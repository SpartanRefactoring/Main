package il.org.spartan.spartanizer.plugin;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.trimming.*;
import il.org.spartan.utils.fluent.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-14 */
public class TextualTraversals {
  public final Traversal traversal = new TraversalImplementation()//
      .push(new TraversalTapper() {
        @Override public void begin() {
          TraversalTapper.super.begin();
        }

        @Override public void end() {
          TraversalTapper.super.end();
        }
      });

  public String fixed(final String from) {
    for (final IDocument $ = new Document(from);;)
      if (fixed(once($)))
        return $.get();
  }

  /** Performs one spartanization iteration
   * @param d JD
   * @return
   * @throws AssertionError */
  public TextEdit once(final IDocument d) throws AssertionError {
    try {
      final TextEdit $ = traversal.go((CompilationUnit) makeAST.COMPILATION_UNIT.from(d.get())).rewriteAST(d, null);
      $.apply(d);
      return $;
    } catch (final NullPointerException | MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      return note.bug(this, ¢);
    }
  }

  public String once(final String from) {
    final IDocument $ = new Document(from);
    once($);
    return $.get();
  }

  /** return if got to fixed point of code */
  private static boolean fixed(final TextEdit ¢) {
    return !¢.hasChildren();
  }
}
