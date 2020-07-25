package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.returnType;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** HashCode method
 * @author Ori Marcovitch
 * @since 2016 */
public class HashCodeMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0xED32AF357BA0D4L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return "int".equals(returnType(¢) + "") && "hashCode".equals(identifier(name(¢)));
  }
}
