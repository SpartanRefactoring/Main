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
  // TODO Yossi: decide whether to move this to {@link Example} --or
  public static Example.Ignores ignores(final String code) {
    return () -> code;
  }

  @Override public boolean equals(@NotNull final Object ¢) {
    return getClass().equals(¢.getClass());
  }

  private static final long serialVersionUID = -2252675511987504571L;

  @NotNull
  @Override public String[] akas() {
    return new String[] { nanoName() };
  }

  /** Determines whether this instance can make a {@link Tip} for the parameter
   * instance.
   * @param e JD
   * @return whether the argument is noneligible for the simplification offered
   *         by this object.
   * @see #check(InfixExpression) */
  public final boolean cantTip(final N ¢) {
    return !check(¢);
  }

  @Override public final boolean ok(@NotNull final N ¢) {
    return canTip(¢);
  }

  public abstract boolean canTip(N n);

  @NotNull
  public String nanoName() {
    return getClass().getSimpleName();
  }

  @Nullable
  @Override public String description() {
    return super.description();
  }

  @Nullable
  public abstract String description(N n);

  @NotNull
  @Override public Example[] examples() {
    return new Example[] {};
  }

  /** Heuristics to find the class of operands on which this class works.
   * @return a guess for the type of the node. */
  @NotNull
  public final Class<N> myAbstractOperandsClass() {
    return myOperandsClass != null ? myOperandsClass : (myOperandsClass = initializeMyOperandsClass());
  }

  @Nullable
  public Class<N> myActualOperandsClass() {
    @NotNull final Class<N> $ = myAbstractOperandsClass();
    return !isAbstract($.getModifiers()) ? $ : null;
  }

  /** A wrapper function without ExclusionManager.
   * @param ¢ The ASTNode object on which we deduce the tip.
   * @return a tip given for the ASTNode ¢. */
  @Nullable
  public Tip tip(final N ¢) {
    return tip(¢, null);
  }

  @Nullable
  @Override public final Tip fire() {
    return tip(object());
  }

  /** @param n an ASTNode
   * @param m exclusion manager guarantees this tip to be given only once.
   * @return a tip given for the ASTNode ¢. */
  @Nullable
  public Tip tip(final N n, @Nullable final ExclusionManager m) {
    return m != null && m.isExcluded(n) ? null : tip(n);
  }

  @NotNull
  @SuppressWarnings("unchecked") private Class<N> castClass(@NotNull final Class<?> c2) {
    return (Class<N>) c2;
  }

  @NotNull
  private Class<N> initializeMyOperandsClass() {
    @Nullable Class<N> $ = null;
    for (@NotNull final Method ¢ : getClass().getMethods())
      if (¢.getParameterCount() == 1 && !Modifier.isStatic(¢.getModifiers()) && isDefinedHere(¢))
        $ = lowest($, ¢.getParameterTypes()[0]);
    return $ != null ? $ : castClass(ASTNode.class);
  }

  private boolean isDefinedHere(@NotNull final Method ¢) {
    return ¢.getDeclaringClass() == getClass();
  }

  @NotNull
  private Class<N> lowest(@Nullable final Class<N> c1, @Nullable final Class<?> c2) {
    return c2 == null || !ASTNode.class.isAssignableFrom(c2) || c1 != null && !c1.isAssignableFrom(c2) ? c1 : castClass(c2);
  }

  @NotNull
  @SuppressWarnings("unchecked") protected final Class<? extends Tipper<N>> myClass() {
    return (Class<? extends Tipper<N>>) getClass();
  }

  private Class<N> myOperandsClass;
}
