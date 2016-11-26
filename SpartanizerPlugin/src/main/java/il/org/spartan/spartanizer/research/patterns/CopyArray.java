package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class CopyArray extends NanoPatternTipper<ForStatement> {
  Set<UserDefinedTipper<ForStatement>> tippers = new HashSet<UserDefinedTipper<ForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for (int $N0 = 0; $N0 < $N1; ++$N0)  $N2[$N0] = $N3[$N0];", "copy();", "copy"));
    }
  };

  @Override public boolean canTip(final ForStatement s) {
    for (final UserDefinedTipper<ForStatement> ¢ : tippers)
      if (¢.canTip(s))
        return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Init array: conevrt to fluent API";
  }

  @Override public Tip tip(final ForStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final UserDefinedTipper<ForStatement> ¢ : tippers)
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
