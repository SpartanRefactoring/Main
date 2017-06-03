package il.org.spartan.spartanizer.tippers;

import il.org.spartan.spartanizer.java.*;

/** A nonempty method declaration
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-03-30 */
public abstract class NonEmptyMethodDeclaration extends CallablePattern {
  private static final long serialVersionUID = 0x480901F48385E850L;
  public NonEmptyMethodDeclaration() {
    andAlso("Must not be constructor ", () -> !current.isConstructor());
    andAlso("Applicable only on non empty methods", () -> haz.anyStatements(current()));
  }
}
