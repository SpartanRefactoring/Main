package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.ast.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;

/** Converts <code>""+"foo"</code> to <code>"foo"</code> when x is of type
 * String
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-08-29 */
public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static boolean validTypes(final Expression ¢1, final Expression ¢2) {
    return type.get(¢1) == Certain.STRING && iz.emptyStringLiteral(¢2);
  }

  @Override public String description() {
    return "[\"\"+foo]->foo";
  }

  @Override String description(final InfixExpression x) {
    return "Eliminate concatentation of \"\" to" + (iz.emptyStringLiteral(right(x)) ? left(x) : right(x));
  }

  @Override Expression replacement(final InfixExpression x) {
    if (!iz.infixPlus(x))
      return null;
    // TODO: Niv, I believe this is one of the problems here. Extract all
    // operands opens parenthesis. Maybe use hop.operands?
    final List<Expression> es = extract.allOperands(x);
    assert es.size() > 1;
    final int ¢ = es.size();
    // TODO: Niv, I am pretty sure the following is buggy. It is too complex to
    // be correct. In fact, if you apply the plugin to itself, you will find the
    // bug... I believe that it is in Wrapping. The right way to do this is to
    // create a new list, and move to it only what's left. Please open an issue,
    // create test cases to demonstrate the problem, and then fix it.
    for (int i = 0; i < es.size() - 1;)
      if (validTypes(es.get(i), es.get(i + 1)))
        es.remove(i + 1);
      else if (!validTypes(es.get(i + 1), es.get(i)))
        ++i;
      else
        es.remove(i);
    return es.size() == ¢ ? null : es.size() == 1 ? first(es) : subject.operands(es).to(PLUS2);
  }
}
