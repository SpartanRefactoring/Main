package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfThrowsReturnNull extends NanoPatternTipper<TryStatement> {
  private static final Set<UserDefinedTipper<TryStatement>> tippers = new HashSet<UserDefinedTipper<TryStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("try $B1 catch($T $N){ return null; }", "If.throwz(() -> $B1).returnNull();", "Go Fluent: IfThrowsReturnNull"));
    }
  };

  @Override public boolean canTip(final TryStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final TryStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Return + "";
  }
}
