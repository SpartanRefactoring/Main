package il.org.spartan.spartanizer.tippers;

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
 * for(int a = 3; Panic; ++OS.is.in.denger) {}
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentAndWhileToFor extends ReplaceToNextStatement<VariableDeclarationFragment> implements Kind.Collapse {
  // @Override public boolean prerequisite(final WhileStatement ¢) {
  // return ¢ != null && !iz.containsContinueStatement(¢.getBody());
  // }
  public static ASTNode convert(final VariableDeclarationFragment f, final WhileStatement ¢) {
    return !lastStatementIsUpdate(¢) ? forWithLastStatement(¢.getAST().newForStatement(), ¢, f)
        : forWhithoutLastStatement(¢.getAST().newForStatement(), ¢, f);
  }

  private static Expression dupWhileLastStatement(final WhileStatement ¢) {
    return duplicate.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  private static ForStatement forWhithoutLastStatement(final ForStatement $, final WhileStatement s, final VariableDeclarationFragment f) {
    $.setExpression(duplicate.of(expression(s)));
    updaters($).add(dupWhileLastStatement(s));
    initializers($).add(dupInitializer(f));
    $.setBody(minus.LastStatement(duplicate.of(body(s))));
    return $;
  }

  private static Expression dupInitializer(VariableDeclarationFragment ¢) {
    // Parent type is VariableDeclarationStatement.
    VariableDeclarationStatement parent = az.variableDeclrationStatement(¢.getParent());
    VariableDeclarationExpression $ = duplicate.of(parent.getAST().newVariableDeclarationExpression(duplicate.of(¢)));
    $.setType(duplicate.of(parent.getType()));
    return $;
  }
  
  @SuppressWarnings("unchecked") private static ForStatement forWithLastStatement(final ForStatement $, final WhileStatement s, final VariableDeclarationFragment f) {
    $.setExpression(duplicate.of(expression(s)));
    $.initializers().add(dupInitializer(f));
    $.setBody(duplicate.of(body(s)));
    return $;
  }

  private static ASTNode lastStatement(final WhileStatement ¢) {
    return hop.lastStatement(¢.getBody());
  }

  private static boolean lastStatementIsUpdate(final WhileStatement ¢) {
    return iz.assignment(lastStatement(¢)) || iz.incrementOrDecrement(lastStatement(¢)) || iz.expressionStatement(lastStatement(¢));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'while', making a for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢.getParent()))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (parent == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    r.remove(parent, g);
    r.replace(s, convert(a, s), g);
    return r;
  }
}
