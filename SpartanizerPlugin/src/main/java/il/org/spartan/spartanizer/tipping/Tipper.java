package il.org.spartan.spartanizer.tipping;

import static java.lang.reflect.Modifier.*;

import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Examples.*;
import il.org.spartan.utils.fluent.*;

/** A tipper is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A tipper is so small that it is
 * idempotent: Applying a tipper to the output of itself is the empty operation.
 * @param <N> type of node which triggers the transformation.
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09 */
public abstract class Tipper<N extends ASTNode> extends Rule.Stateful<N, Tip> //
    implements TipperCategory, Serializable {
  protected static Converter convert(final String from) {
    return new Examples()/** 12 */
        .convert(from);
  }

  private static final long serialVersionUID = -0x1F431C71663C85BBL;

  @Override public String[] akas() {
    return new String[] { tipperName() };
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

  public final String className() {
    return English.name(this);
  }

  @Override public String description() {
    return tipperName();
  }

  public abstract String description(N n);

  @Override public boolean equals(final Object ¢) {
    return getClass().equals(¢.getClass());
  }

  @Override public final Tip fire() {
    return tip(current());
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

  @SuppressWarnings("unchecked") public final Class<Tipper<N>> myClass() {
    return (Class<Tipper<N>>) getClass();
  }

  @Override public final boolean ok(final N ¢) {
    return canTip(¢);
  }

  public abstract Tip tip(N ¢);

  public String tipperName() {
    return English.name(myClass());
  }

  @SuppressWarnings("unchecked") private Class<N> castClass(final Class<?> c2) {
    return (Class<N>) c2;
  }

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

  private Class<N> myOperandsClass;
}
