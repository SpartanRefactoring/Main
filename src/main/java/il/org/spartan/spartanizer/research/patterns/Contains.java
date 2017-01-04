package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public class Contains extends NanoPatternTipper<Block> {
  private static final String description = "Aggregate a collection into single element";
  private static final String replacement = "return collection($X).contains($X1);";
  private static final Set<UserDefinedTipper<Block>> tippers = new HashSet<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("for ($T $N : $X) if ($N == $X1) return true; return false;", replacement, description));
      add(statementsPattern("for ($T $N : $X) if ($X1 == $N) return true; return false;", replacement, description));
      add(statementsPattern("for ($T $N : $X) if ($N.equals($X1)) return true; return false;", replacement, description));
      add(statementsPattern("for ($T $N : $X) if ($X1.equals($N)) return true; return false;", replacement, description));
    }
  };

  @Override public String description() {
    return description;
  }

  @Override public boolean canTip(final Block ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final Block ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String category() {
    return Category.Relational + "";
  }
}
