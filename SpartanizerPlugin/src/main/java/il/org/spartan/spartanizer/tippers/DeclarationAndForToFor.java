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
 * for(Panic) {
 *    ++OS.is.in.denger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic; ++OS.is.in.denger) {}
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class DeclarationAndForToFor extends ReplaceToNextStatement<VariableDeclarationStatement> implements TipperCategory.CommnoFactoring {
  private static Expression dupForLastStatement(final ForStatement ¢) {
    return duplicate.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  private static Expression dupInitializer(final VariableDeclarationStatement ¢) {
    final List<VariableDeclarationFragment> fragments = new ArrayList<>();
    for (final VariableDeclarationFragment f : step.fragments(¢))
      fragments.add(duplicate.of(f));
    final VariableDeclarationExpression $ = duplicate.of(¢.getAST().newVariableDeclarationExpression(fragments.get(0)));
    step.fragments($).clear();
    step.fragments($).addAll(fragments);
    return $;
  }

  private static ForStatement forWhithoutLastStatement(final ForStatement $, final ForStatement s) {
    updaters($).add(dupForLastStatement(s));
    $.setBody(minus.LastStatement(duplicate.of(body(s))));
    return $;
  }

  private static ForStatement forWithLastStatement(final ForStatement $, final ForStatement s) {
    $.setBody(duplicate.of(body(s)));
    return $;
  }

  private static ASTNode lastStatement(final ForStatement ¢) {
    return hop.lastStatement(¢.getBody());
  }

  private static boolean lastStatementIsUpdate(final ForStatement ¢) {
    return iz.assignment(lastStatement(¢)) || iz.incrementOrDecrement(lastStatement(¢)) || iz.expressionStatement(lastStatement(¢));
  }

  public static ASTNode replace(final VariableDeclarationStatement f, final ForStatement ¢) {
    final ForStatement $ = setExpressionAndInitializers(¢, f);
    return lastStatementIsUpdate(¢) ? forWhithoutLastStatement($, ¢) : forWithLastStatement($, ¢);
  }

  private static ForStatement setExpressionAndInitializers(final ForStatement ¢, final VariableDeclarationStatement f) {
    final ForStatement $ = duplicate.of(¢);
    final List<Expression> initializers = initializers($);
    if (initializers.isEmpty())
      initializers.add(dupInitializer(f));
    // TODO: Alex, else have to compare initializers identifiers to given
    // VariableDeclarationStatement names.
    return $;
  }

  @Override public String description(final VariableDeclarationStatement ¢) {
    return "Merge with subequent 'for', making a for (" + ¢ + "; " + expression(az.forStatement(extract.nextStatement(¢.getParent()))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationStatement a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (parent == null)
      return null;
    final ForStatement s = az.forStatement(nextStatement);
    if (s == null)
      return null;
    r.remove(parent, g);
    r.replace(s, replace(a, s), g);
    return r;
  }
}
