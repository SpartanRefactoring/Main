package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ApplyToEach extends NanoPatternTipper<EnhancedForStatement> {
  Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.tipper("for($N1 $N2 : $X) $N2.$N3($A);", "on($X).apply(¢ -> ¢.$N3($A));", ""));
      add(TipperFactory.tipper("for($N1 $N2 : $X) $N3($N2);", "on($X).apply(¢ -> $N3(¢));", ""));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement s) {
    for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
      if (¢.canTip(s))
        return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "ApplyToEach pattern: conevrt to fluent API";
  }

  @Override public Tip tip(final EnhancedForStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final UserDefinedTipper<EnhancedForStatement> ¢ : tippers)
          if (¢.canTip(s)) {
            ¢.tip(s).go(r, g);
            idiomatic.addImport(az.compilationUnit(searchAncestors.forClass(CompilationUnit.class).from(s)), r);
            Logger.logNP(s, getClass() + "");
            return;
          }
        assert false;
      }
    };
  }
}
