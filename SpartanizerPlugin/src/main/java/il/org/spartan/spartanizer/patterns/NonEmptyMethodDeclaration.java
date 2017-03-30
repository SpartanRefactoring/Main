package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/**
 * A nonempty method declaration
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-30
 */
public abstract class NonEmptyMethodDeclaration extends AbstractPattern<MethodDeclaration>{

  private static final long serialVersionUID = 5190682195211446352L;
  protected Javadoc jdoc;
  protected SimpleName name;
  
  public NonEmptyMethodDeclaration() {
    andAlso(new Proposition.Singleton("Applicable only on non empty methods", () -> {
      if (!haz.anyStatements(current()))
        return false;
      name = current().getName();
      jdoc = current().getJavadoc();
      return true;
    }));
  }

  protected Javadoc jdoc() {
    return jdoc;
  }

  protected SimpleName name() {
    return name;
  }
}
