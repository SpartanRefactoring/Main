package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.utils.Proposition.*;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil 
 * @since 2017-03-25 */
public abstract class AbstractPattern<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 1;
  public AbstractPattern() {
    this.prerequisite = Proposition.that("Extract parent and next statement", () -> {
      parent = current.getParent();
      nextStatement = extract.nextStatement(current);
      return true;
    });
  }
  @Override public final String description(@SuppressWarnings("unused") N __) {
    return description(); 
  }
  @Override public abstract Examples examples();

  @Override public final boolean prerequisite(final N ¢) {
    assert current() == ¢;
    return prerequisite.eval();
  } 

  public final void setCurrent(final N c) {
    current = c;
  }
  @Override public final Tip tip(final N n) {
    assert n != null;
    assert n == current();
    return new Tip(description(), myClass(), current()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        AbstractPattern.this.go(r, g);
      }
    }.extend(range());
  }

  protected AbstractPattern<N> andAlso(final Proposition ¢) {
    this.prerequisite = prerequisite.and(¢);
    return this;
  }

  protected AbstractPattern<N> andAlso(final String description, final BooleanSupplier s) {
    return andAlso(prerequisite.and(Proposition.that(description, s)));
  }

  protected AbstractPattern<N> butNot(final Proposition ¢) {
    this.prerequisite = prerequisite.and(not(¢));
    return this;
  }

  protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  protected AbstractPattern<N> orElse(final Proposition ¢) {
    this.prerequisite = prerequisite.or(¢);
    return this;
  }

  protected ASTNode range() {
    return current;
  }

  private Proposition prerequisite;

  @Property protected Statement nextStatement;

  @Property protected ASTNode parent;
}
