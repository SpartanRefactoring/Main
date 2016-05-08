package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNodeExclude;

/**
 * A {@link Wring} to change name of unused variable to double underscore "__"
 * TODO Ori: (maybe) inherent VariableChangeName instead of
 * ReplaceCurrentNodeExclude
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08
 */
@SuppressWarnings("javadoc") public class MethodRenameUnusedVariableToUnderscore
    extends ReplaceCurrentNodeExclude<SingleVariableDeclaration> {
  // true iff renaming annotated variables only
  final static boolean BY_ANNOTATION = true;

  public static class IsUsed extends ASTVisitor {
    boolean c = true;
    SimpleName n;

    public IsUsed(SimpleName sn) {
      n = sn;
    }
    public boolean conclusion() {
      return !c;
    }
    @Override public boolean visit(final SimpleName sn) {
      if (n.getIdentifier().equals(sn.getIdentifier()))
        c = false;
      return c;
    }
    @Override public boolean preVisit2(@SuppressWarnings("unused") ASTNode __) {
      return c;
    }
  }

  public static boolean isUsed(MethodDeclaration d, SimpleName n) {
    final IsUsed u = new IsUsed(n);
    d.getBody().accept(u);
    return u.conclusion();
  }
  @SuppressWarnings("unchecked") public static boolean suppressedUnused(SingleVariableDeclaration n) {
    for (final IExtendedModifier m : (Iterable<IExtendedModifier>) n.modifiers())
      if (m instanceof SingleMemberAnnotation && "SuppressWarnings".equals(((SingleMemberAnnotation) m).getTypeName().toString())) {
        final Expression e = ((SingleMemberAnnotation) m).getValue();
        if (e instanceof StringLiteral)
          return "unused".equals(((StringLiteral) e).getLiteralValue());
        for (final Expression x : (Iterable<Expression>) ((ArrayInitializer) ((SingleMemberAnnotation) m).getValue()).expressions())
          return x instanceof StringLiteral && "unused".equals(((StringLiteral) x).getLiteralValue());
        break;
      }
    return false;
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(SingleVariableDeclaration n, final ExclusionManager em) {
    final ASTNode p = n.getParent();
    if (p == null || !(p instanceof MethodDeclaration))
      return null;
    final MethodDeclaration d = (MethodDeclaration) p;
    for (final SingleVariableDeclaration svd : (Iterable<SingleVariableDeclaration>) d.parameters())
      if (unusedVariableName().equals(svd.getName().getIdentifier()))
        return null;
    if (BY_ANNOTATION && !suppressedUnused(n))
      return null;
    if (isUsed(d, n.getName()))
      return null;
    if (em != null)
      for (final SingleVariableDeclaration svd : (Iterable<SingleVariableDeclaration>) d.parameters())
        if (!n.equals(svd))
          em.exclude(svd);
    final SingleVariableDeclaration $ = n.getAST().newSingleVariableDeclaration();
    $.setName(n.getAST().newSimpleName(unusedVariableName()));
    return $;
  }
  private static String unusedVariableName() {
    return "__";
  }
  @Override String description(SingleVariableDeclaration n) {
    return "Change name of unused variable " + n.getName().getIdentifier() + " to __";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.RENAME_PARAMETERS;
  }
}
