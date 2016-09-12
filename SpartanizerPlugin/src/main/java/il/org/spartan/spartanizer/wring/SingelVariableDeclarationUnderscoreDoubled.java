package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** A {@link Wring} to change name of unused variable to double underscore "__"
 * TODO Ori: (maybe) inherent VariableChangeName instead of
 * ReplaceCurrentNodeExclude
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
// TODO: Ori, please remove all warnings, do not suppress them.
// @SuppressWarnings({ "javadoc", "unused", "unchecked" })
public final class SingelVariableDeclarationUnderscoreDoubled extends ReplaceCurrentNodeExclude<SingleVariableDeclaration>
    implements Kind.UnusedArguments {
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

    @Override public boolean preVisit2(@SuppressWarnings("unused") final ASTNode __) {
      return c;
    }

    @Override public final boolean visit(@SuppressWarnings("unused") final AnnotationTypeDeclaration __) {
      return false;
    }

    @Override public final boolean visit(@SuppressWarnings("unused") final AnonymousClassDeclaration __) {
      return false;
    }

    @Override public final boolean visit(@SuppressWarnings("unused") final EnumDeclaration __) {
      return false;
    }

    @Override public boolean visit(final SimpleName ¢) {
      if (¢.equals(¢.getIdentifier()))
        c = false;
      return c;
    }

    @Override public final boolean visit(@SuppressWarnings("unused") final TypeDeclaration __) {
      return false;
    }
  }

  static final boolean BY_ANNOTATION = true;

  static MethodDeclaration getMethod(final SingleVariableDeclaration d) {
    final ASTNode $ = d.getParent();
    return $ == null || !($ instanceof MethodDeclaration) ? null : (MethodDeclaration) $;
  }

  public static boolean isUsed(final MethodDeclaration d, final SimpleName n) {
    final IsUsed u = new IsUsed(n);
    d.getBody().accept(u);
    return u.conclusion();
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

  // TODO: Ori, search class {@link step}, you would find a way to avoid this
  // warning while using a lib function.
  public static boolean suppressedUnused(final SingleVariableDeclaration d) {
    for (final IExtendedModifier m : (Iterable<IExtendedModifier>) d.modifiers())
      if (m instanceof SingleMemberAnnotation && "SuppressWarnings".equals(((SingleMemberAnnotation) m).getTypeName() + "")) {
        final Expression e = ((SingleMemberAnnotation) m).getValue();
        if (e instanceof StringLiteral)
          return "unused".equals(((StringLiteral) e).getLiteralValue());
        for (final Expression x : (Iterable<Expression>) ((ArrayInitializer) ((SingleMemberAnnotation) m).getValue()).expressions())
          return x instanceof StringLiteral && "unused".equals(((StringLiteral) x).getLiteralValue());
        break;
      }
    return false;
  }

  private static String unusedVariableName() {
    return "__";
  }

  @Override String description(final SingleVariableDeclaration ¢) {
    return "Change name of unused variable " + ¢.getName().getIdentifier() + " to __";
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
}
