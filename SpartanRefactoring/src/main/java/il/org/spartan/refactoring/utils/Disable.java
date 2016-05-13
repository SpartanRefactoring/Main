package il.org.spartan.refactoring.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LineComment;

/**
 * Determines whether an {@link ASTNode} is spartanization disabled
 *
 * @author Ori Roth
 * @since 2016/05/13
 */
public class Disable {
  private final CompilationUnit u;
  private final String s;
  private final Set<Integer> sds;
  private final Set<Integer> sde;
  /**
   * Disable spartanization identifier, used by the programmer to indicate a
   * method/class/code line not to be spartanized
   */
  public final static String dsi = "@DisableSpartan";

  @SuppressWarnings({ "unchecked", "boxing" }) protected Disable(CompilationUnit u) {
    this.u = u;
    s = Source.get(u);
    sds = new HashSet<>();
    sde = new HashSet<>();
    if (u == null || s == null)
      return;
    for (final Comment c : (List<Comment>) u.getCommentList()) {
      u.getLineNumber(c.getStartPosition());
      final CommentVisitor cv = new CommentVisitor(u);
      c.accept(cv);
      if (cv.getContent().contains(dsi)) {
        sds.add(cv.getStartRow());
        sde.add(cv.getEndRow());
        if (!(c instanceof LineComment))
          sde.add(cv.getEndRow() + 1);
      }
    }
  }
  /**
   * @param n node
   * @return true iff spartanization is disabled for n
   */
  @SuppressWarnings("boxing") public boolean check(ASTNode n) {
    if (s == null || u == null)
      return false;
    final int nln = u.getLineNumber(n.getStartPosition()) - 1;
    if (n instanceof BodyDeclaration)
      return sds.contains(nln);
    return sde.contains(nln);
  }
}
