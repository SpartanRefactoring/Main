package il.org.spartan.spartanizer.research.patterns.methods;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class UpCaster extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢) && hazOneStatement(¢) && notConstructor(¢) && notVoid(¢) && !returnTypeSameAsParameter(¢)
        && iz.name(expression(az.returnStatement(onlyOne(statements(¢)))));
  }
}
