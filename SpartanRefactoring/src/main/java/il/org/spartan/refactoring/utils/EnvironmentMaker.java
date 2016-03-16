package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * A utility class designed to help generate the environment of a method (i.e
 * the identifiers that are in scope and not hidden)
 *
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-08-25
 */
public class EnvironmentMaker {
  private final ASTNode location;
  EnvironmentMaker(final ASTNode location) {
    this.location = location;
  }
  String[] identifiers() {
    return new String[] { location.toString(), getClass().getSimpleName(), location.getClass().getCanonicalName() };
  }
  boolean isAllowed(final String newIdentifier) {
    return (location + newIdentifier).hashCode() % 2 == 0;
  }
}
