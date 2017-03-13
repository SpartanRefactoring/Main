package il.org.spartan.spartanizer.tipping;

import static java.lang.reflect.Modifier.*;

import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.Tipper.Example.*;

/** A tipper is a transformation that works on an AstNode. Such a transformation
 * make a single simplification of the tree. A tipper is so small that it is
 * idempotent: Applying a tipper to the output of itself is the empty operation.
 * @param <N> type of node which triggers the transformation.
 * @author Yossi Gil
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015-07-09 */
public abstract class Tipper<N extends ASTNode> extends Rule.Stateful<N, Tip> //
    implements TipperCategory, Serializable {
  public static Tipper.Example.Ignores ignores(final String code) {
    return () -> code;
  }

  private static final long serialVersionUID = -2252675511987504571L;

  @Override public String[] akas() {
    return new String[] { className() };
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

  @Override public final boolean ok(final N ¢) {
    return canTip(¢);
  }

  public abstract boolean canTip(N n);

  public String className() {
    return getClass().getSimpleName();
  }

  @Override public String description() {
    return super.description();
  }

  public abstract String description(N n);

  @Override public Example[] examples() {
    return new Example[] {};
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

  /** A wrapper function without ExclusionManager.
   * @param ¢ The ASTNode object on which we deduce the tip.
   * @return a tip given for the ASTNode ¢. */
  public Tip tip(final N ¢) {
    return tip(¢, null);
  }

  @Override public final Tip fire() {
    return tip(get());
  }

  /** @param n an ASTNode
   * @param m exclusion manager guarantees this tip to be given only once.
   * @return a tip given for the ASTNode ¢. */
  public Tip tip(final N n, final ExclusionManager m) {
    return m != null && m.isExcluded(n) ? null : tip(n);
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

  /** Auxiliary class for FAPI
   * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
   * @since 2017-03-07 */
  public interface Converter {
    Converts to(String to);
  }

  public interface Example {
    static Converter convert(final String from) {
      return to -> new Tipper.Example.Converts() {
        @Override public String from() {
          return from;
        }
    
        @Override public String to() {
          return to;
        }
      };
    }

    interface Converts extends Example {
      String from();

      String to();
    }

    @FunctionalInterface
    interface Ignores extends Example {
      String code();
    }
  }
}
