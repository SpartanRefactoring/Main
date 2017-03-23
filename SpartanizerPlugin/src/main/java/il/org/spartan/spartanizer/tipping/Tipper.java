package il.org.spartan.spartanizer.tipping;

import static java.lang.reflect.Modifier.*;

import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** A tipper is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A tipper is so small that it is
 * idempotent: Applying a tipper to the output of itself is the empty operation.
 * @param <N> type of node which triggers the transformation.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09 */
public abstract class Tipper<N extends ASTNode> extends Rule.Stateful<N, Tip> //
    implements TipperCategory, Serializable {
  private static final long serialVersionUID = -2252675511987504571L;

  @NotNull @SuppressWarnings("unchecked") public final Class<Tipper<N>> myClass() {
    return (Class<Tipper<N>>) getClass();
  }

  private Class<N> myOperandsClass;

  @Override @NotNull public String[] akas() {
    return new String[] { nanoName() };
  }

  public abstract boolean canTip(N n);

  /** Determines whether this instance can make a {@link Tip} for the parameter
   * instance.
   * @param e JD
   * @return whether the argument is noneligible for the simplification offered
   *         by this object.
   * @see #check(InfixExpression) */
  public final boolean cantTip(final N ¢) {
    return !check(¢);
  }

  @NotNull @SuppressWarnings("unchecked") private Class<N> castClass(@NotNull final Class<?> c2) {
    return (Class<N>) c2;
  }

  @Override @Nullable public String description() {
    return super.description();
  }

  @Nullable public abstract String description(N n);

  @Override public boolean equals(@NotNull final Object ¢) {
    return getClass().equals(¢.getClass());
  }

  @Override @NotNull public Example[] examples() {
    return new Example[] {};
  }

  @Override @Nullable public final Tip fire() {
    return tip(object());
  }

  @NotNull private Class<N> initializeMyOperandsClass() {
    @Nullable Class<N> $ = null;
    for (@NotNull final Method ¢ : getClass().getMethods())
      if (¢.getParameterCount() == 1 && !Modifier.isStatic(¢.getModifiers()) && isDefinedHere(¢))
        $ = lowest($, ¢.getParameterTypes()[0]);
    return $ != null ? $ : castClass(ASTNode.class);
  }

  private boolean isDefinedHere(@NotNull final Method ¢) {
    return ¢.getDeclaringClass() == getClass();
  }

  @NotNull private Class<N> lowest(@Nullable final Class<N> c1, @Nullable final Class<?> c2) {
    return c2 == null || !ASTNode.class.isAssignableFrom(c2) || c1 != null && !c1.isAssignableFrom(c2) ? c1 : castClass(c2);
  }

  /** Heuristics to find the class of operands on which this class works.
   * @return a guess for the type of the node. */
  @NotNull public final Class<N> myAbstractOperandsClass() {
    return myOperandsClass != null ? myOperandsClass : (myOperandsClass = initializeMyOperandsClass());
  }

  @Nullable public Class<N> myActualOperandsClass() {
    @NotNull final Class<N> $ = myAbstractOperandsClass();
    return !isAbstract($.getModifiers()) ? $ : null;
  }

  @NotNull public String nanoName() {
    return getClass().getSimpleName();
  }

  @Override public final boolean ok(final N ¢) {
    return canTip(¢);
  }

  /** A wrapper function without ExclusionManager.
   * @param ¢ The ASTNode object on which we deduce the tip.
   * @return a tip given for the ASTNode ¢. */
  @Nullable public Tip tip(final N ¢) {
    return tip(¢, null);
  }

  /** @param n an ASTNode
   * @param m exclusion manager guarantees this tip to be given only once.
   * @return a tip given for the ASTNode ¢. */
  @Nullable public Tip tip(final N n, @Nullable final ExclusionManager m) {
    return m != null && m.isExcluded(n) ? null : tip(n);
  }

  @NotNull public String className() {
    return system.className(this);
  }
}
