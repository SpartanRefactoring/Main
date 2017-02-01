package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** collection.size() == 0
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-01 */
public final class Empty extends NanoPatternTipper<InfixExpression> {
  private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.size() == 0", "empty($X)", "is empty"));
      add(patternTipper("0 == $X.size()", "empty($X)", "is empty"));
    }
  };

  @Override public boolean canTip(final InfixExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
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
