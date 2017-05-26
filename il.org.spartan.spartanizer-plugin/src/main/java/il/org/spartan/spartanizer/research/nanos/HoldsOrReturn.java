package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** if(X) [return | return null];
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-22 */
public final class HoldsOrReturn extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = -0x4931BB748CF56773L;
  private static final NotNullAssumed rival = new NotNullAssumed();
  private static final List<UserDefinedTipper<IfStatement>> tippers = as.list(//
      patternTipper("if($X1) return $D;", "holds(!($X1)).orReturn($D);", ""), //
      patternTipper("if($X1) return;", "holds(!($X1)).orReturn();", "") //
  );

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢) && rival.cantTip(¢);
  }
  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
