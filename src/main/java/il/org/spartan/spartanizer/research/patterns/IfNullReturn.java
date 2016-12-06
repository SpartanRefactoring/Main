package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullReturn extends NanoPatternTipper<IfStatement> {
  static Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("if($X == null) return;", "precondition.notNull($X);", "replace with precondition.notNull($X)"));
      add(TipperFactory.patternTipper("if(null == $X) return;", "precondition.notNull($X);", "replace with precondition.notNull($X)"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace with precondition.notNull($X)";
  }

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip tip(final IfStatement ¢) {
    Logger.logNP(¢, "IfNullReturn");
    return firstThatTips(tippers, ¢).tip(¢);
  }
}
