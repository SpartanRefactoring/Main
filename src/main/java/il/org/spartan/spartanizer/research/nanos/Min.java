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
public final class Min {
  public static class SafeReference extends NanoPatternTipper<InfixExpression> {
    private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
      static final long serialVersionUID = 1L;
      {
        add(patternTipper("$X1 > $X2 ? $X2 : $X1", "max($X1,$X2)", "max"));
        add(patternTipper("$X2 < $X1 ? $X2 : $X1", "max($X1,$X2)", "max"));
        add(patternTipper("$X1 >= $X2 ? $X2 : $X1", "max($X1,$X2)", "max"));
        add(patternTipper("$X2 <= $X1 ? $X2 : $X1", "max($X1,$X2)", "max"));
      }
    };

    @Override public boolean canTip(final InfixExpression ¢) {
      return anyTips(tippers, ¢);
    }

    @Override public Tip pattern(final InfixExpression ¢) {
      return firstTip(tippers, ¢);
    }

    @Override public String description() {
      return "Min between two expressions";
    }
  }
}
