package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** sequence of parameters which occur few times together in a method
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
public class MyArguments extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -6753982059151484641L;

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (!hazAtLeastTwoParameters(d))
      return false;
    @NotNull final String $ = stringify(d);
    @NotNull final List<MethodInvocation> invocations = descendants.whoseClassIs(MethodInvocation.class).from(d);
    return invocations.stream()//
        .map(MyArguments::stringify)
        .allMatch(λ -> λ != null//
            && λ.contains($))//
        && !invocations.isEmpty();
  }

  @NotNull private static String stringify(final MethodDeclaration ¢) {
    return "," + separate.these(parametersNames(¢)).by(",") + ",";
  }

  @NotNull private static String stringify(final MethodInvocation ¢) {
    return "," + separate.these(arguments(¢)).by(",") + ",";
  }
}
