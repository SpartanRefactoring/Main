package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public class Exhaust extends NanoPatternTipper<WhileStatement> {
  private static final List<UserDefinedTipper<WhileStatement>> tippers = new ArrayList<UserDefinedTipper<WhileStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("while ($X != null) {}", "exhaust(()->$X);", "Exhaust pattern: conevrt to fluent API"));
    }
  };

  @Override public boolean canTip(final WhileStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final WhileStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Iterative + "";
  }
}
