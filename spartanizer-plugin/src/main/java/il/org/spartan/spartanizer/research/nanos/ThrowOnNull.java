package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.List;

import org.eclipse.jdt.core.dom.IfStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** if(X == null) throw Exception;
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class ThrowOnNull extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x1159726E51E57D02L;
  private static final List<UserDefinedTipper<IfStatement>> tippers = as.list( //
      patternTipper("if($X1 == null) throw $X2;", "NonNull($X1).orThrow(()->$X2);", "If null throw pattern")//
  );

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public String description() {
    return "Throw if variable is null";
  }
}
