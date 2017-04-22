package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.lazy.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.utils.*;
import nano.ly.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-22 */
public final class ReturnDeadAssignment extends ReturnValuePattern {
  private static final long serialVersionUID = 2597846433179905741L;
  private Assignment assignment;
  private SimpleName to;

  public ReturnDeadAssignment() {
    super//
    .andAlso("Returned value must be an assignment", //
        () -> assignment = az.assignment(value) //
    ).andAlso("Assigment is to a variable", //
        () -> to = az.simpleName(to(assignment)) //
    ).andAlso("Variable is a local variable", //
        () -> Environment.of(methodDeclaration).nest.doesntHave(to + "")//
    );
  }

  @Override public Examples examples() {
    return null;
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    return null;
  }
}
