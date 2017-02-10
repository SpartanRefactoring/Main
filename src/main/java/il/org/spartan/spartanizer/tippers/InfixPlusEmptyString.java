package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Converts {@code ""+"foo"} to {@code "foo"} when x is of type String
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-08-29 */
public final class InfixPlusEmptyString extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onStrings {
  @Override public String description() {
    return "[\"\"+foo]->foo";
  }

  @Override public String description(final InfixExpression ¢) {
    return "Omit concatentation of \"\" to" + (iz.emptyStringLiteral(right(¢)) ? left(¢) : right(¢));
  }

  @Override @SuppressWarnings("boxing") public Expression replacement(final InfixExpression x) {
    if (type.of(x) != Certain.STRING)
      return null;
    final List<Expression> es = hop.operands(x);
    assert es.size() > 1;
    final List<Expression> $ = new ArrayList<>();
    boolean isArithmetic = true;
    for (final Integer i : range.from(0).to(es.size())) {
      final Expression e = es.get(i);
      if (!iz.emptyStringLiteral(e)) {
        $.add(e);
        if (type.of(e) == Certain.STRING)
          isArithmetic = false;
      } else {
        if (i < es.size() - 1 && type.of(es.get(i + 1)) == Certain.STRING)
          continue;
        if (isArithmetic) {
          $.add(e);
          isArithmetic = false;
        }
      }
    }
    return $.size() == es.size() ? null : $.size() == 1 ? first($) : subject.operands($).to(PLUS2);
  }
}
