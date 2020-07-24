package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.returnType;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class ToStringMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x358B27E24DE12730L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return "String".equals(returnType(¢) + "")//
        && "toString".equals(identifier(name(¢)));
  }
}
