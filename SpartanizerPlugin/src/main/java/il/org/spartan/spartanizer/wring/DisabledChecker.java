package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Determines whether an {@link ASTNode} is spartanization disabled. In the
 * current implementation, only instances of {@link BodyDeclaration} may be
 * disabled, and only via their {@link Javadoc} comment
 * @author Ori Roth
 * @since 2016/05/13 */
public class DisabledChecker {
  /**
   * Disable spartanization identifier, used by the programmer to indicate a method/class not to be spartanized 
   */
  public static final String disablers[] = { "@DisableSpartan", "Hedonistic", "[[hedoni]]", "[[hedonisti]]", "[[hedon]]", "[[hedo]]" };
  /**
   * Enable spartanization identifier, used by the programmer to indicate a method/class to be spartanized 
   */
  public static final String enablers[] = { "@EnableSpartan", "[[Spartan]]", "[[spartan]]", "[[sparta]]" };
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
    for (ASTNode p = n; p != null; p = p.getParent()) {
      if (dns.contains(p))
        return true;
      if (ens.contains(p))
        return false;
    }
    return false;
  }

  private class BodyDeclarationVisitor extends ASTVisitor {
    @SuppressWarnings("hiding") final Set<ASTNode> dns;
    @SuppressWarnings("hiding") final Set<ASTNode> ens;

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
      return go(d, d.getJavadoc());
    }

    public boolean go(final BodyDeclaration d, final Javadoc j) {
      return j == null || go(d, (j + ""));
    }

    public boolean go(final BodyDeclaration d, final String s) {
      insertAnnotated(d, s, dns, disablers);
      insertAnnotated(d, s, ens, enablers);
      return true;
    }
    
    private void insertAnnotated(BodyDeclaration d, String s, Set<ASTNode> g, String[] ids) {
      for (final String id : ids)
        if (s.contains(id)) {
          g.add(d);
          return;
        }
    }
  }
}
