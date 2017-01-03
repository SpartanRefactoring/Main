package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public final class SafeReference extends NanoPatternTipper<ConditionalExpression> {
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$N == null ? null : $N.$N2", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("$N != null ? $N.$N2 : null", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("null == $N ? null : $N.$N2", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("null != $N ? $N.$N2 : null", "safe($N).get(()->$N.$N2)", "safe reference"));
      add(patternTipper("$SN1 == null ? null : $SN1.$SN2.$SN3()", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"));
      add(patternTipper("$SN != null ? $SN.$SN2.$SN3() : null", "safe($SN).invoke(()->$SN.$SN2.$SN3())", "safe reference"));
      add(patternTipper("null == $SN ? null : $SN.$SN2.$SN3()", "safe($SN).invoke(()->$SN.$SN2.$SN3())", "safe reference"));
      add(patternTipper("null != $SN? $SN.$SN2.$SN3() : null", "safe($SN).invoke(()->$SN.$SN2.$SN3())", "safe reference"));
      add(patternTipper("$N == null ? null : $N.$SN()", "safe($N).invoke(()->$N.$SN())", "safe reference"));
      add(patternTipper("$N != null ? $N.$SN() : null", "safe($N).invoke(()->$N.$SN())", "safe reference"));
      add(patternTipper("null == $N ? null : $N.$SN()", "safe($N).invoke(()->$N.$SN())", "safe reference"));
      add(patternTipper("null != $N ? $N.$SN() : null", "safe($N).invoke(()->$N.$SN())", "safe reference"));
    }
  };

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }
}
