package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    @Override public Tip pattern(final InfixExpression ¢) {
      return firstTip(tippers, ¢);
    }

    @NotNull
    @Override public Category category() {
      return Category.Safety;
    }

    @NotNull
    @Override public String description() {
      return "A field access or an invocation where the callee is checked to be non-null and if is, evaluates to a default value";
    }

    @Override public String example() {
      return firstPattern(tippers);
    }

    @Override public String symbolycReplacement() {
      return firstReplacement(tippers);
    }
  }
}
