package il.org.spartan.zoomer.inflate.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Expands <code>a += 3</code> to <code>a = a + 3</code>. Capable of dealing
 * with inclusion and all operator types: <code>a |= b &= c</code> ->
 * <code>a = a | (b &= c)</code> -> <code>a = a | (b = b & c)</code> Test file:
 * {@link Issue1001}
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-28 */
public class AssignmentOperatorExpansion extends CarefulTipper<Assignment> implements TipperCategory.Expander {
  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return "use regualr assignment wth operator";
  }

  @Override protected boolean prerequisite(final Assignment ¢) {
    return ¢.getAST().hasResolvedBindings() && validTypes(¢) && convertToInfix(¢.getOperator()) != null;
  }

  @Override public Tip tip(final Assignment ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final InfixExpression e = ¢.getAST().newInfixExpression();
        e.setLeftOperand(copy.of(left(¢)));
        e.setRightOperand(make.plant(copy.of(right(¢))).into(e));
        e.setOperator(convertToInfix(¢.getOperator()));
        final Assignment a = ¢.getAST().newAssignment();
        a.setLeftHandSide(copy.of(left(¢)));
        a.setRightHandSide(e);
        a.setOperator(Operator.ASSIGN);
        r.replace(¢, a, g);
      }
    };
  }

  static InfixExpression.Operator convertToInfix(final Operator ¢) {
    return ¢ == Operator.BIT_AND_ASSIGN ? InfixExpression.Operator.AND
        : ¢ == Operator.BIT_OR_ASSIGN ? InfixExpression.Operator.OR
            : ¢ == Operator.BIT_XOR_ASSIGN ? InfixExpression.Operator.XOR
                : ¢ == Operator.DIVIDE_ASSIGN ? InfixExpression.Operator.DIVIDE
                    : ¢ == Operator.LEFT_SHIFT_ASSIGN ? InfixExpression.Operator.LEFT_SHIFT
                        : ¢ == Operator.MINUS_ASSIGN ? InfixExpression.Operator.MINUS
                            : ¢ == Operator.PLUS_ASSIGN ? InfixExpression.Operator.PLUS
                                : ¢ == Operator.REMAINDER_ASSIGN ? InfixExpression.Operator.REMAINDER
                                    : ¢ == Operator.RIGHT_SHIFT_SIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_SIGNED
                                        : ¢ == Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED : null;
  }

  private static boolean validTypes(final Assignment ¢) {
    final ITypeBinding $ = left(¢).resolveTypeBinding(), br = right(¢).resolveTypeBinding();
    return $ != null && br != null && $.isPrimitive() && br.isPrimitive() && $.isEqualTo(br);
  }
}
