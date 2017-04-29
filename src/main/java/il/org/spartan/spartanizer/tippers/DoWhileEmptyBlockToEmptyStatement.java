package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** replaces a do statement followed by an empty block with a do statement
 * followed by a semicolon. in development, see issue #1125
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class DoWhileEmptyBlockToEmptyStatement extends ReplaceCurrentNode<DoStatement> //
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -2477420682938910320L;

  @Override public ASTNode replacement(final DoStatement ¢) {
    final DoStatement $ = copy.of(¢);
    $.setBody($.getAST().newEmptyStatement());
    return $;
  }

  @Override protected boolean prerequisite(final DoStatement ¢) {
    final Statement $ = ¢.getBody();
    return iz.block($) && iz.emptyBlock(az.block($));
  }

  @Override public Examples examples() {
    return convert("do{}while(x());y();z();")//
        .to("do;while(x());y();z();") //
        .ignores("do{f();g();}while(x());y();z();") //
        .ignores("do;while(x());y();z();")//
    ;
  }

  @Override public String description(@SuppressWarnings("unused") final DoStatement __) {
    return "Replace 'do {} while(?)' with 'do ; while(?)'";
  }
}
