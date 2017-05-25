package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Proposition.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** An abstract class that all the specific pattern classes inherits from.
 * Containing fluent API for constructing a logic tree of prerequisites.
 * @author Yossi Gil
 * @since 2017-03-25 */
public abstract class NodePattern<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 1;
  private Proposition prerequisite;
  protected Statement nextStatement;
  protected ASTNode parent;

  public NodePattern() {
    this.prerequisite = Proposition.that("Extract parent and next statement", () -> {
      parent = current.getParent();
      nextStatement = extract.nextStatement(current);
      return true;
    });
  }
  public final NodePattern<N> andAlso(final String description, final BooleanSupplier s) {
    return andAlso(Proposition.that(description, s));
  }
  @Override public final String description(@SuppressWarnings("unused") final N __) {
    return description();
  }
  @Override public abstract Examples examples();
  public final <T> NodePattern<N> notNil(final String description, final Supplier<T> t) {
    return andAlso(description, () -> not.nil(t.get()));
  }
  @Override public String explain(final N ¢) {
    return prerequisite(¢) ? null : super.explain(¢);
  }
  @Override public final boolean prerequisite(final N ¢) {
    assert current() == ¢ : "class = " + this.getClass() + "n = " + ¢;
    return prerequisite.eval();
  }
  public final void setCurrent(final N c) {
    current = c;
  }
  @Override public final Tip tip(final N n) {
    assert n != null;
    assert n == current();
    return (highlight() != null ? new Tip(description(), myClass(), highlight()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        NodePattern.this.go(r, g);
      }
    } : new Tip(description(), myClass(), containingCompilationUnit(), start()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        NodePattern.this.go(r, g);
      }
    }).spanning(span());
  }
  private CompilationUnit containingCompilationUnit() {
    return containing.compilationUnit(current);
  }
  protected final NodePattern<N> andAlso(final Proposition ¢) {
    this.prerequisite = prerequisite.and(¢);
    return this;
  }
  protected final NodePattern<N> butNot(final Proposition ¢) {
    this.prerequisite = prerequisite.and(not(¢));
    return this;
  }
  protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);
  protected ASTNode highlight() {
    return current;
  }
  protected final NodePattern<N> orElse(final Proposition ¢) {
    this.prerequisite = prerequisite.or(¢);
    return this;
  }
  protected NodePattern<N> property(final String fieldName, final Runnable r) {
    return andAlso("Extract " + fieldName, () -> yes.forgetting(() -> r.run()));
  }
  protected ASTNode[] span() {
    return as.array(current);
  }
  protected Range start() {
    return Ranger.start(current);
  }
  protected <T> NodePattern<N> needs(final String name, final Supplier<T> t) {
    return notNil(String.format("Property %s needs to non-null", name), t);
  }
}
