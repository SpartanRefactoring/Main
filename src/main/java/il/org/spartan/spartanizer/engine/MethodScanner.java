package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A utility class used to scan statements of a {@link MethodDeclaration}.
 * @author Ori Roth
 * @since 2016 */
public abstract class MethodScanner {
  @NotNull protected final MethodDeclaration method;
  @Nullable protected final List<Statement> statements;
  @Nullable protected Statement currentStatement;
  protected int currentIndex;

  public MethodScanner(@NotNull final MethodDeclaration method) {
    assert method != null;
    this.method = method;
    if (body(method) == null) {
      statements = null;
      currentStatement = null;
    } else {
      statements = step.statements(body(method));
      currentStatement = first(statements);
    }
    currentIndex = -1;
  }

  /** @return List of available statements from the method to be scanned. */
  protected abstract List<Statement> availableStatements();

  /** @return List of available statements. Updates the current statement and
   *         the current index while looping. */
  @NotNull public Iterable<Statement> statements() {
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