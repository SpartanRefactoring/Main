package il.org.spartan.spartanizer.wring;

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
  public static final String disablers[] = { "@DisableSpartan", "Hedonistic", "[[hedoni]]", "[[hedonisti]]", "[[hedon]]", "[[hedo]]" };
  /** Enable spartanization identifier, used by the programmer to indicate a
   * method/class to be spartanized */
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
   * @return true iff spartanization is disabled for n [[Hedonistic] */
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
    // TODO: Ori Roth: Don't use short names for global things. 
    @SuppressWarnings("hiding") final Set<ASTNode> dns;
    @SuppressWarnings("hiding") final Set<ASTNode> ens;

    BodyDeclarationVisitor(final Set<ASTNode> dns, final Set<ASTNode> ens) {
      this.dns = dns;
      this.ens = ens;
    }

    public boolean go(final BodyDeclaration d, final Javadoc j) {
      return j == null || go(d, j + "");
    }

    public boolean go(final BodyDeclaration d, final String s) {
      insertAnnotated(d, s, dns, disablers);
      insertAnnotated(d, s, ens, enablers);
      return true;
    }

    @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
      return go(¢);
    }

    @Override public boolean visit(final AnnotationTypeMemberDeclaration ¢) {
      return go(¢);
    }

    @Override public boolean visit(final EnumConstantDeclaration ¢) {
      return go(¢);
    }

    @Override public boolean visit(final EnumDeclaration ¢) {
      return go(¢);
    }

    @Override public boolean visit(final FieldDeclaration ¢) {
      return go(¢);
    }

    @Override public boolean visit(final Initializer ¢) {
      return go(¢);
    }

    @Override public boolean visit(final MethodDeclaration ¢) {
      return go(¢);
    }

    @Override public boolean visit(final TypeDeclaration ¢) {
      return go(¢);
    }

    private boolean go(final BodyDeclaration ¢) {
      return go(¢, ¢.getJavadoc());
    }

    private void insertAnnotated(final BodyDeclaration d, final String s, final Set<ASTNode> g, final String[] ids) {
      for (final String id : ids)
        if (s.contains(id)) {
          g.add(d);
          return;
        }
    }
  }
}
