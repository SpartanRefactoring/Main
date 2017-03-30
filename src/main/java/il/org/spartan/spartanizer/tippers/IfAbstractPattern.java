package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-01 */
abstract public class IfAbstractPattern extends AbstractPattern<IfStatement> {
  private static final long serialVersionUID = 1L;
  @Property Statement then, elze;
  @Property Expression condition;
  public IfAbstractPattern() {
    andAlso(Proposition.of("Must be an if statement", //
        () -> {
          condition = current.getExpression();
          then = current.getThenStatement();
          elze = current.getElseStatement();
          return true;
        }));
  }

  @Override public abstract Example[] examples();

  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }

  @Override public abstract String description(final IfStatement ¢) ;
}
