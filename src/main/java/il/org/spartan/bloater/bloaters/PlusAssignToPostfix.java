package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Expend : {@code
 *  a+=1;
 * } To: {@code
 * a++;
 * } Important : Works only in cases where binding exists, otherwise does
 * nothing Tested in {@link Issue096}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-3-23 */
public class PlusAssignToPostfix extends ReplaceCurrentNode<Assignment>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x1F5C3A50D08EA75BL;

  @Override @Nullable public ASTNode replacement(@NotNull final Assignment ¢) {
    return ¢.getOperator() != Operator.PLUS_ASSIGN || ¢.getLeftHandSide().resolveTypeBinding() == null
        || ¢.getLeftHandSide().resolveTypeBinding() == null || !¢.getLeftHandSide().resolveTypeBinding().isPrimitive()
        || !iz.numberLiteral(¢.getRightHandSide()) || !"1".equals(az.numberLiteral(¢.getRightHandSide()).getToken()) ? null
            : subject.operand(¢.getLeftHandSide()).to(PostfixExpression.Operator.INCREMENT);
  }

  @Override public String description(@SuppressWarnings("unused") Assignment __) {
    return null;
  }
}
