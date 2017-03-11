package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Min between two expressions
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
public final class Min extends NanoPatternTipper<ConditionalExpression> {
  private static final long serialVersionUID = 6090319353585840250L;
  private static final Collection<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X1 > $X2 ? $X2 : $X1", "min($X1,$X2)", "min"));
      add(patternTipper("$X2 < $X1 ? $X2 : $X1", "min($X1,$X2)", "min"));
      add(patternTipper("$X1 >= $X2 ? $X2 : $X1", "min($X1,$X2)", "min"));
      add(patternTipper("$X2 <= $X1 ? $X2 : $X1", "min($X1,$X2)", "min"));
    }
  };

  @Override public boolean check(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Min between two expressions";
  }
}
