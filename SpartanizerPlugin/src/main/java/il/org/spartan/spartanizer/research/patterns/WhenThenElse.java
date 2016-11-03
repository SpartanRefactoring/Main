package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** There is no reason to use this tipper, it won't make code look better. But
 * this can be used for replacing pattern with something so it won't be found
 * again...
 * @author Ori Marcovitch
 * @year 2016 */
public final class WhenThenElse extends NanoPatternTipper<ConditionalExpression> {
  UserDefinedTipper<ConditionalExpression> tipper = TipperFactory.tipper("$X1 ? $X2 : $X3", "when($X1).then($X2).elze($X3)", "");

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return getClass().getSimpleName() + " pattern";
  }

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return tipper.canTip(¢);
  }

  @Override public Tip tip(final ConditionalExpression x) {
    Logger.logNP(x, getClass().getSimpleName());
    return new Tip(this.description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        tipper.tip(x).go(r, g);
      }
    };
  }
}
