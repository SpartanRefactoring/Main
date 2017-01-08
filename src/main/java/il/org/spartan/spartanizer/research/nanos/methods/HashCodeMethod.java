package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class HashCodeMethod extends JavadocMarkerNanoPattern {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return "int".equals(returnType(¢) + "") && "hashCode".equals(identifier(name(¢)));
  }
}
