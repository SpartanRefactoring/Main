package il.org.spartan.reflection;

import static fluent.ly.___.*;

import java.lang.reflect.*;

import org.jetbrains.annotations.*;

/** A class realizing a recursive traversal of a reflection {@link Class}
 * object, applying the appropriate function supplied by a {@link Visitor} to
 * the object itself and to all data, function, and class members of this
 * object.
 * <p>
 * Each of these visitation functions returns an <code><b>int</b></code>. This
 * class computes the sum of all returned values.
 * <p>
 * Visitation functions are first invoked on all members, and only then it
 * recurses to members of inner classes.
 * @author Yossi Gil
 * @since 04/08/2007 */
public final class ReflectionTraversal {
  /** where should visitation start */
  public final Class<?> clazz;
  /** what should be done in each reflection node encountered? */
  public final Visitor visitor;

  /** Setup the traversal pattern
   * @param clazz where should traversal start, must not be
   *        <code><b>null</b></code> .
   * @param visitor what should be done at each node, must not be
   *        <code><b>null</b></code>. */
  public ReflectionTraversal(final Class<?> clazz, final Visitor visitor) {
    nonnull(clazz);
    nonnull(visitor);
    this.clazz = clazz;
    this.visitor = visitor;
  }

  /** initiate the traversal
   * @return sum of all visit functions on all visited object */
  public int go() {
    return go(clazz);
  }

  private int go(@NotNull final Class<?> from) {
    nonnull(from);
    // Visit the class itself
    int $ = visitor.visit(from);
    // Visit all sorts of members
    for (final Field ¢ : from.getDeclaredFields())
      $ += visitor.visit(¢);
    for (final Method ¢ : from.getDeclaredMethods())
      $ += visitor.visit(¢);
    for (final Constructor<?> ¢ : from.getDeclaredConstructors())
      $ += visitor.visit(¢);
    for (final Class<?> ¢ : from.getDeclaredClasses())
      $ += visitor.visit(¢);
    // Recurse into inner, local and anonymous classes
    for (@NotNull final Class<?> ¢ : from.getDeclaredClasses())
      $ += go(¢);
    return $;
  }

  public interface Visitor {
    int visit(Class<?> c);

    int visit(Constructor<?> c);

    int visit(Field f);

    int visit(Method m);
  }
}
