package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.utils.Proposition;

/** A pattern for SwitchStatement
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31 */
public abstract class Switch extends NodeMatcher<SwitchStatement> {
  private static final long serialVersionUID = 0x7D070AD8D484B480L;
  protected List<Statement> statements;
  protected Expression expression;
  protected List<SwitchCase> cases;

  public Switch() {
    andAlso(Proposition.that("Must be switch statement", () -> {
      statements = step.statements(current);
      expression = step.expression(current);
      cases = statements.stream().filter(iz::switchCase).map(az::switchCase).collect(Collectors.toList());
      return true;
    }));
  }
  public List<SwitchCase> cases() {
    return statements.stream().filter(iz::switchCase).map(az::switchCase).collect(toList());
  }
}
