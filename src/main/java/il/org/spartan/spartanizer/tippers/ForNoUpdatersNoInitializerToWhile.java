package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** See {@link #examples()} for documentation
 * @since 2017-03-24 */
public class ForNoUpdatersNoInitializerToWhile extends ReplaceCurrentNode<ForStatement>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -8750113331278505320L;
  Expression condition;

  @Override public String description() {
    final String $ = condition == null ? "C" : trivia.gist(condition);
    return String.format("Convert 'for(;%s;)' to 'while(%s)'", $, $);
  }

  @Override public Example[] examples() {
    return new Example[] { //
        Example.convert("for(;c;)f();").to("while(c)f();"), //
        Example.ignores("for(g();c;)f();"), //
        Example.ignores("for(;c;g())f();"), //
    };
  }

  @Override public String description(final ForStatement ¢) {
    condition = ¢.getExpression();
    return description();
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    condition = ¢.getExpression();
    return ¢.initializers().isEmpty() && ¢.updaters().isEmpty() && (condition = ¢.getExpression()) != null;
  }

  @Override public WhileStatement replacement(final ForStatement ¢) {
    final WhileStatement $ = ¢.getAST().newWhileStatement();
    $.setExpression(copy.of(¢.getExpression()));
    $.setBody(copy.of(¢.getBody()));
    return $;
  }
}
