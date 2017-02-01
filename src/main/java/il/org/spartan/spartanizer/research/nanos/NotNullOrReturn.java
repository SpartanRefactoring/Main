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
import org.jetbrains.annotations.NotNull;

/** @nano if(X = null) return; <br>
 *       if(X = null) return null;
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public class NotNullOrReturn extends NanoPatternTipper<IfStatement> {
  private static final String description = "replace with azzert.notNull(X)";
  private static final PreconditionNotNull rival = new PreconditionNotNull();

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && returnsDefault(then(¢)) //
        && rival.cantTip(¢)//
    ;
  }

  @NotNull
  @Override public Tip pattern(@NotNull final IfStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(ast("azzert.notNull(" + separate.these(nullCheckees(¢)).by(",") + ");")), g);
      }
    };
  }

  @NotNull
  @Override public Category category() {
    return Category.Safety;
  }

  @NotNull
  @Override public String description() {
    return description;
  }

  @NotNull
  @Override public String technicalName() {
    return "IfXIsNullReturn";
  }

  @NotNull
  @Override public String example() {
    return "if(X == null) return;";
  }

  @NotNull
  @Override public String symbolycReplacement() {
    return "azzert.notNull(X);";
  }
}
