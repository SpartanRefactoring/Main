package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Catch without body
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
public final class SuppressException extends NanoPatternTipper<CatchClause> {
  private static final long serialVersionUID = -8859938289108985941L;
  private static final List<UserDefinedTipper<TryStatement>> tippers = new ArrayList<UserDefinedTipper<TryStatement>>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      add(patternTipper("try $B1 catch($T $N){}", "try $B1 catch($T $N){ignore();};", ""));
    }
  };

  @Override public boolean canTip(final CatchClause ¢) {
    return anyTips(tippers, step.parent(¢));
  }

  @Override public Tip pattern(final CatchClause ¢) {
    return firstTip(tippers, step.parent(¢));
  }

  @Override public Category category() {
    return Category.Exception;
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
