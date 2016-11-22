package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Reduce extends NanoPatternTipper<EnhancedForStatement> {
  Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for ($N0<$N1,$N2> $N3 : $N4.$N5()) $N6+=$N3.$N7();", "reduce();", "reduce"));
      add(TipperFactory.patternTipper("for (boolean[] $N0 : $N1) $N2+=$N0.$N2;", "reduce();", "reduce"));
      add(TipperFactory.patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2 += $N3.$N4($N5[($N0 & $N6)], $N7[($N0 & $N6)]);", "reduce();",
          "reduce"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement s) {
    for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
      if (¢.canTip(s))
        return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "Reduce pattern: conevrt to fluent API";
  }

  @Override public Tip tip(final EnhancedForStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
          if (¢.canTip(s)) {
            ¢.tip(s).go(r, g);
            Logger.logNP(s, getClass() + "");
            return;
          }
        assert false;
      }
    };
  }
}
