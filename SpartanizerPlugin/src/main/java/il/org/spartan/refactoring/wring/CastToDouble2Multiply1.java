package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** Replace <code>(double)X</code> by <code>1.*X</code>
 * @author Alex Kopzon 
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToDouble2Multiply1 extends Wring.ReplaceCurrentNode<CastExpression> {
  @Override String description(final CastExpression e) {
    return "Replace all (double) casts to 1.* in " + e;
  }
  @Override ASTNode replacement(CastExpression e) {
    return (!(e.getType().isPrimitiveType())) ? null : ((e.getType().toString() != "double") ? null : replaceDoubleToOne(e));
  }
  /** The actual replace in done here.
   * @param e JD
   * @return {@link ASTNode} which represents cast to (double). */
  private static ASTNode replaceDoubleToOne(final CastExpression e) {
    List<Expression> $ = new ArrayList<>();
    $.add((Expression) MakeAST.EXPRESSION.from("1.")); // how to convert a
                                                       // string to Expression
    $.add(e.getExpression());
    return subject.operands($).to(TIMES);
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}