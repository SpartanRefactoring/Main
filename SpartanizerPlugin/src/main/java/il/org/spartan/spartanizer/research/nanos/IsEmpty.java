package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** collection.size() == 0
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-01 */
public final class IsEmpty extends NanoPatternTipper<InfixExpression> {
  private static final long serialVersionUID = 0xE2C7F278DB4B7AEL;
  private static final NanoPatternContainer<InfixExpression> tippers = new NanoPatternContainer<InfixExpression>()
      .add("$X.size() == 0", "empty($X)", "is empty").add("0 == $X.size()", "empty($X)", "is empty").add("$X.isEmpty()", "empty($X)", "is empty");

  @Override public boolean canTip(final InfixExpression ¢) {
    return tippers.canTip(¢);
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
