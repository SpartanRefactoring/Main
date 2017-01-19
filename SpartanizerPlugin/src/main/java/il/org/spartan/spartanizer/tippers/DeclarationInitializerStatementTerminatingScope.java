package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;

/** Convert <code>int a=3;b=a;</code> into <code>b = a;</code>
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationInitializerStatementTerminatingScope extends $VariableDeclarationFragementAndStatement
    implements TipperCategory.Inlining {
  private static boolean isPresentOnAnonymous(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.nodeTypeEquals(ancestor, ANONYMOUS_CLASS_DECLARATION))
        return true;
    return false;
  }

  static boolean never(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.nodeTypeIn(ancestor, TRY_STATEMENT, SYNCHRONIZED_STATEMENT))
        return true;
    return false;
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline local " + ¢.getName() + " into subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (!strongCondition(f) || f == null || extract.core(f.getInitializer()) instanceof LambdaExpression || initializer == null || haz.annotation(f)
        || iz.enhancedFor(nextStatement) && iz.simpleName(az.enhancedFor(nextStatement).getExpression())
            && !(az.simpleName(az.enhancedFor(nextStatement).getExpression()) + "").equals(n + "") && !iz.simpleName(initializer)
            && !iz.literal(initializer)
        || isNotAllowedOpOnPrimitive(f, nextStatement) || isArrayInitWithUnmatchingTypes(f))
      return null;
    final VariableDeclarationStatement currentStatement = az.variableDeclrationStatement(f.getParent());
    if (currentStatement == null)
      return null;
    final Block parent = az.block(currentStatement.getParent());
    if (parent == null)
      return null;
    final List<Statement> ss = statements(parent);
    if (!lastIn(nextStatement, ss) || !penultimateIn(currentStatement, ss) || !Collect.definitionsOf(n).in(nextStatement).isEmpty())
      return null;
    final List<SimpleName> uses = Collect.usesOf(n).in(nextStatement);
    if (!sideEffects.free(initializer)) {
      final SimpleName use = onlyOne(uses);
      if (use == null || haz.unknownNumberOfEvaluations(use, nextStatement))
        return null;
    }
    for (final SimpleName use : uses)
      if (never(use, nextStatement) || isPresentOnAnonymous(use, nextStatement))
        return null;
    final Expression v = fixArrayInitializer(initializer, currentStatement);
    final InlinerWithValue i = new Inliner(n, $, g).byValue(v);
    final Statement newStatement = copy.of(nextStatement);
    if (i.addedSize(newStatement) - removalSaving(f) > 0)
      return null;
    $.replace(nextStatement, newStatement, g);
    i.inlineInto(newStatement);
    remove(f, $, g);
    return $;
  }

  /**  */
  static boolean isNotAllowedOpOnPrimitive(final VariableDeclarationFragment f, final Statement nextStatement) {
    if (!iz.literal(f.getInitializer()) || !iz.expressionStatement(nextStatement))
      return false;
    final ExpressionStatement es = (ExpressionStatement) nextStatement;
    if (iz.methodInvocation(es.getExpression())) {
      final MethodInvocation m = (MethodInvocation) es.getExpression();
      final Expression $ = !iz.parenthesizedExpression(expression(m)) ? expression(m) : ((ParenthesizedExpression) expression(m)).getExpression();
      return iz.simpleName($) && ((SimpleName) $).getIdentifier().equals(f.getName().getIdentifier());
    }
    if (!iz.fieldAccess(es.getExpression()))
      return false;
    final FieldAccess fa = (FieldAccess) es.getExpression();
    final Expression e = !iz.parenthesizedExpression(fa.getExpression()) ? fa : ((ParenthesizedExpression) fa.getExpression()).getExpression();
    return iz.simpleName(e) && ((SimpleName) e).getIdentifier().equals(f.getName().getIdentifier());
  }

  public static Expression fixArrayInitializer(final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!iz.arrayInitializer(initializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType(az.arrayType(copy.of(type(currentStatement))));
    $.setInitializer(copy.of(az.arrayInitializer(initializer)));
    return $;
  }

  private static String getElTypeNameFromArrayType(final Type t) {
    if (!(t instanceof ArrayType))
      return null;
    final Type et = ((ArrayType) t).getElementType();
    if (!(et instanceof SimpleType))
      return null;
    final Name $ = ((SimpleType) et).getName();
    return !($ instanceof SimpleName) ? null : ((SimpleName) $).getIdentifier();
  }

  private static boolean isArrayInitWithUnmatchingTypes(final VariableDeclarationFragment f) {
    if (!(f.getParent() instanceof VariableDeclarationStatement))
      return false;
    final String $ = getElTypeNameFromArrayType(az.variableDeclarationStatement(f.getParent()).getType());
    if (!(f.getInitializer() instanceof ArrayCreation))
      return false;
    final String initializerElementTypeName = getElTypeNameFromArrayType(((ArrayCreation) f.getInitializer()).getType());
    return $ != null && initializerElementTypeName != null && !$.equals(initializerElementTypeName);
  }

  private static boolean strongCondition(final VariableDeclarationFragment ¢) {
    // VariableDeclarationStatement f;
    // if(¢ == null ||(f = az.variableDeclarationStatement(¢)) == null)
    // return false;
    // if(step.fragments(f).size() <= 1)
    // return true;
    return true;
  }
}
