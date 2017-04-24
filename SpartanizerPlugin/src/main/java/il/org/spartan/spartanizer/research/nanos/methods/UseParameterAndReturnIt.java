package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import nano.ly.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class UseParameterAndReturnIt extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x5CA73E4455E35806L;

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneParameter(¢)//
        && notEmpty(¢)//
        && !iz.constructor(¢)//
        && !iz.voidType(returnType(¢))
        && returnStatements(¢).stream().map(λ -> expression(λ) + "").allMatch(λ -> λ.equals(identifier(name(the.onlyOneOf(parameters(¢))))));
  }
}
