package il.org.spartan.athenizer.bloaters;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Expand Boolean Expressions : toList Expand : {@code
 * x && y()
 * } To : {@code
 *  boolean a = x;
 *  boolean b = y();
 *  a && b
 * } Test case is {@link Issue0980}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @author tomerdragucki
 * @since 2017-01-13 */
public class BooleanExpressionBloater extends CarefulTipper<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x6593D58F0DEFC96AL;

  @Override protected boolean prerequisite(final InfixExpression ¢) {
    return (¢.getOperator() == Operator.CONDITIONAL_AND || ¢.getOperator() == Operator.CONDITIONAL_OR)
        && (isComplicated(¢.getLeftOperand()) || isComplicated(¢.getRightOperand())) && !iz.lambdaExpression(¢.getParent());
  }
  @Override public Tip tip(final InfixExpression ¢) {
    final VariableDeclarationStatement $ = getSeperate(¢.getLeftOperand()), v2 = getSeperate(¢.getRightOperand());
    final InfixExpression i = subject.pair(step.fragments($).get(0).getName(), step.fragments(v2).get(0).getName()).to(¢.getOperator());
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(yieldAncestors.untilContainingBlock().from(¢), Block.STATEMENTS_PROPERTY);
        l.insertBefore($, yieldAncestors.untilClass(Statement.class).from(¢), g);
        l.insertAfter(v2, $, g);
        r.replace(¢, i, g);
      }
    };
  }
  private static VariableDeclarationStatement getSeperate(final Expression x) {
    final VariableDeclarationFragment f = x.getAST().newVariableDeclarationFragment();
    f.setInitializer(copy.of(x));
    final PrimitiveType t = x.getAST().newPrimitiveType(PrimitiveType.BOOLEAN);
    f.setName(make.from(x).identifier(scope.newName(x, t)));
    final VariableDeclarationStatement $ = x.getAST().newVariableDeclarationStatement(f);
    $.setType(t);
    return $;
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Expand boolean expression";
  }
  private static boolean isComplicated(final Expression ¢) {
    return iz.nodeTypeIn(¢, METHOD_INVOCATION, INFIX_EXPRESSION, ASSIGNMENT);
  }
}
