package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) if($X2) $X3;", "$X1.stream().filter($N2 -> $X2).forEach($N2 -> $X3);",
          "ForEachThat pattern: conevrt to fluent API"));
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) $X2;", "$X1.stream().forEach($N2 -> $X2);", "ForEach pattern: conevrt to fluent API"));
      // TODO: Marco decide what to do with this fun:
      // add(TipperFactory.patternTipper("for($N1 $N2 : $X) $N3($N2);",
      // "on($X).apply(¢ -> $N3(¢));", ""));
      // add(TipperFactory.patternTipper("for ($N0<? extends $N1,? extends $N2>
      // $N3 : $N4) $N5($N3);", "foreach();", "foreach"));
      // add(TipperFactory.patternTipper("for ($N0<$N1,$N2> $N3 : $N4()) {
      // $N5.$N6($N3.$N7()); $N5.$N6($N3.$N8());}", "foreach();", "foreach"));
      // add(TipperFactory.patternTipper("for (int $N0 : $N1) $N2($N0,$N3);",
      // "foreach();", "foreach"));
      // add(TipperFactory.patternTipper("for ($N0.$N1<$N2<$N3>,$N4> $N5 :
      // $N6.$N7().$N8()) $N9($N5.$N10(),$N5.$N11());", "foreach();",
      // "foreach"));
      // add(TipperFactory.patternTipper("for ($N0<$N1> $N2 : $N3.$N4())
      // $N5.$N6($N2.$N7(),$N2.$N8());", "foreach();", "foreach"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "ForEach pattern: conevrt to fluent API";
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }
}
