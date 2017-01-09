package il.org.spartan.spartanizer.research.nanos.characteristics;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
public class ArgumentsTuple extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (!hazAtLeastTwoParameters(d))
      return false;
    final String $ = stringify(d);
    ___.nothing();
    return searchDescendants.forClass(MethodInvocation.class).from(d).stream()//
        .map(¢ -> stringify(¢))//
        .allMatch(s -> s != null && s.contains($));
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