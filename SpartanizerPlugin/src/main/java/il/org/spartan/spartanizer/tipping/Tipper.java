package il.org.spartan.spartanizer.tipping;

import static java.lang.reflect.Modifier.*;

import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Examples.*;
import nano.ly.*;

/** A tipper is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A tipper is so small that it is
 * idempotent: Applying a tipper to the output of itself is the empty operation.
 * @param <N> __ of node which triggers the transformation.
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
    return separate.these(cCamelCase.components(tipperName())).bySpaces();
  }

  public abstract String description(N n);

  @Override public boolean equals(final Object ¢) {
    return getClass().equals(¢.getClass());
  }

  @Override public final Tip fire() {
    return tip(current());
  }

  /** Heuristics to find the class of operands on which this class works.
   * @return a guess for the __ of the node. */
  public final Class<N> getAbstractOperandClass() {
    return myOperandsClass != null ? myOperandsClass : (myOperandsClass = initializeMyOperandsClass());
  }

  public Class<N> myActualOperandsClass() {
    final Class<N> $ = getAbstractOperandClass();
    return !isAbstract($.getModifiers()) ? $ : null;
  }

  @SuppressWarnings("unchecked") public final Class<Tipper<N>> myClass() {
    return (Class<Tipper<N>>) getClass();
  }

  @Override public final boolean ok(final N ¢) {
    return canTip(¢);
  }

  public abstract Tip tip(N ¢);

  /**
   * @return a string that represents why the tipper failed to tip on the given node.
   */
  @SuppressWarnings("unused") public String explain(N ¢) {
    return null;
  }

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

  public static <N extends ASTNode> Tipper<N> materialize(final IMarker $) {
    if ($.getResource() == null)
      return null;
    final Object o = getKey($);
    if (o == null)
      return nil.forgetting(note.bug("Missing attribute"));
    if (!(o instanceof Class))
      return nil.forgetting(note.bug("Attribute of wrong __"));
    @SuppressWarnings("unchecked") final Class<? extends Tipper<N>> tipperClass = (Class<? extends Tipper<N>>) (Class<?>) o;
    return Tipper.instantiate(tipperClass);
  }

  private static Object getKey(final IMarker $) {
    try {
      return $.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY);
    } catch (final CoreException ¢) {
      return note.bug(¢);
    }
  }

  public static <X extends ASTNode, T extends Tipper<X>> Tipper<X> instantiate(final Class<T> $) {
    try {
      return $.newInstance();
    } catch (InstantiationException | IllegalAccessException ¢) {
      return note.bug(¢);
    }
  }

  private Class<N> myOperandsClass;
}
