package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.utils.Proposition.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** An abstract class that all the specific pattern classes inherits from.
 * Containing fluent API for constructing a logic tree of prerequisites.
 * @author Yossi Gil
 * @since 2017-03-25 */
public abstract class AbstractPattern<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 1;
  private Proposition prerequisite;
  @Property protected Statement nextStatement;
  @Property protected ASTNode parent;

  public AbstractPattern() {
    this.prerequisite = Proposition.that("Extract parent and next statement", () -> {
      parent = current.getParent();
      nextStatement = extract.nextStatement(current);
      return true;
    });
  }

  protected final AbstractPattern<N> andAlso(final Proposition ¢) {
    this.prerequisite = prerequisite.and(¢);
    return this;
  }

  protected final AbstractPattern<N> andAlso(final String description, final BooleanSupplier s) {
    return andAlso(prerequisite.and(Proposition.that(description, s)));
  }

  protected final AbstractPattern<N> butNot(final Proposition ¢) {
    this.prerequisite = prerequisite.and(not(¢));
    return this;
  }

  private CompilationUnit containingCompilationUnit() {
    return containing.compilationUnit(current);
  }

  @Override public final String description(@SuppressWarnings("unused") final N __) {
    return description();
  }

  @Override public abstract Examples examples();

  protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  protected ASTNode highlight() {
    return current;
  }

  protected final AbstractPattern<N> orElse(final Proposition ¢) {
    this.prerequisite = prerequisite.or(¢);
    return this;
  }

  @Override public final boolean prerequisite(final N ¢) {
    assert current() == ¢ : "class = " + this.getClass() + "n = " + ¢;
    return prerequisite.eval();
  }

  public final void setCurrent(final N c) {
    current = c;
  }

  protected ASTNode[] span() {
    return as.array(current);
  }

  protected Range start() {
    return Ranger.start(current);
  }

  @Override public final Tip tip(final N n) {
    assert n != null;
    assert n == current();
    return (highlight() != null ? new Tip(description(), myClass(), highlight()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        AbstractPattern.this.go(r, g);
      }
    } : new Tip(description(), myClass(), containingCompilationUnit(), start()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        AbstractPattern.this.go(r, g);
      }
    }).spanning(span());
  }
}
