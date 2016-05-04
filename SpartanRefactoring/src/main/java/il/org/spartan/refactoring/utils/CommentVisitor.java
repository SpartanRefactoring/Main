package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

/**
 * A visitor for {@link Comment} nodes. Preserves the comment content (using the
 * source code) and its end location row in the source.
 *
 * @author Ori Roth
 *
 */
public class CommentVisitor extends ASTVisitor {
  private final CompilationUnit cu; // compilation unit
  private final String s; // source code
  private String c; // comment content
  private int sr; // comment start row
  private int er; // comment end row

  /**
   * Create a new comment visitor with up to date source code reference
   */
  public CommentVisitor() {
    super();
    cu = Source.getCompilationUnit();
    s = Source.get();
  }
  /**
   * visit {@link LineComment}, get it's source
   *
   * @param cm a {@link LineComment}
   * @return true iff visit inner comment nodes
   */
  @Override public boolean visit(LineComment cm) {
    return commentVisit(cm);
  }
  /**
   * visit {@link BlockComment}, get it's source
   *
   * @param cm a {@link BlockComment}
   * @return true iff visit inner comment nodes
   */
  @Override public boolean visit(BlockComment cm) {
    return commentVisit(cm);
  }
  /**
   * visit {@link Javadoc}, get it's source
   *
   * @param cm a {@link Javadoc}
   * @return true iff visit inner comment nodes
   */
  @Override public boolean visit(Javadoc cm) {
    return commentVisit(cm);
  }
  /**
   * visit {@link Comment}, get it's source
   *
   * @param cm a {@link Comment}
   * @return true iff visit inner comment nodes
   */
  private <C extends Comment> boolean commentVisit(C cm) {
    final int sp = cm.getStartPosition();
    final int ep = sp + cm.getLength();
    c = new StringBuilder(s).substring(sp, ep).toString();
    sr = cu.getLineNumber(cm.getStartPosition()) - 1;
    er = cu.getLineNumber(cm.getStartPosition() + cm.getLength()) - 1;
    return true;
  }
  /**
   * Aftermath result of the visit - comment source
   *
   * @return visited comment's source
   */
  public String getContent() {
    return c;
  }
  /**
   * Get comment's start row
   *
   * @return comment start row index (starting 0)
   */
  public int getStartRow() {
    return sr;
  }
  /**
   * Get comment's end row
   *
   * @return comment end row index (starting 0)
   */
  public int getEndRow() {
    return er;
  }
}
