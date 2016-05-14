package il.org.spartan.refactoring.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Determines whether an {@link ASTNode} is spartanization disabled. In the
 * current implementation, only instances of {@link BodyDeclaration} may be
 * disabled, and only via their {@link Javadoc} comment
 *
 * @author Ori Roth
 * @since 2016/05/13
 */
public class Disable {
  final Set<Javadoc> dcs;
  final Set<ASTNode> dns;
  /**
   * Disable spartanization identifier, used by the programmer to indicate a
   * method/class/code line not to be spartanized
   */
  public final static String dsi = "@DisableSpartan";

  @SuppressWarnings({ "unchecked" }) protected Disable(CompilationUnit u, String s) {
    dcs = new HashSet<>();
    dns = new HashSet<>();
    if (u == null || s == null)
      return;
    for (final Comment c : (List<Comment>) u.getCommentList()) {
      if (!(c instanceof Javadoc))
        continue;
      u.getLineNumber(c.getStartPosition());
      final CommentVisitor cv = new CommentVisitor(u, s);
      c.accept(cv);
      if (cv.getContent().contains(dsi))
        dcs.add((Javadoc) c);
    }
    u.accept(new ASTVisitor() {
      @Override public boolean visit(AnnotationTypeDeclaration n) {
        return go(n);
      }
      @Override public boolean visit(EnumDeclaration n) {
        return go(n);
      }
      @Override public boolean visit(TypeDeclaration n) {
        return go(n);
      }
      @Override public boolean visit(AnnotationTypeMemberDeclaration n) {
        return go(n);
      }
      @Override public boolean visit(EnumConstantDeclaration n) {
        return go(n);
      }
      @Override public boolean visit(FieldDeclaration n) {
        return go(n);
      }
      @Override public boolean visit(Initializer n) {
        return go(n);
      }
      @Override public boolean visit(MethodDeclaration n) {
        return go(n);
      }
      private boolean go(BodyDeclaration n) {
        if (dcs.contains(n.getJavadoc())) {
          dns.add(n);
          return false;
        }
        return true;
      }
    });
  }
  /**
   * @param n node
   * @return true iff spartanization is disabled for n
   */
  public boolean check(ASTNode n) {
    ASTNode p = n;
    while (p != null) {
      if (dns.contains(p))
        return true;
      p = p.getParent();
    }
    return false;
  }
}
