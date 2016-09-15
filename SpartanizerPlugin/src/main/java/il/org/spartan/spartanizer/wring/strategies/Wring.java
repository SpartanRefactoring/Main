package il.org.spartan.spartanizer.wring.strategies;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;

/** A wring is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A wring is so small that it is
 * idempotent: Applying a wring to the output of itself is the empty operation.
 * @param <N> type of node which triggers the transformation.
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09 */
public abstract class Wring<N extends ASTNode> implements Kind {
  public String name() {
    return getClass().getSimpleName();
  }

  /** Determines whether this {@link Wring} object is not applicable for a given
   * {@link PrefixExpression} is within the "scope" of this . Note that a
   * {@link Wring} is applicable in principle to an object, but that actual
   * application will be vacuous.
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is noneligible for
   *         the simplification offered by this object.
   * @see #canWring(InfixExpression) */
  public final boolean cantWring(final N ¢) {
    return !canWring(¢);
  }

  /** Determine whether the parameter is "eligible" for application of this
   * instance. The parameter must be within the scope of the current instance.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  public boolean canWring(@SuppressWarnings("unused") final N __) {
    return true;
  }

  /** Determines whether this {@link Wring} object is applicable for a given
   * {@link InfixExpression} is within the "scope" of this . Note that it could
   * be the case that a {@link Wring} is applicable in principle to an object,
   * but that actual application will be vacuous. If a wring claims a node, it
   * may be the case that the node would not be seen at all by other wrings
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is within the
   *         scope of this object @ */
  @Deprecated public boolean claims(final N ¢) {
    return wring(¢, null) != null;
  }

  protected abstract String description(N n);

  public Rewrite wring(final N ¢) {
    return wring(¢, null);
  }

  public Rewrite wring(final N n, final ExclusionManager m) {
    return m != null && m.isExcluded(n) ? null : wring(n);
  }
}
