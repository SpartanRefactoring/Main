package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** if(X == null) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfNullThrow extends NanoPatternTipper<IfStatement> {
  private static final UserDefinedTipper<IfStatement> tipper = patternTipper("if($X == null) throw $X2;", "If.Null($X).throwz(() -> $X2);",
      "If null throw pattern");

  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return tipper.tip(¢);
  }

  @Override public String category() {
    return Category.Throw + "";
  }
}
