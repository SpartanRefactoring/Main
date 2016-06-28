package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * Determines whether an {@link ASTNode} is spartanization disabled. In the
 * current implementation, only instances of {@link BodyDeclaration} may be
 * disabled, and only via their {@link Javadoc} comment
 *
 * @author Ori Roth
 * @since 2016/05/13
 */
public class Disable {
  final Set<ASTNode> dns;
  /**
   * Disable spartanization identifier, used by the programmer to indicate a
   * method/class/code line not to be spartanized
   */
  public final static String dsi = "@DisableSpartan";

  protected Disable(final CompilationUnit u) {
    dns = new HashSet<>();
    if (u == null)
      return;
    u.accept(new BodyDeclarationVisitor(dns));
  }
  /**
   * @param n node
   * @return true iff spartanization is disabled for n
   */
  public boolean check(final ASTNode n) {
    ASTNode p = n;
    while (p != null) {
      if (dns.contains(p))
        return true;
      p = p.getParent();
    }
    return false;
  }

  private static class BodyDeclarationVisitor extends ASTVisitor {
    Set<ASTNode> dns;

    BodyDeclarationVisitor(final Set<ASTNode> dns) {
      this.dns = dns;
    }
    @Override public boolean visit(final AnnotationTypeDeclaration d) {
      return go(d);
    }
    @Override public boolean visit(final EnumDeclaration d) {
      return go(d);
    }
    @Override public boolean visit(final TypeDeclaration d) {
      return go(d);
    }
    @Override public boolean visit(final AnnotationTypeMemberDeclaration d) {
      return go(d);
    }
    @Override public boolean visit(final EnumConstantDeclaration d) {
      return go(d);
    }
    @Override public boolean visit(final FieldDeclaration d) {
      return go(d);
    }
    @Override public boolean visit(final Initializer i) {
      return go(i);
    }
    @Override public boolean visit(final MethodDeclaration d) {
      return go(d);
    }
    private boolean go(final BodyDeclaration d) {
      final Javadoc j = d.getJavadoc();
      if (j == null || !j.toString().contains(dsi))
        return true;
      dns.add(d);
      return false;
    }
  }
}
