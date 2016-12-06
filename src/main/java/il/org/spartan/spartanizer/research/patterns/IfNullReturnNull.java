package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullReturnNull extends NanoPatternTipper<IfStatement> {
  static Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("if($X == null) return null;", "If.Null($X)returnsNull();", ""));
      add(TipperFactory.patternTipper("if(null == $X) return null;", "If.Null($X)returnsNull();", ""));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "If.Null($X)returnsNull();";
  }

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip tip(final IfStatement ¢) {
    Logger.logNP(¢, "IfNullReturnNull");
    return firstThatTips(tippers, ¢).tip(¢);
  }
}
