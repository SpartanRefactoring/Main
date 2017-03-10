package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano if(X = null) return; <br>
 *       if(X = null) return null;
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public class NotNullOrReturn extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 3915101342508232691L;
  private static final String description = "replace with aszert.notNull(X)";
  private static final PreconditionNotNull rival = new PreconditionNotNull();

  @Override public boolean interesting(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && returnsDefault(then(¢)) //
        && rival.cantTip(¢)//
    ;
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(ast("aszert.notNull(" + separate.these(nullCheckees(¢)).by(",") + ");")), g);
      }
    };
  }

  @Override public Category category() {
    return Category.Safety;
  }

  @Override public String description() {
    return description;
  }

  @Override public String technicalName() {
    return "IfXIsNullReturn";
  }

  @Override public String example() {
    return "if(X == null) return;";
  }

  @Override public String symbolycReplacement() {
    return "azzert.notNull(X);";
  }
}
