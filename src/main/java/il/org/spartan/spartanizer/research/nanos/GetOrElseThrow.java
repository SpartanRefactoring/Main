package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** {@code
 *  if(X)
 *    throw Y
 *  return Z;
 * }
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-29 */
public class GetOrElseThrow extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 2369640174584695552L;
  private static final String description = "replace with azzert.notNull(X)";
  private static final ThrowOnNull assertNotNull = new ThrowOnNull();

  @Override public boolean canTip(final IfStatement ¢) {
    return assertNotNull.check(¢)//
        && iz.returnStatement(next(¢))//
    ;
  }

  @Nullable static Statement next(final IfStatement ¢) {
    return extract.nextStatement(¢);
  }

  @Nullable @Override public Fragment pattern(@NotNull final IfStatement ¢) {
    return new Fragment(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        @Nullable final Statement next = next(¢);
        r.remove(next, g);
        r.replace(¢, extract.singleStatement(ast("notNull(" + separate.these(nullCheckees(¢)).by(",") + ").get(" + returnee(next) + ");")), g);
      }
    };
  }

  @NotNull @Override public Category category() {
    return Category.Safety;
  }

  @NotNull @Override public String description() {
    return description;
  }

  @NotNull @Override public String technicalName() {
    return "IfXIsNullThrowElseReturnY";
  }

  @NotNull @Override public String example() {
    return "if(X == null) throw new RuntimeException(); return Y;";
  }

  @NotNull @Override public String symbolycReplacement() {
    return "notNull(X).get(Y);";
  }
}
