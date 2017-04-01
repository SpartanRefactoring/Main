package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.nominal.*;

/** A single parameter method declaration pattern
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-29 */
public abstract class SingleParameterMethodDeclaration extends NonEmptyMethodDeclaration {
  private static final long serialVersionUID = 0x7F668ECA50C04888L;
  protected SingleVariableDeclaration parameter;

  public SingleParameterMethodDeclaration() {
    andAlso("Must have only one parameter", () -> {
      parameter = onlyOne(parameters(current()));
      return JohnDoe.property(parameter);
    });
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
