package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.extract.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Rename unused variable to double underscore "__" TODO Ori: (maybe) inherent
 * VariableChangeName instead of ReplaceCurrentNodeExclude
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
public final class SingelVariableDeclarationUnderscoreDoubled extends ReplaceCurrentNodeExclude<SingleVariableDeclaration>
    implements Kind.Annonimization {
  static final boolean BY_ANNOTATION = true;

  public static boolean isUsed(final MethodDeclaration d, final SimpleName n) {
    final IsUsed u = new IsUsed(n);
    d.getBody().accept(u);
    return u.conclusion();
  }

  public static boolean suppressing(final SingleVariableDeclaration d) {
    for (final Annotation ¢ : annotations(d)) {
      if (!"SuppressWarnings".equals(¢.getTypeName() + ""))
        continue;
      if (iz.singleMemberAnnotation(¢))
        return suppresssing(az.singleMemberAnnotation(¢));
      if (suppressing(az.normalAnnotation(¢)))
        return true;
    }
    return false;
  }

  static MethodDeclaration getMethod(final SingleVariableDeclaration ¢) {
    final ASTNode $ = ¢.getParent();
    return $ == null || !($ instanceof MethodDeclaration) ? null : (MethodDeclaration) $;
  }

  private static boolean isUnused(final Expression ¢) {
    return iz.literal("unused", ¢);
  }

  private static ASTNode replace(final SingleVariableDeclaration ¢) {
    final SingleVariableDeclaration $ = ¢.getAST().newSingleVariableDeclaration();
    $.setName(¢.getAST().newSimpleName(unusedVariableName()));
    $.setFlags($.getFlags());
    $.setInitializer($.getInitializer());
    $.setType(duplicate.of(¢.getType()));
    duplicate.modifiers(step.extendedModifiers(¢), step.extendedModifiers($));
    return $;
  }

  private static boolean suppressing(final ArrayInitializer i) {
    for (final Expression ¢ : step.expressions(i))
      if (isUnused(¢))
        return true;
    return false;
  }

  private static boolean suppressing(final Expression ¢) {
    return iz.literal("unused", ¢) || iz.arrayInitializer(¢) && suppressing(az.arrayInitializer(¢));
  }

  private static boolean suppressing(final NormalAnnotation a) {
    if (a == null)
      return false;
    for (final MemberValuePair ¢ : step.values(a)) {
      if (!iz.identifier("value", ¢.getName()))
        continue;
      if (isUnused(¢.getValue()))
        return true;
    }
    return false;
  }

  private static boolean suppresssing(final SingleMemberAnnotation ¢) {
    return suppressing(¢.getValue());
  }

  private static String unusedVariableName() {
    return "__";
  }

  @Override public String description(final SingleVariableDeclaration ¢) {
    return "Rename unused variable " + ¢.getName().getIdentifier() + " to " + unusedVariableName();
  }

  @Override public ASTNode replacement(final SingleVariableDeclaration ¢) {
    return replacement(¢, null);
  }

  @Override public ASTNode replacement(final SingleVariableDeclaration n, final ExclusionManager m) {
    final MethodDeclaration d = getMethod(n);
    if (d == null)
      return null;
    for (final SingleVariableDeclaration ¢ : step.parameters(d))
      if (unusedVariableName().equals(¢.getName().getIdentifier()))
        return null;
    if (BY_ANNOTATION && !suppressing(n) || isUsed(d, n.getName()))
      return null;
    if (m != null)
      for (final SingleVariableDeclaration ¢ : step.parameters(d))
        if (!n.equals(¢))
          m.exclude(¢);
    return replace(n);
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
}
