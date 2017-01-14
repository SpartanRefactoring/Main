package il.org.spartan.zoomer.inflate.zoomers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

import il.org.spartan.spartanizer.java.namespace.*;

/** Expand Boolean Expressions : </br>
 * Expand :
 * 
 * <pre>
 * x && y()
 * </pre>
 * 
 * To :
 * 
 * <pre>
 *  boolean a = x;
 *  boolean b = y();
 *  a && b
 * </pre>
 * 
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-13 */
public class ExpandBooleanExpression extends CarefulTipper<InfixExpression> implements TipperCategory.Expander {
  @Override protected boolean prerequisite(final InfixExpression ¢) {
    return ¢.getOperator() == Operator.CONDITIONAL_AND || ¢.getOperator() == Operator.AND || ¢.getOperator() == Operator.OR
        || ¢.getOperator() == Operator.CONDITIONAL_OR;
  }

  @Override public Tip tip(final InfixExpression ¢) {
    InfixExpression e = subject.pair(getSeperate(¢.getLeftOperand()).getName(), getSeperate(¢.getRightOperand()).getName()).to(¢.getOperator());
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        //final ListRewrite l = r.getListRewrite(¢, Expression.);
//        l.insertAfter(¢, x1, g);
//        l.insertAfter(x1, x2, g);
//        l.insertAfter(x2, e, g);
//        l.remove(¢, g);
      }
    };
  }

  private static SingleVariableDeclaration getSeperate(Expression e) {
    SingleVariableDeclaration x = e.getAST().newSingleVariableDeclaration();
    x.setInitializer(copy.of(e));
    PrimitiveType t = e.getAST().newPrimitiveType(PrimitiveType.BOOLEAN);
    x.setType(t);
    x.setName(e.getAST().newSimpleName(scope.newName(e, t)));
    return x;
  }

  @Override public String description(@SuppressWarnings("unused") InfixExpression __) {
    return null;
  }
}
