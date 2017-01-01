package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since Dec 13, 2016 */
public final class Between extends NanoPatternTipper<InfixExpression> {
  List<UserDefinedTipper<InfixExpression>> tippers = new ArrayList<UserDefinedTipper<InfixExpression>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("$X1 < $N", "Go fluent: Bwtween"));
      add(TipperFactory.patternTipper("$X1 <= $N && $N < $X2", "is.value($N).between($X1).inclusive().and($X2)", "Go fluent: Bwtween"));
      add(TipperFactory.patternTipper("$X1 < $N && $N <= $X2", "is.value($N).between($X1).and($X2).inclusive()", "Go fluent: Bwtween"));
      add(TipperFactory.patternTipper("$X1 <= $N && $N <= $X2", "is.value($N).between($X1).inclusive().and($X2).inclusive()", "Go fluent: Bwtween"));
    }
  };

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Go fluent: Between pattern";
  }

  @Override public boolean canTip(@SuppressWarnings("unused") final InfixExpression ¢) {
    // iz.
    //// extendedOperands(¢).stream().filter(o -> iz.)
    return false;
  }

  @Override public Tip pattern(final InfixExpression ¢) {
    return firstTip(tippers, ¢);
  }
}
