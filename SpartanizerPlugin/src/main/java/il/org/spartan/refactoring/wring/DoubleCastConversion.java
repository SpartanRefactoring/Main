package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** A {@link Wring} that replaces the (double)X cast {@link Expressions} with the form of '1.*X'
 * @author Alex and Dan
 * @since 2016-09-15 */
public final class DoubleCastConversion extends ReplaceCurrentNode<CastExpression> {
  @Override String description(final CastExpression e) {
    return "Replace all (double) casts to 1.* in " + e;
  }
  @Override ASTNode replacement(CastExpression e) {
    return (!(e.getType().isPrimitiveType())) ? null : ((e.getType().toString() != "double") ? null : replaceDoubleToOne(e));
  }
  /**The actual replace in done here.
   * @param e JD
   * @return {@link ASTNode} which represents cast to (double).
   */
  private static ASTNode replaceDoubleToOne(final CastExpression e) {
    List<Expression> $ = new ArrayList<>();
    $.add("1."); //how to convert a string
    $.add(e.getExpression());
    return subject.operands($).to(TIMES);
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}