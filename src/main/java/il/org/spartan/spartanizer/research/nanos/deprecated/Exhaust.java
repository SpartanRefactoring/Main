package il.org.spartan.spartanizer.research.nanos.deprecated;

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

public class Exhaust extends NanoPatternTipper<WhileStatement> {
  private static final List<UserDefinedTipper<WhileStatement>> tippers = new ArrayList<UserDefinedTipper<WhileStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("while ($X) {}", "exhaust(()->$X);", "Exhaust pattern: conevrt to fluent API"));
    }
  };

  @Override public boolean canTip(final WhileStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final WhileStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String description() {
    return "Move an Iterable to its end using getNext() != null";
  }
}

