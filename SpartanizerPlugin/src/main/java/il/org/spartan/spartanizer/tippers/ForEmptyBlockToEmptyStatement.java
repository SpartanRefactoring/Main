package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** replaces a for statement followed by an empty block with a for statement
 * followed by a semicolon
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-22 */
public class ForEmptyBlockToEmptyStatement extends ReplaceCurrentNode<ForStatement> //
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 2013380508545213297L;

  @Override public ASTNode replacement(final ForStatement ¢) {
    final ForStatement $ = copy.of(¢);
    $.setBody($.getAST().newEmptyStatement());
    return $;
  }

  @Override protected boolean prerequisite(@NotNull final ForStatement ¢) {
    final Statement $ = ¢.getBody();
    return iz.block($) && iz.emptyBlock(az.block($));
  }

  @Override @NotNull public Example[] examples() {
    return new Example[] {
        convert("for(x();y();z()){}")//
            .to("for(x();y();z());"), //
        ignores("for(x();y();z()){f();g();}"), //
        ignores("for(x();y();z());")//
    };
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "replaces a for statment followed by an empty block with a for statment followed by a semicolon";
  }
}
