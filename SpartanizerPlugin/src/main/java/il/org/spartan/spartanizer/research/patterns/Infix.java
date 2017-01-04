package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** Just another form of {@link SafeReference}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-04 */
public final class Infix {
  public static class SafeReference extends NanoPatternTipper<InfixExpression> {
    private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
      static final long serialVersionUID = 1L;
      {
        add(patternTipper("$N1 != null && $N1.$N2", "safe($N1).get(()->$N1.$N2)", "safe reference"));
        add(patternTipper("null != $N1 && $N1.$N2", "safe($N1).get(()->$N1.$N2)", "safe reference"));
        //
        add(patternTipper("$SN1 != null && $SN1.$SN2.$SN3()", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"));
        add(patternTipper("null != $SN1 && $SN1.$SN2.$SN3()", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"));
        //
        add(patternTipper("$N1 != null && $N1.$SN2()", "safe($N1).invoke(()->$N1.$SN2())", "safe reference"));
        add(patternTipper("null != $N1 && $N1.$SN2()", "safe($N1).invoke(()->$N1.$SN2())", "safe reference"));
      }
    };

    @Override public boolean canTip(final InfixExpression ¢) {
      return anyTips(tippers, ¢);
    }

    @Override public Tip pattern(final InfixExpression ¢) {
      return firstTip(tippers, ¢);
    }

    @Override public String category() {
      return Category.Conditional + "";
    }
  }
}
