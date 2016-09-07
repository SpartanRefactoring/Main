package il.org.spartan.spartanizer.utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Determines whether an {@link ASTNode} is spartanization disabled. In the
 * current implementation, only instances of {@link BodyDeclaration} may be
 * disabled, and only via their {@link Javadoc} comment
 * @author Ori Roth
 * @since 2016/05/13 */
public class DisabledChecker {
  /** Disable spartanization identifier, used by the programmer to indicate a
   * method/class not to be spartanized */
  public final static String dsi = "@DisableSpartan";
  /** Enable spartanization identifier, used by the programmer to indicate a
   * method/class to be spartanized */
  public final static String esi = "@EnableSpartan";
  final Set<ASTNode> dns;
  final Set<ASTNode> ens;

  public DisabledChecker(final CompilationUnit u) {
    dns = new HashSet<>();
    ens = new HashSet<>();
    if (u == null)
      return;
    u.accept(new BodyDeclarationVisitor(dns, ens));
  }

  /** @param n node
   * @return true iff spartanization is disabled for n */
  public boolean check(final ASTNode n) {
    ASTNode p = n;
    while (p != null) {
      if (dns.contains(p))
        return true;
      if (ens.contains(p))
        return false;
      p = p.getParent();
    }
    return false;
  }

  private class BodyDeclarationVisitor extends ASTVisitor {
    @SuppressWarnings("hiding") Set<ASTNode> dns;
    @SuppressWarnings("hiding") Set<ASTNode> ens;

    BodyDeclarationVisitor(final Set<ASTNode> dns, final Set<ASTNode> ens) {
      this.dns = dns;
      this.ens = ens;
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
      if (j == null)
        return true;
      final String s = j.toString();
      if (s.contains(dsi))
        dns.add(d);
      else if (s.contains(esi))
        ens.add(d);
      return true;
    }
  }
}
