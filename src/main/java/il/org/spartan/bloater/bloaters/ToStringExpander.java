package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Expend : {@code
 *  a+ "";
 * } To: {@code
 * a.toString();
 * } Important : Works only in cases where binding exists, otherwise does
 * nothing Tested in {@link Issue096}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-20 */
public class ToStringExpander extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 2259745231803950939L;

  @Nullable @Override public ASTNode replacement(@NotNull final InfixExpression ¢) {
    if (¢.getOperator() != Operator.PLUS || ¢.getLeftOperand().resolveTypeBinding() == null || ¢.getRightOperand().resolveTypeBinding() == null
        || extract.allOperands(¢).size() != 2)
      return null;
    final MethodInvocation $ = ¢.getAST().newMethodInvocation();
    if ("\"\"".equals(¢.getRightOperand() + "") && !¢.getLeftOperand().resolveTypeBinding().isPrimitive())
      $.setExpression(copy.of(¢.getLeftOperand()));
    else {
      if (!"\"\"".equals(¢.getLeftOperand() + "") || ¢.getRightOperand().resolveTypeBinding().isPrimitive())
        return null;
      $.setExpression(copy.of(¢.getRightOperand()));
    }
    $.setName(¢.getAST().newSimpleName("toString"));
    return $;
  }

  @Nullable @Override @SuppressWarnings("unused") public String description(final InfixExpression __) {
    return null;
  }
}
