package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** A {@link Wring} to change name of unused variable to double underscore "__"
 * TODO Ori: (maybe) inherent VariableChangeName instead of
 * ReplaceCurrentNodeExclude
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
@SuppressWarnings({ "javadoc", "unused", "hiding", "unchecked" }) public final class SingelVariableDeclarationUnderscoreDoubled
    extends ReplaceCurrentNodeExclude<SingleVariableDeclaration> implements Kind.UnusedArguments {
  // true iff renaming annotated variables only
  final static boolean BY_ANNOTATION = true;

  public static boolean isUsed(final MethodDeclaration d, final SimpleName n) {
    final IsUsed u = new IsUsed(n);
    d.getBody().accept(u);
    return u.conclusion();
  }

  public static boolean suppressedUnused(final SingleVariableDeclaration d) {
    for (final IExtendedModifier m : (Iterable<IExtendedModifier>) d.modifiers())
      if (m instanceof SingleMemberAnnotation && "SuppressWarnings".equals("" + ((SingleMemberAnnotation) m).getTypeName())) {
        final Expression e = ((SingleMemberAnnotation) m).getValue();
        if (e instanceof StringLiteral)
          return "unused".equals(((StringLiteral) e).getLiteralValue());
        for (final Expression x : (Iterable<Expression>) ((ArrayInitializer) ((SingleMemberAnnotation) m).getValue()).expressions())
          return x instanceof StringLiteral && "unused".equals(((StringLiteral) x).getLiteralValue());
        break;
      }
    return false;
  }

  static MethodDeclaration getMethod(final SingleVariableDeclaration d) {
    final ASTNode $ = d.getParent();
    return $ == null || !($ instanceof MethodDeclaration) ? null : (MethodDeclaration) $;
  }

  private static ASTNode replacement(final SingleVariableDeclaration ¢) {
    final SingleVariableDeclaration $ = ¢.getAST().newSingleVariableDeclaration();
    $.setName(¢.getAST().newSimpleName(unusedVariableName()));
    $.setFlags($.getFlags());
    $.setInitializer($.getInitializer());
    $.setType(duplicate.of(¢.getType()));
    duplicate.modifiers(step.modifiers(¢), step.modifiers($));
    return $;
  }

  private static String unusedVariableName() {
    return "__";
  }

  @Override String description(final SingleVariableDeclaration d) {
    return "Change name of unused variable " + d.getName().getIdentifier() + " to __";
  }

  @Override ASTNode replacement(final SingleVariableDeclaration n, final ExclusionManager m) {
    final MethodDeclaration d = getMethod(n);
    if (d == null)
      return null;
    for (final SingleVariableDeclaration svd : step.parameters(d))
      if (unusedVariableName().equals(svd.getName().getIdentifier()))
        return null;
    if (BY_ANNOTATION && !suppressedUnused(n) || isUsed(d, n.getName()))
      return null;
    if (m != null)
      for (final SingleVariableDeclaration svd : step.parameters(d))
        if (!n.equals(svd))
          m.exclude(svd);
    return replacement(n);
  }

  public static class IsUsed extends ASTVisitor {
    boolean c = true;
    String n;

    public IsUsed(final SimpleName sn) {
      n = sn.getIdentifier();
    }

    public IsUsed(final String sn) {
      n = sn;
    }

    public boolean conclusion() {
      return !c;
    }

    @Override public boolean preVisit2(final ASTNode __) {
      return c;
    }

    @Override public final boolean visit(final AnnotationTypeDeclaration __) {
      return false;
    }

    @Override public final boolean visit(final AnonymousClassDeclaration __) {
      return false;
    }

    @Override public final boolean visit(final EnumDeclaration __) {
      return false;
    }

    @Override public boolean visit(final SimpleName n) {
      if (n.equals(n.getIdentifier()))
        c = false;
      return c;
    }

    @Override public final boolean visit(final TypeDeclaration __) {
      return false;
    }
  }
}
