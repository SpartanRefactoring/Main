package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** replaces a do statement followed by an empty block with a do statement
 * followed by a semicolon. in development, see issue #1125
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class DoWhileEmptyBlockToEmptyStatement extends ReplaceCurrentNode<DoStatement> //
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x226190FC35C78670L;

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
