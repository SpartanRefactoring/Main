package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.spartanizations.Spartanizations;
import il.org.spartan.utils.Range;

/**
 * A function object representing a sequence of operations on an
 * {@link ASTRewrite} object.
 *
 * @author Yossi Gil
 * @since 2015-08-28
 */
public abstract class Rewrite extends Range {
  /** A textual description of the action to be performed **/
  public final String description;
  /** The line number of the first character to be rewritten **/
  public int lineNumber = -1;
  /**
   * Instantiates this class
   *
   * @param description a textual description of the changes described by this
   *          instance
   * @param n the node on which change is to be carried out
   * @param ns additional nodes, defining the scope of this action.
   */
  public Rewrite(final String description, final ASTNode n, final ASTNode... ns) {
    this(description, range(n, ns));
    lineNumber = ((CompilationUnit) AncestorSearch.forClass(CompilationUnit.class).from(n)).getLineNumber(from);
  }
  Rewrite(final String description, final Range other) {
    super(other);
    this.description = description;
  }
  /**
   * A factory function that converts a sequence of ASTNodes into a
   * {@link Range}
   *
   * @param n arbitrary
   * @param ns
   */
  static Range range(final ASTNode n, final ASTNode... ns) {
    return range(singleNodeRange(n), ns);
  }
  static Range range(final Range r, final ASTNode... ns) {
    Range $ = r;
    for (final ASTNode n : ns)
      $ = $.merge(singleNodeRange(n));
    return $;
  }
  static Range singleNodeRange(final ASTNode n) {
    final int from = n.getStartPosition();
    return new Range(from, from + n.getLength());
  }
  /**
   * Convert the rewrite into changes on an {@link ASTRewrite}
   *
   * @param r where to place the changes
   * @param g to be associated with these changes
   */
  public abstract void go(ASTRewrite r, TextEditGroup g);
  /**
   * Returns all comments associated with an {@link ASTNode}. More precise, gets
   * all the comments about to be eradicated when replacing this node.
   * @author Ori Roth
   * 
   * @param n
   *          original node
   * @param rew
   *          rewriter
   * @return list of comments
   */
  @SuppressWarnings("unchecked")
  static public List<ASTNode> getComments(ASTNode n, ASTRewrite rew) {
    String s = Source.get();
    SourceRange t = rew.getExtendedSourceRangeComputer().computeSourceRange(n);
    int sp = t.getStartPosition();
    int ep = sp + t.getLength();
    CompilationUnit cu = (CompilationUnit) n.getRoot();
    List<ASTNode> $ = new ArrayList<>();
    for (Comment c : (List<Comment>) cu.getCommentList()) {
      int csp = c.getStartPosition();
      if (csp < sp) {
        continue;
      } else if (csp >= ep) {
        break;
      }
      $.add((Comment) rew.createStringPlaceholder(s.substring(csp, csp + c.getLength()) + "\n", c.getNodeType()));
    }
    return $;
  }
}
