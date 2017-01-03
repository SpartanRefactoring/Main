package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class LispLastIndex extends NanoPatternTipper<InfixExpression> {
  private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.size()-1", "lastIndex($X)", "lisp: lastIndex"));
    }
  };

  @Override public boolean canTip(final InfixExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Lisp + "";
  }
}
