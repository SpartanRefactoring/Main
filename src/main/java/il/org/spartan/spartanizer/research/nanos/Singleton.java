package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** collection.size() == 1
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-28 */
public final class Singleton extends NanoPatternTipper<InfixExpression> {
  private static final List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.size() == 1", "singleton($X)", "is singleton"));
      add(patternTipper("1 == $X.size()", "singleton($X)", "is singleton"));
    }
  };

  @Override public boolean canTip(final InfixExpression ¢) {
    return anyTips(tippers, ¢);
  }

  @Override @Nullable public Tip pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Boolean expression that checks whether a collection is of size 1";
  }

  @Override public String technicalName() {
    return "singleton(X)";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }

  @Override @NotNull public NanoPatternTipper.Category category() {
    return Category.Iterative;
  }
}
