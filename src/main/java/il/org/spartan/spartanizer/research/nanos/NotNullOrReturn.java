package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** @nano if(X = null) return; <br>
 *       if(X = null) return null;
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public class NotNullOrReturn extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 3915101342508232691L;
  private static final String description = "replace with azzert.notNull(X)";
  private static final lazy<PreconditionNotNull> rival = lazy.get(PreconditionNotNull::new);

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && returnsDefault(then(¢)) //
        && rival.get().cantTip(¢)//
    ;
  }

  @Override @NotNull public Tip pattern(@NotNull final IfStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(ast("azzert.notNull(" + separate.these(nullCheckees(¢)).by(",") + ");")), g);
      }
    };
  }

  @Override @NotNull public String description() {
    return description;
  }

  @Override @NotNull public String nanoName() {
    return "NotNullAssumed";
  }
}
