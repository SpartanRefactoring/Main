package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * int a = 3;
 * while(Panic) {
 *    ++OS.is.in.denger;
 * }
 * </pre>
 *
 * to
 *
 * <pre>
 * for(int a = 3; Panic; ++OS.is.in.denger) {}
 * </pre>
 *
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentAndWhileToFor extends ReplaceToNextStatement<Assignment> implements Kind.Collapse {
  @Override public String description(@SuppressWarnings("unused") final Assignment ¢) {
    return "Convert the while() to a traditional for(;;)";
   }

  private static boolean goingOut() {
    return false;
  }
  
  @Override protected ASTRewrite go(final ASTRewrite r, final Assignment a, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.asStatement(a.getParent());
    if (goingOut())
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    r.remove(parent, g);
    r.replace(s, convert(a, s), g);
    return r;
  }
  
  @SuppressWarnings("unchecked") private static ForStatement forWhithoutLastStatement(final ForStatement $, final WhileStatement s, final Assignment a) {
    $.setExpression(dupWhileExpression(s));
    $.updaters().add(dupWhileLastStatement(s));
    $.initializers().add(duplicate.of(a));
    $.setBody(minus.LastStatement(dupWhileBody(s)));
    return $;
  }
  
  @SuppressWarnings("unchecked") private static ForStatement forWithLastStatement(final ForStatement $, final WhileStatement s, final Assignment a) {
    $.setExpression(dupWhileExpression(s));
    $.initializers().add(duplicate.of(a));
    $.setBody(dupWhileBody(s));
    return $;
  }

  private static Statement dupWhileBody(final WhileStatement ¢) {
    return duplicate.of(¢.getBody());
  }

  private static Expression dupWhileExpression(final WhileStatement ¢) {
    return duplicate.of(¢.getExpression());
  }

  private static Expression dupWhileLastStatement(final WhileStatement ¢) {
    return duplicate.of(az.expressionStatement(lastStatement(¢)).getExpression());
  }

  private static ASTNode lastStatement(final WhileStatement ¢) {
    return hop.lastStatement(¢.getBody());
  }

  private static boolean lastStatementIsUpdate(final WhileStatement ¢) {
    return iz.assignment(lastStatement(¢)) || iz.incrementOrDecrement(lastStatement(¢)) || iz.expressionStatement(lastStatement(¢));
  }

  //@Override public boolean prerequisite(final WhileStatement ¢) {
  //  return ¢ != null && !iz.containsContinueStatement(¢.getBody());
 // }

  public static ASTNode convert(final Assignment a, final WhileStatement ¢) {
    return !lastStatementIsUpdate(¢) ? forWithLastStatement(¢.getAST().newForStatement(), ¢, a) :
      forWhithoutLastStatement(¢.getAST().newForStatement(), ¢, a);
  }
}
