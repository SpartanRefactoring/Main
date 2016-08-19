package il.org.spartan.refactoring.wring;

import static il.org.spartan.idiomatic.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
<<<<<<< b67b65cd2e60e8d8282df4e23c701b5b407a3943

=======
>>>>>>> Fix rogue files
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.utils.*;

<<<<<<< b67b65cd2e60e8d8282df4e23c701b5b407a3943
import il.org.spartan.refactoring.utils.*;

=======
>>>>>>> Fix rogue files
/** Replace <code>(double)X</code> by <code>1.*X</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToLong2Multiply1L extends Wring.ReplaceCurrentNode<CastExpression> implements Kind.NoImpact {
  @Override String description(final CastExpression e) {
    return "Use 1L*" + expression(e) + " instead of (long)" + expression(e);
  }
<<<<<<< b67b65cd2e60e8d8282df4e23c701b5b407a3943
=======

>>>>>>> Fix rogue files
  @Override ASTNode replacement(final CastExpression e) {
    return eval(//
        () -> replacement(expression(e))//
    ).when(//
        type(e).isPrimitiveType() && "long".equals("" + type(e)) //
    );
  }
<<<<<<< b67b65cd2e60e8d8282df4e23c701b5b407a3943
  private static InfixExpression replacement(final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }
=======

  private static InfixExpression replacement(final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }

>>>>>>> Fix rogue files
  private static NumberLiteral literal(final Expression e) {
    final NumberLiteral $ = e.getAST().newNumberLiteral();
    $.setToken("1L");
    return $;
  }
}
