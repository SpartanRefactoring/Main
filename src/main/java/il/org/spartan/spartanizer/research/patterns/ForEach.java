package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for($N1 $N2 : $X1) if($X2) $N3.$N4($A);", "$X1.stream().filter($N2 -> $X2).forEach($N2 -> $N3.$N4($A));",
          "ForEachThat pattern: conevrt to fluent API"));
      add(TipperFactory.patternTipper("for($N1 $a : $X) $N2.$N3($A);", "on($X).apply(¢ -> ¢.$N3($A));", "ForEach pattern: conevrt to fluent API"));
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

  @Override public boolean canTip(final EnhancedForStatement s) {
    for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
      if (¢.canTip(s))
        return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "ForEach pattern: conevrt to fluent API";
  }

  @Override public Tip tip(final EnhancedForStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
          if (¢.canTip(s)) {
            ¢.tip(s).go(r, g);
            // idiomatic.addImport(az.compilationUnit(searchAncestors.forClass(CompilationUnit.class).from(s)),
            // r);
            Logger.logNP(s, getClass() + "");
            return;
          }
        assert false;
      }
    };
  }
}
