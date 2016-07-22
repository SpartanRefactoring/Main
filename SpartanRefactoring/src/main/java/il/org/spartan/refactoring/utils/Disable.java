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
  final Set<ASTNode> disabled = new HashSet<>();
  /**
   * Disable spartanization signature, used by the programmer to indicate a
   * method/class/code line not to be spartanized
   */
  public final static String signature = "[do not spartanize]";

  protected Disable(final CompilationUnit u) {
    if (u == null)
      return;
    u.accept(new BodyDeclarationVisitor());
  }
  /**
   * @param n node
   * @return true iff spartanization is disabled for n
   */
  public boolean check(final ASTNode n) {
    for (ASTNode p = n; p != null; p = p.getParent())
      if (disabled.contains(p))
        return true;
    return false;
  }

  private class BodyDeclarationVisitor extends ASTVisitor {
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
      if (j == null || !j.toString().contains(signature))
        return true;
      disabled.add(d);
      return false;
    }
  }
}
