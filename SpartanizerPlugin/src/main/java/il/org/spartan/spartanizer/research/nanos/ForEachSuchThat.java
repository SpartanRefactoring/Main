package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public class ForEachSuchThat extends ForEach {
  private static final String description = "ForEachThat pattern: conevrt to fluent API";
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($T $N1 : $N2) if($X1) $X2;", "$N2.stream().filter($N1 -> $X1).forEach($N1 -> $X2);", description));
      add(patternTipper("for($T $N1 : $X1) if($X2) $X3;", "($X1).stream().filter($N1 -> $X2).forEach($N1 -> $X3);", description));
      add(patternTipper("for($T $N1 : $N2) if($X1) try{ $X2; } catch($T2 $N3) $B",
          "$N2.stream().filter($N1 -> $X1).forEach($N1 -> { try{$X2;} catch($T2 $N3) $B});", description));
      add(patternTipper("for($T $N1 : $X1) if($X2) try{ $X3; } catch($T2 $N3) $B ",
          "($X1).stream().filter($N1 -> $X2).forEach($N1 -> {try{ $X3; } catch($T2 $N3) $B});", description));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢) && nonTips(rivals, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Iterate a collection and apply a statement for each element";
  }

  @Override public String technicalName() {
    return "ForEachEInCSatisfyingXApplyS";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
