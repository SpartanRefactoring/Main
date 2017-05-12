package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** if (!$X1.containsKey($X2)) <br>
 * <tab> $X1.put($X2, $X3); <br>
 * into: <br>
 * $X1.putIfAbsent($X2, $X3);
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class PutIfAbsent extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x7E8219A58DD68684L;
  private static final List<UserDefinedTipper<IfStatement>> tippers = as.list(
      patternTipper("if (!$X1.containsKey($X2)) $X1.put($X2, $X3);", "$X1.putIfAbsent($X2, $X3);", "use putIfAbsent"),
      patternTipper("if (!containsKey($X2)) put($X2, $X3);", "putIfAbsent($X2, $X3);", "use putIfAbsent"));

  @Override public boolean canTip(final IfStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Put an element in a map if a key is absent";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
