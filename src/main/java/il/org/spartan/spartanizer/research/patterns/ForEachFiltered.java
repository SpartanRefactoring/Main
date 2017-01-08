package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public class ForEachFiltered extends ForEach {
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($N1 $N2 : $N3) if($X1) $X2;", "$N3.stream().filter($N2 -> $X1).forEach($N2 -> $X2);",
          "ForEachThat pattern: conevrt to fluent API"));
      add(patternTipper("for($N1 $N2 : $X1) if($X2) $X3;", "($X1).stream().filter($N2 -> $X2).forEach($N2 -> $X3);",
          "ForEachThat pattern: conevrt to fluent API"));
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
}
