package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** A pattern for SwitchStatement
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31 */
public abstract class SwitchStatementAbstractPattern extends NodePattern<SwitchStatement> {
  private static final long serialVersionUID = 0x7D070AD8D484B480L;
  protected List<Statement> statements;
  protected Expression expression;
  protected List<SwitchCase> cases;

  public SwitchStatementAbstractPattern() {
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
