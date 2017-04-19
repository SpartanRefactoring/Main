package il.org.spartan.athenizer.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
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

  @Override public Tip tip(final InfixExpression ¢) {
    subject.pair(getSeperate(¢.getLeftOperand()).getName(), getSeperate(¢.getRightOperand()).getName()).to(¢.getOperator());
    return new Tip(description(¢), getClass(), ¢) {
      @Override @SuppressWarnings("unused") public void go(final ASTRewrite __, final TextEditGroup g) {
        // final ListRewrite l = r.getListRewrite(¢, Expression.);
        // l.insertAfter(¢, x1, g);
        // l.insertAfter(x1, x2, g);
        // l.insertAfter(x2, e, g);
        // l.remove(¢, g);
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
    return null;
  }
}
