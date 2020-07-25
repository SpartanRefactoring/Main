package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

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
    final IfStatement $ = copy.of(¢);
    $.setElseStatement(null);
    return !iz.blockRequiredInReplacement(¢, $) ? $ : subject.statement($).toBlock();
  }
}
