package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class UseParameterAndReturnIt extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return parameters(¢) != null && parameters(¢).size() == 1 && body(¢) != null && !iz.constructor(¢)
        && searchDescendants.forClass(ReturnStatement.class).from(¢).stream().map(r -> expression(r) + "")
            .allMatch(e -> e.equals(identifier(name(onlyOne(parameters(¢))))));
  }
}
