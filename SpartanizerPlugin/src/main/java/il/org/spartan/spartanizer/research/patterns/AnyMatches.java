package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.idiomatics.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class AnyMatches extends NanoPatternTipper<EnhancedForStatement> {
  Set<UserDefinedTipper<EnhancedForStatement>> tippers = new HashSet<UserDefinedTipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("for ($N0<$N1> $N2 : $N3().$N4()) if ($N2.$N5($N6)) return true;", "if(anyMatches()) return true;;", "any"));
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "AnyMatches pattern: conevrt to fluent API";
  }

  @Override public Tip pattern(final EnhancedForStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        firstTip(tippers, s).go(r, g);
        idiomatic.addImport(az.compilationUnit(searchAncestors.forClass(CompilationUnit.class).from(s)), r);
      }
    };
  }
}
