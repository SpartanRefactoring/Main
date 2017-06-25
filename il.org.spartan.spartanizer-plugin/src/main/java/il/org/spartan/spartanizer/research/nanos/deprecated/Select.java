package il.org.spartan.spartanizer.research.nanos.deprecated;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 17, 2016 */
public class Select extends NanoPatternTipper<EnhancedForStatement> {
  private static final String DESCRIPTION = "Go Fluent: filter pattern";
  private static final long serialVersionUID = 0x2EFFEB4AF79B74AEL;
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = as.list(
      patternTipper("for($T $N1 : $X1) if($X2) $N2.add($N3);", //
          "$N2.addAll($X1.stream().filter($N1 -> $X2).collect(toList()));", //
          DESCRIPTION), //
      patternTipper("for($T $N1 : $X1) if($X2) $N2.add($X3);", //
          "$N2.addAll($X1.stream().filter($N1 -> $X2).map($N1 -> $X3).collect(toList()));", //
          DESCRIPTION)//
  );

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }
  @Override public Category category() {
    return Category.Iterative;
  }
  @Override public String example() {
    return firstPattern(tippers);
  }
  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
