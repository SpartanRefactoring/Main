package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * int a = 3;
 * while(Panic) {
 *    ++OS.is.in.denger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class WhileToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment> implements TipperCategory.Collapse {
  public static ASTNode replace(final VariableDeclarationFragment f, final WhileStatement ¢) {
    return !fitting(¢) ? null : buildForStatement(az.variableDeclrationStatement(f.getParent()), ¢);
  }

  private static ForStatement buildForStatement(final VariableDeclarationStatement s, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(duplicate.of(body(¢)));
    $.setExpression(pullInitializersFromExpression(dupWhileExpression(¢), s));
    step.initializers($).add(Initializers(findFirst.elementOf(step.fragments(s))));
    return $;
  }

  private static Expression dupWhileExpression(final WhileStatement ¢) {
    return duplicate.of(expression(¢));
  }

  private static boolean fitting(@SuppressWarnings("unused") final WhileStatement __) {
    // TODO: check that the variables declared before the loop doesn't in use
    // after the scope.
    return true;
  }

  private static VariableDeclarationStatement fragmentParent(final VariableDeclarationFragment ¢) {
    return duplicate.of(az.variableDeclrationStatement(¢.getParent()));
  }

  // Ugly one...
  private static Expression handleInfix(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> operands = hop.operands(from);
    for (final Expression ¢ : operands)
      if (iz.parenthesizeExpression(¢) && iz.assignment(az.parenthesizedExpression(¢).getExpression())) {
        final Assignment a = az.assignment(az.parenthesizedExpression(¢).getExpression());
        final SimpleName var = az.simpleName(step.left(a));
        for (final VariableDeclarationFragment f : step.fragments(s))
          if (f.getName().toString().equals(var.toString())) {
            f.setInitializer(duplicate.of(step.right(a)));
            operands.set(operands.indexOf(¢), ¢.getAST().newSimpleName(var.toString()));
          }
      }
    final InfixExpression $ = subject.pair(operands.get(0), operands.get(1)).to(from.getOperator());
    return subject.append($, minus.firstElem(minus.firstElem(operands)));
    // return $;
  }

  private static Expression Initializers(final VariableDeclarationFragment ¢) {
    return az.variableDeclarationExpression(fragmentParent(¢));
  }

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement f) {
    return !iz.infix(from) ? from : handleInfix(duplicate.of(az.infixExpression(from)), f);
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'while', making a for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || r == null || nextStatement == null || exclude == null)
      return null;
    final VariableDeclarationStatement parent = az.variableDeclrationStatement(f.getParent());
    if (parent == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    exclude.excludeAll(step.fragments(parent));
    r.remove(parent, g);
    r.replace(s, replace(f, s), g);
    return r;
  }
}
