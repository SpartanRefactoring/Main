package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** convert {@code if (x) return b; else {} } into {@code
 * if (x)
 *   return b;
 * }
 * @author Yossi Gil
 * @since 2015-08-01 */
public final class IfDegenerateElse extends ReplaceCurrentNode<IfStatement>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x6A05F681B3F27CB0L;

  @Override public Examples examples() {
    return //
    convert("if (x) return b; else {}") //
        .to("if (x) return b;")//
        .ignores("if (x) return b;") //
    ;
  }
  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }
  @Override public String description(final IfStatement ¢) {
    return "Remove vacuous 'else' branch of 'if(" + Trivia.gist(¢.getExpression() + "") + ")...'";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && then(¢) != null && degenerateElse(¢);
  }
  @Override public Statement replacement(final IfStatement ¢) {
    final IfStatement ret = copy.of(¢);
    ret.setElseStatement(null);
    return !iz.blockRequiredInReplacement(¢, ret) ? ret : subject.statement(ret).toBlock();
  }
}
