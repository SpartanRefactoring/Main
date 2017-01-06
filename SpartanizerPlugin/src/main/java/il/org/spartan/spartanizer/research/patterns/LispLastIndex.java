package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
public final class LispLastIndex extends NanoPatternTipper<InfixExpression> {
  private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.size()-1", "lastIndex($X)", "lisp: lastIndex"));
    }
  };
  static final LispLastElement rival = new LispLastElement();

  @Override public boolean canTip(final InfixExpression ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(az.methodInvocation(parent(¢)));
  }

  @Override public Tip pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Functional + "";
  }

  @Override public String description() {
    return "Index of last element in collection";
  }
}
