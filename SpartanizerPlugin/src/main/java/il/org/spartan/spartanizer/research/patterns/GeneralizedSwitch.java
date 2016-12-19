package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class GeneralizedSwitch extends NanoPatternTipper<IfStatement> {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Go Fluent: Generalized Switch";
  }

  @Override public boolean canTip(final IfStatement ¢) {
    return !¢.equals(then(az.ifStatement(parent(¢)))) && wizard.differsInSingleAtomic(branches(¢));
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, wizard.ast("holds.on(;"), g);
      }
    };
  }
}
