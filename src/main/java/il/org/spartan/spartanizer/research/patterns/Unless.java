package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @since Dec 13, 2016 */
public final class Unless extends NanoPatternTipper<ConditionalExpression> {
  List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X1 ? null : $X2", "unless($X1).eval(() -> $X2)", "Go fluent: Unless pattern"));
      add(patternTipper("$X1  ? $X2 : null", "unless(!$X1).eval(() -> $X2)", "Go fluent: Unless pattern"));
    }
  };
  static DefaultsTo rival = new DefaultsTo();

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }
}
