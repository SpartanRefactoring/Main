package il.org.spartan.spartanizer.plugin;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import fluent.ly.note;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.traversal.Traversal;
import il.org.spartan.spartanizer.traversal.TraversalImplementation;
import il.org.spartan.spartanizer.traversal.TraversalTapper;
import il.org.spartan.tables.Table;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-14 */
public class TextualTraversals {
  
  Table table;
  public final Traversal traversal = new TraversalImplementation()//
      .push(new TraversalTapper() {
        @Override public void begin() {
          //System.out.println("Entering file ...");
          TraversalTapper.super.begin();
        }
        @Override public void end() {
          //System.out.println("... Exiting File");
          TraversalTapper.super.end();
        }
      });

  public String fixed(final String from) {
    int n = 0;
    for (IDocument $ = new Document(from), to;; $ = to) {
      to = once($);
      if (to == null || ++n > 20)
        return $.get();
    }
  }
  public String once(final ASTNode ¢) {
    return once(¢ + "");
  }
  /** Performs one spartanization iteration
   * @param d JD
   * @return
   * @throws AssertionError */
  public IDocument once(final IDocument d) throws AssertionError {
    try {
      final TextEdit $ = traversal.go((CompilationUnit) makeAST.COMPILATION_UNIT.from(d.get())).rewriteAST(d, null);
      $.apply(d);
      return $.getChildren().length != 0 ? d : null;
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException | AssertionFailedException | 
        NullPointerException ¢) {
      return note.bug(this, ¢);
    }
  }
  public String once(final String from) {
    return once(new Document(from)).get();
  }
}
