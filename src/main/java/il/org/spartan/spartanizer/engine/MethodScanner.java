package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** A utility class used to scan statements of a {@link MethodDeclaration}.
 * @author Ori Roth
 * @since 2016 */
public abstract class MethodScanner {
  protected final MethodDeclaration method;
  protected final List<Statement> statements;
  protected Statement currentStatement;
  protected int currentIndex;

  @SuppressWarnings("unchecked") public MethodScanner(final MethodDeclaration method) {
    assert method != null;
    this.method = method;
    if (method.getBody() == null) {
      statements = null;
      currentStatement = null;
    } else {
      statements = method.getBody().statements();
      currentStatement = first(statements);
    }
    currentIndex = -1;
  }
  /** @return List of available statements from the method to be scanned. */
  protected abstract List<Statement> availableStatements();
  /** @return List of available statements. Updates the current statement and
   *         the current index while looping. */
  public Iterable<Statement> statements() {
    return () -> new Iterator<Statement>() {
      final Iterator<Statement> i = availableStatements().iterator();

      @Override public boolean hasNext() {
        return i.hasNext();
      }
      @Override public Statement next() {
        final Statement $ = i.next();
        ++currentIndex;
        return currentStatement = $;
      }
    };
  }
}