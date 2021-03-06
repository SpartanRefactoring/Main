package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** See {@link #examples()} for documentation
 * @since 2017-03-24 */
public class ForNoUpdatersNoInitializerToWhile extends ReplaceCurrentNode<ForStatement>//
    implements Category.Loops {
  private static final long serialVersionUID = -0x796EA5B6F743E568L;
  Expression condition;

  @Override public String description() {
    final String $ = condition == null ? "C" : Trivia.gist(condition);
    return String.format("Convert 'for(;%s;)' to 'while(%s)'", $, $);
  }
  @Override public Examples examples() {
    return convert("for(;c;)f();").to("while(c)f();") //
        .ignores("for(g();c;)f();") //
        .ignores("for(;c;g())f();") //
    ;
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
