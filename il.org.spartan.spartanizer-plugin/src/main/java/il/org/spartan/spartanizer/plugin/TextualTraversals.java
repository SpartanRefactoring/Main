package il.org.spartan.spartanizer.plugin;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.tables.*;

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
      })//
      .push(new TraversalTapper() {
        @Override public void begin() {
          TraversalTapper.super.begin();
          // table = new Table("tippers2", "/tmp");
        }
        @Override public void noTipper()      {/**/}
        @Override public void setNode()       {/**/}
        @Override public void tipperAccepts() {/**/}
        @Override public void tipperRejects() {/**/}
        @Override public void tipperTip()     {
          // table.col("","");
        }
        @Override public void tipPrune()      {/**/}
        @Override public void tipRewrite()    {/**/}        
        @Override public void end() {
          TraversalTapper.super.end();
        }
      })
      ;

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
    } catch (MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      return note.bug(this, ¢);
    }
  }
  public String once(final String from) {
    return once(new Document(from)).get();
  }
}
