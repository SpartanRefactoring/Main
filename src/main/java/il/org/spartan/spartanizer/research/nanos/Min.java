package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Min between two expressions
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
public final class Min extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = 6090319353585840250L;
  private static final Collection<UserDefinedTipper<ConditionalExpression>> tippers = as.list(
      patternTipper("$X1 > $X2 ? $X2 : $X1", "min($X1,$X2)", "min"), patternTipper("$X2 < $X1 ? $X2 : $X1", "min($X1,$X2)", "min"),
      patternTipper("$X1 >= $X2 ? $X2 : $X1", "min($X1,$X2)", "min"), patternTipper("$X2 <= $X1 ? $X2 : $X1", "min($X1,$X2)", "min"));

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Nullable @Override public Fragment pattern(@NotNull final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Min between two expressions";
  }
}
