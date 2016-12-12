package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** if(X) throw Exception;
 * @author Ori Marcovitch
 * @year 2016 */
public final class IfThrow extends NanoPatternTipper<IfStatement> {
  private static final IfNullThrow nullTipper = new IfNullThrow();
  private static final UserDefinedTipper<IfStatement> tipper = TipperFactory.patternTipper("if($X) throw $X2;", "If.True($X).throwz(() -> $X2);",
      "IfThrow pattern. Go fluent!");

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "";
  }

  @Override public boolean canTip(final IfStatement ¢) {
    return tipper.canTip(¢) && nullTipper.cantTip(¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return tipper.tip(¢);
  }
}
