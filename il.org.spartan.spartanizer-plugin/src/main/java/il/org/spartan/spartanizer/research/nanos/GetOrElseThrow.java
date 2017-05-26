package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** {@code
 *  if(X)
 *    throw Y
 *  return Z;
 * }
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-29 */
public class GetOrElseThrow extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x20E2A72F139D8B00L;
  private static final String description = "replace with azzert.NonNull(X)";
  private static final ThrowOnNull assertNonNull = new ThrowOnNull();

  @Override public boolean canTip(final IfStatement ¢) {
    return assertNonNull.check(¢)//
        && iz.returnStatement(next(¢))//
    ;
  }
  static Statement next(final IfStatement ¢) {
    return extract.nextStatement(¢);
  }
  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Statement next = next(¢);
        r.remove(next, g);
        r.replace(¢, extract.singleStatement(make.ast("NonNull(" + separate.these(nullCheckees(¢)).by(",") + ").get(" + returnee(next) + ");")), g);
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
    return "IfXIsNullThrowElseReturnY";
  }
  @Override public String example() {
    return "if(X == null) throw new RuntimeException(); return Y;";
  }
  @Override public String symbolycReplacement() {
    return "NonNull(X).get(Y);";
  }
}
