package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public final class SafeInvocation extends NanoPatternTipper<ConditionalExpression> {
  private static final Set<UserDefinedTipper<ConditionalExpression>> tippers = new HashSet<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$N == null ? null : $N.$N2()", "safe($N).invoke(()->$N.$N2())", "safe reference"));
      add(patternTipper("$N != null ? $N.$N2() : null", "safe($N).invoke(()->$N.$N2())", "safe reference"));
      add(patternTipper("null == $N ? null : $N.$N2()", "safe($N).invoke(()->$N.$N2())", "safe reference"));
      add(patternTipper("null != $N ? $N.$N2() : null", "safe($N).invoke(()->$N.$N2())", "safe reference"));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }
}
