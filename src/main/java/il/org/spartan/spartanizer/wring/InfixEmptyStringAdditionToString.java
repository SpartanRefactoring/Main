package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** transforms "" + x to x when x is of type String
 * @author Stav Namir
 * @author Shalmon Niv
 * @since 2016-08-29 */
public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression>
    implements il.org.spartan.spartanizer.wring.Kind.NoImpact {
  private static boolean isEmptyStringLiteral(final Expression x) {
    return wizard.same(x, x.getAST().newStringLiteral());
  }
  
  private static boolean validTypes(final Expression ¢1, final Expression ¢2) {
    return (type.get(¢1) == type.Primitive.Certain.STRING && isEmptyStringLiteral(¢2));
  }

  private static String descriptionAux(final Expression x) {
    return "Use " + (x != null ? x + "" : "the variable alone");
  }
  
  @Override public String description() {
    return descriptionAux(null);
  }

  @Override String description(final InfixExpression x) {
    return descriptionAux(isEmptyStringLiteral(step.right(x)) ? step.left(x) : step.right(x));
  }

  @Override Expression replacement(final InfixExpression x) {
    if (x.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> es = extract.allOperands(x);
    assert es.size() > 1;
    final int ¢ = es.size();
    for(int i = 0; i < es.size()-1 ;){
      if (validTypes(es.get(i),es.get(i+1)))
        es.remove(i+1);
      else if (validTypes(es.get(i+1),es.get(i)))
        es.remove(i);
      else
        ++i;
    }
    return es.size() == ¢ ? null : es.size() == 1 ? lisp.first(es) : subject.operands(es).to(wizard.PLUS2);
  }
}
