package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @nano for(A a : B ) if(X) S;
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public class ForEachSuchThat extends ForEach {
  private static final long serialVersionUID = -0x11B2685C8CD86C61L;
  private static final String description = "ForEachSuchThat pattern: conevrt to fluent API";
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = as.list(
      patternTipper("for($T $N1 : $N2) if($X1) $X2;", "$N2.stream().filter($N1 -> $X1).forEach($N1 -> $X2);", description),
      patternTipper("for($T $N1 : $X1) if($X2) $X3;", "($X1).stream().filter($N1 -> $X2).forEach($N1 -> $X3);", description),
      patternTipper("for($T $N1 : $N2) if($X1) try{ $X2; } catch($T2 $N3) $B",
          "$N2.stream().filter($N1 -> $X1).forEach($N1 -> { try{$X2;} catch($T2 $N3) $B});", description),
      patternTipper("for($T $N1 : $X1) if($X2) try{ $X3; } catch($T2 $N3) $B ",
          "($X1).stream().filter($N1 -> $X2).forEach($N1 -> {try{ $X3; } catch($T2 $N3) $B});", description));

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢)//
        && nonTips(rivals, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Iterate a collection and apply a statement for each element";
  }

  @Override public String tipperName() {
    return ForEach.class.getSimpleName();
  }
}
