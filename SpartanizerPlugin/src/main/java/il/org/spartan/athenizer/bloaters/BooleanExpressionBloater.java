package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** Expand Boolean Expressions : toList Expand : {@code
 * x && y()
 * } To : {@code
 *  boolean a = x;
 *  boolean b = y();
 *  a && b
 * }
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-13 */
public class BooleanExpressionBloater extends CarefulTipper<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x6593D58F0DEFC96AL;

  @Override protected boolean prerequisite(final InfixExpression ¢) {
    return ¢.getOperator() == Operator.CONDITIONAL_AND || ¢.getOperator() == Operator.AND || ¢.getOperator() == Operator.OR
        || ¢.getOperator() == Operator.CONDITIONAL_OR;
  }
  /** [[SuppressWarningsSpartan]] */
  @Override public Tip tip(final InfixExpression ¢) {
    SingleVariableDeclaration d1 = getSeperate(¢.getLeftOperand());
    SingleVariableDeclaration d2 = getSeperate(¢.getRightOperand());
    InfixExpression i = subject.pair(d1.getName(), d2.getName()).to(¢.getOperator());
    System.out.println(d1);
    System.out.println(d2);
    System.out.println(i);
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(yieldAncestors.untilContainingBlock().from(¢), Block.STATEMENTS_PROPERTY);
        l.insertAfter(d1, parent(¢), g);
        l.insertAfter(d2, d1, g);
        l.insertAfter(i, d2, g);
        l.remove(¢, g);
      }
    };
  }
  private static SingleVariableDeclaration getSeperate(final Expression x) {
    final SingleVariableDeclaration $ = x.getAST().newSingleVariableDeclaration();
    $.setInitializer(copy.of(x));
    final PrimitiveType t = x.getAST().newPrimitiveType(PrimitiveType.BOOLEAN);
    $.setType(t);
    $.setName(make.from(x).identifier(scope.newName(x, t)));
    return $;
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Expand boolean expression";
  }
}
