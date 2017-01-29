package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** <pre>
 *  if(X) <br>
 *    throw Y
 *  return Z;
 * </pre>
 * 
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-29 */
public class GetOrElseThrow extends NanoPatternTipper<IfStatement> {
  private static final String description = "replace with azzert.notNull(X)";
  private static final AssertNotNull assertNotNull = new AssertNotNull();

  @Override public boolean canTip(final IfStatement ¢) {
    return assertNotNull.canTip(¢)//
        && returns(extract.nextStatement(¢))//
    ;
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(ast("get().notNull(" + separate.these(nullCheckees(¢)).by(",") + ");")), g);
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
