package il.org.spartan.spartanizer.patterns;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/**
 * TODO Yuval Simon: document class 
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31
 */
public abstract class SwitchZtatement extends AbstractPattern<SwitchStatement> {
  private static final long serialVersionUID = 9009181505972647040L;
  
  protected List<Statement> statements;
  protected Expression expression;
  
  public SwitchZtatement() {
    andAlso(Proposition.of("Must be switch statement", () -> {
      statements = step.statements(current);
      expression = step.expression(current);
      return true;
    }));
  }
  
  public List<SwitchCase> cases() {
    return statements.stream().filter(λ -> iz.switchCase(λ)).map(λ->az.switchCase(λ)).collect(Collectors.toList());
  }
}
