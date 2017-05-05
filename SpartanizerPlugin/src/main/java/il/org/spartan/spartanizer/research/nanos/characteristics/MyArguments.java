package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** sequence of parameters which occur few times together in a method
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
public class MyArguments extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x5DBAF6E60F15E6E1L;

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (!hazAtLeastTwoParameters(d))
      return false;
    final String $ = stringify(d);
    final List<MethodInvocation> invocations = descendants.whoseClassIs(MethodInvocation.class).from(d);
    return invocations.stream()//
        .map(MyArguments::stringify)
        .allMatch(λ -> λ != null//
            && λ.contains($))//
        && !invocations.isEmpty();
  }
  private static String stringify(final MethodDeclaration ¢) {
    return "," + separate.these(parametersNames(¢)).by(",") + ",";
  }
  private static String stringify(final MethodInvocation ¢) {
    return "," + separate.these(arguments(¢)).by(",") + ",";
  }
}
