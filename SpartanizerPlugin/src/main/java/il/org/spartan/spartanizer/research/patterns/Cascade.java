package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.Matcher.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.statementsPattern;

/** TODO: Marco implement @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public final class Cascade extends NanoPatternTipper<Block> {
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("for($T $N : $X1) if($X2) return $N;", "return $X1.stream().findFirst($N -> $X2).get();", "Go Fluent : FindFirst",
          Option.LAST_IN_BLOCK));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return "";
  }

  @Override public boolean canTip(final Block x) {
    return anyTips(tippers, x);
  }

  @Override public Tip pattern(final Block x) {
    return firstTip(tippers, x);
  }
}
