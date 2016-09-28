package il.org.spartan.spartanizer.tipping;

import static java.lang.reflect.Modifier.*;

import java.lang.reflect.*;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** A tipper is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A tipper is so small that it is
 * idempotent: Applying a tipper to the output of itself is the empty operation.
 * @param <N> type of node which triggers the transformation.
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09 */
public abstract class Tipper<N extends ASTNode> implements Category {
  private Class<N> myOperandsClass;

  /** Determine whether the parameter is "eligible" for application of this
   * instance.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  public abstract boolean canTip(final N n);

  /** Determines whether this instance can make a {@link Tip} for the parameter
   * instance.
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is noneligible for
   *         the simplification offered by this object.
   * @see #canTip(InfixExpression) */
  public final boolean cantTip(final N ¢) {
    return !canTip(¢);
  }

  @SuppressWarnings("unchecked") private Class<N> castClass(final Class<?> c2) {
    return (Class<N>) c2;
  }

  @Override public String description() {
    return getClass().getSimpleName();
  }

  public abstract String description(N n);

  private Class<N> initializeMyOperandsClass() {
    Class<N> $ = null;
    for (final Method ¢ : getClass().getMethods())
      if (¢.getParameterCount() == 1 && !Modifier.isStatic(¢.getModifiers()) && isDefinedHere(¢))
        $ = lowest($, ¢.getParameterTypes()[0]);
    return $ != null ? $ : castClass(ASTNode.class);
  }

  private boolean isDefinedHere(final Method ¢) {
    return ¢.getDeclaringClass() == getClass();
  }

  private Class<N> lowest(final Class<N> c1, final Class<?> c2) {
    return c2 == null || !ASTNode.class.isAssignableFrom(c2) || c1 != null && !c1.isAssignableFrom(c2) ? c1 : castClass(c2);
  }

  /** Heuristics to find the class of operands on which this class works.
   * @return a guess for the type of the node. */
  public final Class<N> myAbstractOperandsClass() {
    return myOperandsClass != null ? myOperandsClass : (myOperandsClass = initializeMyOperandsClass());
  }

  public Class<N> myActualOperandsClass() {
    final Class<N> $ = myAbstractOperandsClass();
    return !isAbstract($.getModifiers()) ? $ : null;
  }

  public String name() {
    return getClass().getSimpleName();
  }

  public Tip tip(final N ¢) throws TipperFailure {
    return tip(¢, null);
  }

  public Tip tip(final N n, final ExclusionManager m) throws TipperFailure {
    return m != null && m.isExcluded(n) ? null : tip(n);
  }
}
