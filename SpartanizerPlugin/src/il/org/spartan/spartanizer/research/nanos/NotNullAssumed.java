package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano if(X = null) return; <br>
 *       if(X = null) return null;
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public class NotNullAssumed extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x36553BED8BDBAFF3L;
  private static final String description = "replace with azzert.notNull(X)";
  private static final lazy<NotNullRequired> rival = lazy.get(NotNullRequired::new);

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && returnsDefault(then(¢)) //
        && rival.get().cantTip(¢)//
    ;
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(make.ast("azzert.notNull(" + separate.these(nullCheckees(¢)).by(",") + ");")), g);
      }
    };
  }

  @Override public String description() {
    return description;
  }

  @Override public String tipperName() {
    return "NotNullAssumed";
  }
}
