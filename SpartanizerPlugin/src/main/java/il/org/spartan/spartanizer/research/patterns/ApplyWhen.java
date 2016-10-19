package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace if(X) Y; with Y #when X
 * @author Ori Marcovitch
 * @year 2016 */
public final class ApplyWhen extends NanoPatternTipper<IfStatement> implements TipperCategory.CommnoFactoring {
  @SuppressWarnings("serial") Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    {
      add(TipperFactory.tipper("if($X) $N($A);", "applyWhen($X, $N($A));", ""));
      add(TipperFactory.tipper("if($X1) $X2.$N($A);", "applyWhen($X1, $X2.$N($A));", ""));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "applyWhen";
  }

  @Override public boolean canTip(final IfStatement x) {
    for (final UserDefinedTipper<IfStatement> ¢ : tippers)
      if (¢.canTip(x))
        return true;
    return false;
  }

  @Override public Tip tip(final IfStatement x) throws TipperFailure {
    Logger.logNP(x, "ApplyWhen");
    for (final UserDefinedTipper<IfStatement> ¢ : tippers)
      if (¢.canTip(x))
        return ¢.tip(x);
    assert false;
    return null;
  }
}
