package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.*;

/** A nonempty method declaration
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-30 */
public abstract class NonEmptyMethodDeclaration extends NodePattern<MethodDeclaration> {
  private static final long serialVersionUID = 0x480901F48385E850L;
  protected Javadoc jdoc;
  protected SimpleName name;

  public NonEmptyMethodDeclaration() {
    andAlso("Applicable only on non empty methods", () -> {
      if (!haz.anyStatements(current()))
        return false;
      name = current().getName();
      jdoc = current().getJavadoc();
      return true;
    });
  }

  protected Javadoc jdoc() {
    return jdoc;
  }

  protected SimpleName name() {
    return name;
  }
}
