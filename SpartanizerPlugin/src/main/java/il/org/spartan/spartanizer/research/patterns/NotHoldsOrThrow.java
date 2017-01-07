package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** if(X) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class NotHoldsOrThrow extends NanoPatternTipper<IfStatement> {
  private static final NotNullOrThrow rival = new NotNullOrThrow();
  private static final UserDefinedTipper<IfStatement> tipper = //
      patternTipper("if($X1) throw $X2;", "holds(!($X1)).orThrow(()->$X2);", "IfThrow pattern. Go fluent!");

  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢) && rival.cantTip(¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return tipper.tip(¢);
  }

  @Override public Category category() {
    return Category.Throw;
  }

  @Override public String description() {
    return "Throw if condition doesn't hold";
  }
}
