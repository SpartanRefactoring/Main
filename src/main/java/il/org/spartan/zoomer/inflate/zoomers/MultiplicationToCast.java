package il.org.spartan.zoomer.inflate.zoomers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Covers Issues #1006 & #1007 </br>
 * Convert :
 * 
 * <pre>
 * x * 1. / x * 1.0
 * x * 1L
 * </pre>
 * 
 * To:
 * 
 * <pre>
 * (double) x
 * (long) x
 * </pre>
 *  Tested in {@link Issue1007}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-11 */
public class MultiplicationToCast extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Expander {
  @Override public ASTNode replacement(InfixExpression x) {
    if (x.getOperator() != Operator.TIMES)
      return null;
    List<Expression> lst = extract.allOperands(x);
    int i = 0;
    boolean found = false;
    CastExpression $ = x.getAST().newCastExpression();
    for (Expression e : lst) {
      if (iz.numberLiteral(e) && ("1.".equals(az.numberLiteral(e).getToken()) || "1.0".equals(az.numberLiteral(e).getToken()))) {
        $.setType(copy.of(x.getAST().newPrimitiveType(PrimitiveType.DOUBLE)));
        found = true;
      }
      if (iz.numberLiteral(e) && ("1L".equals(az.numberLiteral(e).getToken()))) {
        $.setType(copy.of(x.getAST().newPrimitiveType(PrimitiveType.LONG)));
        found = true;
      }
      if (found) {
        if (!x.hasExtendedOperands()) {
          if (i == 0) {
            $.setExpression(copy.of(x.getRightOperand()));
            return $;
          }
          $.setExpression(copy.of(x.getLeftOperand()));
          return $;
        }
        List<Expression> lstcp = extract.allOperands(x);
        lstcp.remove(i);
        $.setExpression(subject.operands(lstcp).to(x.getOperator()));
        return $;
      }
      ++i;
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") InfixExpression __) {
    return null;
  }
}
