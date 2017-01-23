package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO:  orimarco <tt>marcovitch.ori@gmail.com</tt>
 please add a description 
 @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 
 */

public final class TakeDefaultTo extends NanoPatternTipper<ConditionalExpression> {
  private static final List<UserDefinedTipper<ConditionalExpression>> tippers = new ArrayList<UserDefinedTipper<ConditionalExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X1 != null ? $X2 : $X3", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent"));
      add(patternTipper("$X1 == null ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent"));
      add(patternTipper("null != $X1 ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent"));
      add(patternTipper("null == $X1 ? $X3 : $X2", "take($X2).default¢($X1).to($X3)", "takeDfaultTo pattern: Go fluent"));
    }
  };
  static final DefaultsTo rival = new DefaultsTo();

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(¢);
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Evaluate expression, if null take some expression, otherwise take another";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @Override public Category category() {
    return Category.Default;
  }
}

