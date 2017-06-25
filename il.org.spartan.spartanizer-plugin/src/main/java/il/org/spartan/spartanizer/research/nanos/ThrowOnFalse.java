package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tipping.*;

/** if(X) throw Exception;
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class ThrowOnFalse extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x7EF186D12E11879EL;
  private static final ThrowOnNull rival = new ThrowOnNull();
  private static final List<UserDefinedTipper<IfStatement>> tippers = as.list(//
      patternTipper("if($X1) throw $X2;", "holds(!($X1)).orThrow(()->$X2);", "IfThrow pattern. Go fluent!"));

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(¢);
  }
  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public String description() {
    return "Throw if condition doesn't hold";
  }
}
