package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** replaces a for statement followed by an empty block with a for statement
 * followed by a semicolon
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class ForEmptyBlockToEmptyStatement extends ReplaceCurrentNode<ForStatement> //
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 4315491163164760502L;

  @Override public ASTNode replacement(final ForStatement ¢) {
    final ForStatement $ = copy.of(¢);
    $.setBody($.getAST().newEmptyStatement());
    return $;
  }

  @Override protected boolean prerequisite(final ForStatement ¢) {
    final Statement $ = ¢.getBody();
    return iz.block($) && iz.emptyBlock(az.block($));
  }

  @Override public Examples examples() {
    return convert("for(x();y();z()){}")//
        .to("for(x();y();z());") //
        .ignores("for(x();y();z()){f();g();}") //
        .ignores("for(x();y();z());")//
    ;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Replace 'for(?;?;?){}' with 'for(?;?;?);'";
  }
}
