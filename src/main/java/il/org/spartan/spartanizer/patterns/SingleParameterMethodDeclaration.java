package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import nano.ly.*;

/** A single parameter method declaration pattern
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-29 */
public abstract class SingleParameterMethodDeclaration extends NonEmptyMethodDeclaration {
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  private static final long serialVersionUID = 0x7F668ECA50C04888L;

  public SingleParameterMethodDeclaration() {
    andAlso("Must have only one parameter", () -> {
      parameter = the.onlyOneOf(parameters(current()));
      return JohnDoe.property(parameter);
    });
  }

  protected final SimpleName parameterName() {
    return parameter.getName();
  }

  @Override protected ASTNode highlight() {
    return parameterName();
  }

  protected Type type() {
    return parameter.getType();
  }

  protected SingleVariableDeclaration parameter;
}
