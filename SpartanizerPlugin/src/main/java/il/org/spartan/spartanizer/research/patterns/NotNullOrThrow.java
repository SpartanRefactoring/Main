package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** if(X == null) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class NotNullOrThrow extends NanoPatternTipper<IfStatement> {
  private static final UserDefinedTipper<IfStatement> tipper = //
      patternTipper("if($X1 == null) throw $X2;", "notNull($X1).orThrow(()->$X2);", "If null throw pattern");

  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return tipper.tip(¢);
  }

  @Override public String category() {
    return Category.Throw + "";
  }

  @Override public String description() {
    return "Throw if variable is null";
  }
}
