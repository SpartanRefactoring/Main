package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/**
 * A single parameter method declaration pattern 
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-29
 */
public abstract class SingleParameterMethodDeclaration extends NonEmptyMethodDeclaration {

  private static final long serialVersionUID = 9180181890030651528L;
  protected SingleVariableDeclaration parameter;

  public SingleParameterMethodDeclaration() {
    andAlso(Proposition.of("Must have only one parameter", () -> {
      parameter = onlyOne(parameters(current()));
      return JohnDoe.property(parameter);
    }));
  }

  protected Type type() {
    return parameter.getType();
  }

  protected final SimpleName ParameterName() {
    return parameter.getName();
  }


  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  
}
