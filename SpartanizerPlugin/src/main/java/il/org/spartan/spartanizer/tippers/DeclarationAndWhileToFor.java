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
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class DeclarationAndWhileToFor extends ReplaceToNextStatement<VariableDeclarationFragment> implements TipperCategory.Collapse {
  private static Expression dupInitializer(final VariableDeclarationFragment ¢) {
    final VariableDeclarationStatement parent = az.variableDeclrationStatement(¢.getParent());
    final VariableDeclarationExpression $ = duplicate.of(parent.getAST().newVariableDeclarationExpression(duplicate.of(¢)));
    $.setType(duplicate.of(parent.getType()));
    return $;
  }

  private static ForStatement buildForStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setExpression(duplicate.of(expression(¢)));
    $.setBody(duplicate.of(body(¢)));
    initializers($).add(dupInitializer(f));
    return $;
  }

  private static boolean fitting(final WhileStatement ¢) {
    //TODO: check that the variables declared before the loop doesn't in use after the scope.
    return true;
  }

  public static ASTNode replace(final VariableDeclarationFragment f, final WhileStatement ¢) {
    return !fitting(¢) ? null : buildForStatement(f, ¢);
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'while', making a for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(f.getParent());
    if (parent == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    r.remove(parent, g);
    r.replace(s, replace(f, s), g);
    return r;
  }
}
