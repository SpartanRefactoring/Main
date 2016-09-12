package il.org.spartan.spartanizer.wring;

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
public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  @Override public String description() {
    return "[\"\"+foo]->foo";
  }

  @Override String description(final InfixExpression x) {
    return "Eliminate concatentation of \"\" to" + (iz.emptyStringLiteral(right(x)) ? left(x) : right(x));
  }

  @Override Expression replacement(final InfixExpression x) {
    if (type.get(x) != Certain.STRING)
      return null;
    final List<Expression> es = hop.operands(x);
    assert es.size() > 1;
    final List<Expression> ¢ = new ArrayList<>();
    boolean isString = false;
    for (int i = 0; i < es.size(); ++i) {
      final Expression e = es.get(i);
      if (!iz.emptyStringLiteral(e)) {
        ¢.add(e);
        if (type.get(e) == Certain.STRING)
          isString = true;
      } else {
        if (i < es.size() - 1 && type.get(es.get(i + 1)) == Certain.STRING)
          continue;
        if (!isString) {
          ¢.add(e);
          isString = true;
        }
      }
    }
    return ¢.size() == es.size() ? null : ¢.size() == 1 ? ¢.get(0) : subject.operands(¢).to(PLUS2);
  }
}
