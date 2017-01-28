package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
public final class SupressException extends NanoPatternTipper<CatchClause> {
  private static final List<UserDefinedTipper<TryStatement>> tippers = new ArrayList<UserDefinedTipper<TryStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("try $B1 catch($T $N){}", "try $B1 catch($T $N){ignore();};", ""));
    }
  };

  @Override public boolean canTip(final CatchClause ¢) {
    return anyTips(tippers, parent(¢));
  }

  @Override public Tip pattern(final CatchClause ¢) {
    return firstTip(tippers, parent(¢));
  }

  @Override public Category category() {
    return Category.Exception;
  }

  private static TryStatement parent(final CatchClause ¢) {
    return az.tryStatement(step.parent(¢));
  }

  @Override public String technicalName() {
    return "catchXIgnore";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
