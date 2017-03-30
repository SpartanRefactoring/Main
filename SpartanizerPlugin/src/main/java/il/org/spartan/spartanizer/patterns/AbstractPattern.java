package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.utils.Proposition.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-25 */
public abstract class AbstractPattern<N extends ASTNode> extends CarefulTipper<N> {
  private static final long serialVersionUID = 0x79313A9ADD5632BCL;
  private Proposition prerequisite;

  public AbstractPattern() {
    this.prerequisite = Proposition.T;
  }

  @Override public final boolean prerequisite(final N ¢) {
    assert object() == ¢;
    return prerequisite.eval();
  }

  @Override public final Tip tip(final N n) {
    assert n != null;
    assert n == object();
    return new Tip(description(), object(), myClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        AbstractPattern.this.go(r, g);
      }
    };
  }

  protected AbstractPattern<N> butNot(final Proposition ¢) {
    this.prerequisite = prerequisite.and(not(¢));
    return this;
  }

  protected AbstractPattern<N> andAlso(final Proposition ¢) {
    this.prerequisite = prerequisite.and(¢);
    return this;
  }

  protected abstract ASTRewrite go(ASTRewrite r, TextEditGroup g);

  protected AbstractPattern<N> orElse(final Proposition ¢) {
    this.prerequisite = prerequisite.or(¢);
    return this;
  }
}
