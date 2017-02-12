package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** collection.size() == 0
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-01 */
public final class Empty extends NanoPatternTipper<InfixExpression> {
  private static final NanoPatternContainer<InfixExpression> tippers = new NanoPatternContainer<InfixExpression>() {
    static final long serialVersionUID = 1L;
    {
      patternTipper("$X.size() == 0", "empty($X)", "is empty");
      patternTipper("0 == $X.size()", "empty($X)", "is empty");
      patternTipper("$X.isEmpty()", "empty($X)", "is empty");
    }
  };

  @Override public boolean canTip(final InfixExpression ¢) {
    return tippers.anyTips(¢);
  }

  @Override public Tip pattern(final InfixExpression ¢) {
    return tippers.firstTip(¢);
  }

  @Override public String description() {
    return "Boolean expression that checks whether a collection is of size 0";
  }

  @Override public String technicalName() {
    return "empty(X)";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @Override public NanoPatternTipper.Category category() {
    return Category.Iterative;
  }
}
