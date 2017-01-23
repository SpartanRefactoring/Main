package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/**  if(X == null) throw Exception;
 * @year 2016 
 * @author Ori Marcovitch
 * @since Jan 8, 2017
 */

public final class NotNullOrThrow extends NanoPatternTipper<IfStatement> {
  private static final List<UserDefinedTipper<IfStatement>> tippers = new ArrayList<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("if($X1 == null) throw $X2;", "notNull($X1).orThrow(()->$X2);", "If null throw pattern"));
    }
  };

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public Category category() {
    return Category.Exception;
  }

  @Override public String description() {
    return "Throw if variable is null";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}

