package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** {@link ClassInstanceCreation} pattern.
 * @author Ori Roth
 * @since 2017-04-20 */
public abstract class ClassInstanceCreationPattern extends NodePattern<ClassInstanceCreation> {
  private static final long serialVersionUID = 0x319433183A0507AEL;
  @Property protected Expression expression;
  @Property protected List<Expression> arguments;
  @Property protected String name;

  protected ClassInstanceCreationPattern() {
    andAlso("Must be a Class Instance Creation", () -> {
      expression = current.getExpression();
      arguments = arguments(current);
      name = current.getType() + "";
      return name != null;
    });
  }
}
