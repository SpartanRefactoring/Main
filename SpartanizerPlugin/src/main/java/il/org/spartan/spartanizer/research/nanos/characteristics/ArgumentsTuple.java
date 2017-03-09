package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** sequence of parameters which occur few times together in a method
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public class ArgumentsTuple extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -6753982059151484641L;

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (!hazAtLeastTwoParameters(d))
      return false;
    final String $ = stringify(d);
    ___.nothing();
    return descendants.whoseClassIs(MethodInvocation.class).from(d).stream()//
        .map(ArgumentsTuple::stringify)//
        .allMatch(λ -> λ != null && λ.contains($));
  }

  private static String stringify(final MethodDeclaration ¢) {
    return "," + separate.these(parametersNames(¢)).by(",") + ",";
  }

  private static String stringify(final MethodInvocation ¢) {
    return "," + separate.these(arguments(¢)).by(",") + ",";
  }
}
