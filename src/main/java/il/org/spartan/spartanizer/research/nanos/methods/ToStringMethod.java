package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class ToStringMethod extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return "String".equals(returnType(¢) + "")//
        && "toString".equals(identifier(name(¢)));
  }
}
