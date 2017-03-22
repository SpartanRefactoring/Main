package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.Nullable;

/** if(X) [return | return null];
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-22 */
public final class HoldsOrReturn extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = -5274202747883054963L;
  private static final NotNullOrReturn rival = new NotNullOrReturn();
  private static final List<UserDefinedTipper<IfStatement>> tippers = as.list(//
      patternTipper("if($X1) return $D;", "holds(!($X1)).orReturn($D);", ""), //
      patternTipper("if($X1) return;", "holds(!($X1)).orReturn();", "") //
  );

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(¢);
  }

  @Nullable @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
