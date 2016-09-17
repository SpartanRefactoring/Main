package il.org.spartan.spartanizer.wring.dispatch;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** Determines whether an {@link ASTNode} is spartanization disabled. In the
 * current implementation, only instances of {@link BodyDeclaration} may be
 * disabled, and only via their {@link Javadoc} comment
 * <p>
 * Algorithm:
 * <ol>
 * <li>Visit all nodes that contain an annotation.
 * <li>If a node has a disabler, disable all nodes below it using
 * {@link hop#descendants(ASTNode)}
 * <li>Disabling is done by setting a node property, and is carried out
 * <li>If a node which was previously disabled contains an enabler, enable all
 * all its descendants.
 * <li>If a node which was previously enabled, contains a disabler, disable all
 * nodes below it, and carry on.
 * <li>Obviously, the visit needs to be pre-order, i.e., visiting the parent
 * before the children.
 * </ol>
 * The disabling information is used later by the suggestion/fixing mechanisms,
 * which should know little about this class.
 * @author Ori Roth
 * @since 2016/05/13 */
public class DisabledChecker {
  // TODO: Ori. I am not sure we need a class for this one. All we need is a
  // recursive function/visitor. yg.
  /** Disable spartanization markers, used to indicate that no spartanization
   * should be made to node */
  public static final String disablers[] = { "[[SuppressWarningsSpartan]]", //
  };
  /** Enable spartanization identifier, overriding a disabler */
  public static final String enablers[] = { "[[EnableWarningsSpartan]]", //
  };

  @SuppressWarnings("synthetic-access") public DisabledChecker(final CompilationUnit u) {
    if (u == null)
      return;
    u.accept(new BodyDeclarationVisitor());
  }

  /** @param n node
   * @return true iff spartanization is disabled for n */
  public boolean check(final ASTNode n) {
    return false;
  }

  private class BodyDeclarationVisitor extends ASTVisitor {
    public boolean go(final BodyDeclaration d, final String s) {
      return true;
    }
  }
}
