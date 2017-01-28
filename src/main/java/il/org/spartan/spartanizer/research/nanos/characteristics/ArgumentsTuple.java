package il.org.spartan.spartanizer.research.nanos.characteristics;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public class ArgumentsTuple extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (!hazAtLeastTwoParameters(d))
      return false;
    final String $ = stringify(d);
    ___.nothing();
    return yieldDescendants.untilClass(MethodInvocation.class).from(d).stream()//
        .map(ArgumentsTuple::stringify)//
        .allMatch(λ -> λ != null && λ.contains($));
  }

  private static String stringify(final MethodDeclaration ¢) {
    final String $ = parametersNames(¢) + "";
    return $.length() <= 2 ? null : "," + $.substring(1, $.length() - 1).replaceAll(" ", "") + ",";
  }

  private static String stringify(final MethodInvocation ¢) {
    final String $ = arguments(¢) + "";
    return $.length() <= 2 ? null : "," + $.substring(1, $.length() - 1).replaceAll(" ", "") + ",";
  }
}
