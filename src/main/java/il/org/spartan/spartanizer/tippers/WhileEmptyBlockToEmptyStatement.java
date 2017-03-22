package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.Example;

import static il.org.spartan.utils.Example.*;

/** replaces a while statement followed by an while block with a for statement
 * followed by a semicolon
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-22 */
public class WhileEmptyBlockToEmptyStatement extends ReplaceCurrentNode<WhileStatement> //
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 2013380508545213297L;

  @Override public ASTNode replacement(final WhileStatement ¢) {
    WhileStatement $ = copy.of(¢);
    $.setBody($.getAST().newEmptyStatement());
    return $;
  }

  @Override protected boolean prerequisite(final WhileStatement ¢) {
    final Statement $ = ¢.getBody();
    return iz.block($) && iz.emptyBlock(az.block($));
  }

  @Override public Example[] examples() {
    return new Example[] {
        convert("while(x();y();z()){}")//
            .to("while(x();y();z());"), //
        ignores("while(x();y();z()){f();g();}"), //
        ignores("while(x();y();z());")//
    };
  }

  @Override public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "replaces a for statment followed by an empty block with a for statment followed by a semicolon";
  }
}
