package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** There's also {@link Infix.SafeNavigation} which catches the same
 * pattern @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public final class SafeNavigation extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = -4108306692245103780L;
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = as.list(
      patternTipper("$N == null ? $D : $N.$N2", "safe($N).get(()->$N.$N2)", "safe reference"),
      patternTipper("$N != null ? $N.$N2 : $D", "safe($N).get(()->$N.$N2)", "safe reference"),
      patternTipper("null == $N ? $D : $N.$N2", "safe($N).get(()->$N.$N2)", "safe reference"),
      patternTipper("null != $N ? $N.$N2 : $D", "safe($N).get(()->$N.$N2)", "safe reference"),
      patternTipper("$SN1 == null ? $D : $SN1.$SN2.$SN3()", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"),
      patternTipper("$SN1 != null ? $SN1.$SN2.$SN3() : $D", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"),
      patternTipper("null == $SN1 ? $D : $SN1.$SN2.$SN3()", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"),
      patternTipper("null != $SN1? $SN1.$SN2.$SN3() : $D", "safe($SN1).invoke(()->$SN1.$SN2.$SN3())", "safe reference"),
      patternTipper("$N == null ? $D : $N.$SN()", "safe($N).invoke(()->$N.$SN())", "safe reference"),
      patternTipper("$N != null ? $N.$SN() : $D", "safe($N).invoke(()->$N.$SN())", "safe reference"),
      patternTipper("null == $N ? $D : $N.$SN()", "safe($N).invoke(()->$N.$SN())", "safe reference"),
      patternTipper("null != $N ? $N.$SN() : $D", "safe($N).invoke(()->$N.$SN())", "safe reference"));

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable @Override public Fragment pattern(@NotNull final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "A field access or an invocation where the callee is checked to be non-null and if is, evaluates to a default value";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @NotNull @Override public Category category() {
    return Category.Safety;
  }
}
