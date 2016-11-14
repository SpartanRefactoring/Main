package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @year 2016 */
public final class FindFirst extends NanoPatternTipper<EnhancedForStatement> {
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) if($X2) return $N2;", "return findFirstIn($X1).satisfying(($N2) -> $X2);", "FindFirst"));
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) if($X2) {$N3 = $N2; break;}", "$N3 = findFirstIn($X1).satisfying(($N2) -> $X2);",
          "FindFirst"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "";
  }

  @Override public boolean canTip(final EnhancedForStatement x) {
    for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  @Override public Tip tip(final EnhancedForStatement x) {
    for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
