package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Expand Boolean Expressions : toList Expand : {@code
 * x && y()
 * } To : {@code
 *  boolean a = x;
 *  boolean b = y();
 *  a && b
 * }
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-13 */
public class BooleanExpressionBloater extends CarefulTipper<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 7319428629798046058L;

  @Override protected boolean prerequisite(@NotNull final InfixExpression ¢) {
    return ¢.getOperator() == Operator.CONDITIONAL_AND || ¢.getOperator() == Operator.AND || ¢.getOperator() == Operator.OR
        || ¢.getOperator() == Operator.CONDITIONAL_OR;
  }

  @NotNull
  @Override public Tip tip(@NotNull final InfixExpression ¢) {
    subject.pair(getSeperate(¢.getLeftOperand()).getName(), getSeperate(¢.getRightOperand()).getName()).to(¢.getOperator());
    return new Tip(description(¢), ¢, getClass()) {
      @Override @SuppressWarnings("unused") public void go(final ASTRewrite __, final TextEditGroup g) {
        // final ListRewrite l = r.getListRewrite(¢, Expression.);
        // l.insertAfter(¢, x1, g);
        // l.insertAfter(x1, x2, g);
        // l.insertAfter(x2, e, g);
        // l.remove(¢, g);
      }
    };
  }

  private static SingleVariableDeclaration getSeperate(@NotNull final Expression x) {
    final SingleVariableDeclaration $ = x.getAST().newSingleVariableDeclaration();
    $.setInitializer(copy.of(x));
    final PrimitiveType t = x.getAST().newPrimitiveType(PrimitiveType.BOOLEAN);
    $.setType(t);
    $.setName(make.from(x).identifier(scope.newName(x, t)));
    return $;
  }

  @Nullable
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return null;
  }
}
