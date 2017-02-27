package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** if(X) throw Exception;
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class NotHoldsOrThrow extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 9147240551145375646L;
  private static final NotNullOrThrow rival = new NotNullOrThrow();
  private static final List<UserDefinedTipper<IfStatement>> tippers = new ArrayList<UserDefinedTipper<IfStatement>>() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("if($X1) throw $X2;", "holds(!($X1)).orThrow(()->$X2);", "IfThrow pattern. Go fluent!"));
    }
  };

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢) //
        && rival.cantTip(¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public Category category() {
    return Category.Exception;
  }

  @Override public String description() {
    return "Throw if condition doesn't hold";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
