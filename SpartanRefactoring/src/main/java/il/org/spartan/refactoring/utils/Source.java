package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;

/**
 * An accessible container for the source code
 *
 * @author Ori Roth
 * @since 2016-04-17
 */
public class Source {

  static private String s = null;
  static public String get() {
    return s;
  }
  static public void set(String content) {
    s = content;
  }
  /**
   * Disable Spartanization identifier, used by the programmer to indicate a
   * method/class/code line to not be spartanized
   */
  public final static String dsi = "@DisableSpartan";
  /**
   * Match row indexes of comment and a node, to see if a dsi inside the comment
   * would disable the node.
   * 
   * @param nln
   *          node row index
   * @param cer
   *          comment row index
   * @param t
   *          comment type
   * @return true iff a dsi in the comment disables the node
   */
  static private boolean matchRowIndexes(int nln, int cer, int t) {
    switch (t) {
    case ASTNode.LINE_COMMENT:
      return nln == cer;
    case ASTNode.BLOCK_COMMENT:
      return nln == cer || nln == cer + 1;
    case ASTNode.JAVADOC:
      return nln == cer || nln == cer + 1;
    default:
      return nln == cer;
    }
  }
  /**
   * Checks whether or not the spartanization of the current node is disabled.
   * There are two options: first, one should look at the parents of any node to
   * determine its status (implemented as isSpartanizationDisabledInAncestor).
   * Second, the go method of the visitor should return false upon reaching a
   * dsi, what wuld stop it from reaching the the nodes sons (currently
   * implemented in fillRewrite and collect in Trimmer)
   * 
   * @param n
   *          ASTNode
   * @return true iff the spartanization is disabled for this node and its sons.
   */
  public static <N extends ASTNode> boolean isSpartanizationDisabled(N n) {
    String s = Source.get();
    if (s == null) {
      // In a failure case, allow all spartanizations
      return false;
    }
    CompilationUnit cu = ((CompilationUnit) n.getRoot());
    int nln = cu.getLineNumber(n.getStartPosition()) - 1;
      for (Comment c : (List<Comment>) cu.getCommentList()) {
        CommentVisitor cv = new CommentVisitor(cu, s);
        c.accept(cv);
        int cer = cv.getEndRow();
        if (matchRowIndexes(nln, cer, c.getNodeType()) && cv.getContent().contains(dsi)) {
          return true;
        }
      }
    return false;
  }
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
