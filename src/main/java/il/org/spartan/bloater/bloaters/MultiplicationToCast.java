package il.org.spartan.bloater.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Covers Issues #1006 & #1007 toList Convert : {@code
 * x * 1. / x * 1.0
 * x * 1L
 * } To: {@code
 * (double) x
 * (long) x
 * } Tested in {@link Issue1007}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-11 */
public class MultiplicationToCast extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final InfixExpression x) {
    if (x.getOperator() != Operator.TIMES)
      return null;
    // TODO: Dor Ma'ayan please rename to xs --yg
    final List<Expression> lst = extract.allOperands(x);
    int i = 0;
    boolean found = false;
    final CastExpression $ = x.getAST().newCastExpression();
    for (final Expression e : lst) {
      if (iz.literal(e, 1.)) {
        $.setType(x.getAST().newPrimitiveType(PrimitiveType.DOUBLE));
        found = true;
      }
      if (iz.literal(e, 1L)) {
        $.setType(x.getAST().newPrimitiveType(PrimitiveType.LONG));
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
        final List<Expression> lstcp = extract.allOperands(x);
        lstcp.remove(i);
        $.setExpression(subject.operands(lstcp).to(x.getOperator()));
        return $;
      }
      ++i;
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return null;
  }
}
