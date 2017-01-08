package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public final class PercolateException extends NanoPatternTipper<CatchClause> {
  private static final Set<UserDefinedTipper<TryStatement>> tippers = new HashSet<UserDefinedTipper<TryStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("try $B1 catch($T $N){ throw $N;}", "try $B1 catch($T $N){percolate($N);};", ""));
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
    return "catchXThrowX";
  }
}
