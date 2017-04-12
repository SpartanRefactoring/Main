package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Catch exception, then throw it again
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public final class PercolateException extends NanoPatternTipper<CatchClause> {
  private static final long serialVersionUID = 0x31FED4316A03FE00L;
  private static final List<UserDefinedTipper<TryStatement>> tippers = as
      .list(patternTipper("try $B1 catch($T $N1){ throw $N2;}", "try $B1 catch($T $N1){percolate($N2);};", ""));

  @Override public boolean canTip(final CatchClause ¢) {
    return anyTips(tippers, parentAsTryStatement(¢));
  }

  @Override public Tip pattern(final CatchClause ¢) {
    return firstTip(tippers, parentAsTryStatement(¢));
  }

  @Override public Category category() {
    return Category.Exception;
  }

  private static TryStatement parentAsTryStatement(final CatchClause ¢) {
    return az.tryStatement(parent(¢));
  }

  @Override public String technicalName() {
    return "catchXThrowX";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
