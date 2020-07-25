package il.org.spartan.athenizer.zoomers;

import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue0980;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.namespace.scope;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
    implements Category.Bloater {
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
