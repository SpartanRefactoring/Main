package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** See {@link #examples()} for documentation
 * @since 2017-03-24 */
public class ForNoUpdatersNoInitializerToWhile extends ReplaceCurrentNode<ForStatement>//
    implements Category.Loops {
  private static final long serialVersionUID = -0x796EA5B6F743E568L;
  Expression condition;

  @Override public String description() {
    final String ret = condition == null ? "C" : Trivia.gist(condition);
    return String.format("Convert 'for(;%s;)' to 'while(%s)'", ret, ret);
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
    final WhileStatement ret = ¢.getAST().newWhileStatement();
    ret.setExpression(copy.of(¢.getExpression()));
    ret.setBody(copy.of(¢.getBody()));
    return ret;
  }
}
