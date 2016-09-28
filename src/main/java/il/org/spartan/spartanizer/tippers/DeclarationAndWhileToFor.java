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

public final class DeclarationAndWhileToFor extends ReplaceToNextStatement<VariableDeclarationFragment> implements Category.CommnoFactoring {

  public static ASTNode replace(final VariableDeclarationFragment f, final WhileStatement ¢) {
    ForStatement $ = setExpressionAndInitializers(¢, f);
    return lastStatementIsUpdate(¢) ? forWhithoutLastStatement($, ¢) : forWithLastStatement($, ¢);
  }

  private static ForStatement setExpressionAndInitializers(final WhileStatement ¢, final VariableDeclarationFragment f) {
    ForStatement $ = ¢.getAST().newForStatement();
    $.setExpression(duplicate.of(expression(¢)));
    initializers($).add(dupInitializer(f));
    return $;
  }
  
  private static Expression dupInitializer(final VariableDeclarationFragment ¢) {
    final VariableDeclarationStatement parent = az.variableDeclrationStatement(¢.getParent());
    final VariableDeclarationExpression $ = duplicate.of(parent.getAST().newVariableDeclarationExpression(duplicate.of(¢)));
    $.setType(duplicate.of(parent.getType()));
    return $;
  }

  private static Expression dupWhileLastStatement(final WhileStatement ¢) {
    return duplicate.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }


  private static ForStatement forWhithoutLastStatement(final ForStatement $, final WhileStatement s) {
    updaters($).add(dupWhileLastStatement(s));
    $.setBody(minus.LastStatement(duplicate.of(body(s))));
    return $;
  }

  private static ForStatement forWithLastStatement(final ForStatement $, final WhileStatement s) {
    $.setBody(duplicate.of(body(s)));
    return $;
  }

  private static boolean goingOut() {
    return false;
  }

  private static ASTNode lastStatement(final WhileStatement ¢) {
    return hop.lastStatement(¢.getBody());
  }

  private static boolean lastStatementIsUpdate(final WhileStatement ¢) {
    return iz.assignment(lastStatement(¢)) || iz.incrementOrDecrement(lastStatement(¢)) || iz.expressionStatement(lastStatement(¢));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'while', making a for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (goingOut())
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    r.remove(parent, g);
    r.replace(s, replace(a, s), g);
    return r;
  }
}
