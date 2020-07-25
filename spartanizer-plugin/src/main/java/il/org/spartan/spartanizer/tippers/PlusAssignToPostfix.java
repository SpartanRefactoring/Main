package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.PostfixExpression;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Expend : {@code a+=1;} To: {@code a++;} Important : Works only in cases
 * where binding exists, otherwise doesnothing Tested in {@link Issue096}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-3-23 */
public class PlusAssignToPostfix extends ReplaceCurrentNode<Assignment>//
    implements Category.Theory.Arithmetics.Symbolic {
  private static final long serialVersionUID = 0x1F5C3A50D08EA75BL;

  @Override public ASTNode replacement(final Assignment $) {
    return !Environment.of($).isNumeric($.getLeftHandSide() + "") || $.getOperator() != Operator.PLUS_ASSIGN
        || !iz.numberLiteral($.getRightHandSide()) || !"1".equals(az.numberLiteral($.getRightHandSide()).getToken()) ? null
            : subject.operand($.getLeftHandSide()).to(PostfixExpression.Operator.INCREMENT);
  }
  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return null;
  }
}
