package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil {@code  Yossi.Gil@GMail.COM}
 * @since 2015-08-01 */
public abstract class IfAbstractPattern extends AbstractPattern<IfStatement> {
  private static final long serialVersionUID = 1L;
  @Property protected Statement then, elze;
  @Property protected Expression condition;
  @Property protected List<Statement> subsequentStatements;

  public IfAbstractPattern() {
    andAlso("Must be an if statement", () -> {
      condition = current.getExpression();
      then = current.getThenStatement();
      elze = current.getElseStatement();
      subsequentStatements = hop.subsequentStatements(current);
      return true;
    });
  }

  @Override public abstract Examples examples();

  static boolean degenerateElse(final IfStatement ¢) {
    return elze(¢) != null && iz.vacuousElse(¢);
  }

  @Override public abstract String description(IfStatement ¢);
}
