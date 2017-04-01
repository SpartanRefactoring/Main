package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** replaces a while statement followed by an while block with a for statement
 * followed by a semicolon
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class WhileEmptyBlockToEmptyStatement extends ReplaceCurrentNode<WhileStatement> //
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x1BF0F6E7B9886371L;

  @Override public ASTNode replacement(final WhileStatement ¢) {
    final WhileStatement $ = copy.of(¢);
    $.setBody($.getAST().newEmptyStatement());
    return $;
  }

  @Override protected boolean prerequisite(final WhileStatement ¢) {
    final Block $ = az.block(¢.getBody());
    return $ != null && iz.emptyBlock($);
  }

  @Override public Examples examples() {
    return  //
        convert("while(x()){}").to("while(x());") //
        .ignores("while(x()){y();z();}")//
    ;
  }

  @Override public String description(@SuppressWarnings("unused") final WhileStatement __) {
    return "replaces a for statment followed by an empty block with a for statment followed by a semicolon";
  }
}
