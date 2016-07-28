/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import static il.org.spartan.azzert.*;
import static il.org.spartan.idiomatic.*;
import static java.lang.Math.*;
import static org.hamcrest.Matchers.*;
import il.org.spartan.Utils.Applicator;
import il.org.spartan.*;
import il.org.spartan.lazy.Cookbook.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

/** This interface represents the concept of a <i>lazy symbolic spreadsheet</i>,
 * made by DAG of interdependent {@link Property}s. A {@link Property} is either
 * an
 * <ol>
 * <li> {@link Ingredient} is a generic storing a value of the type parameter,
 * <i>or</i> a
 * <li> {@link Recipe} for making such a value from <i>prerequisite</i> cells.
 * </ol>
 * <p>
 * The protocol of cells include:
 * <ol>
 * <li> {@link Property#ϑ()}, returning the cell's value, recomputing it if it is
 * less recent than any {@link Ingredient} on which it depends, directly or
 * indirectly. The computed value is cached, and used in subsequent calls to
 * prevent unnecessary re-computation.
 * <li> {@link Property#cache()}, returning the last value cached in the cell.
 * <li> {@link Property#of(Object)}, whose parameter must be of the correct type,
 * stores its parameter in the cell, whereby invalidating the contents of all
 * cells whose recipe depends on it.
 * <p>
 * When a cell is set to a specific value, the recipe is lost forever.
 * </ol>
 * <p>
 * Thus, a cell's value is only evaluated when it is accessed, and only when it
 * out of date with respect to the cells it depends on.
 * <p>
 * Note that
 * <ol>
 * <li>Classes in this interface give several implementations of the cell
 * concept.
 * <li>Inner classes of {@link __META} provide examples and extensive unit
 * testing.
 * <li>There are no other members: A client class that
 * <code><b>implements</b></code> this interface should be ok.
 * <ol>
 * apply
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public interface Environment {
  /** A factory method of class {@link Property} returning an a null.
   * @param <T> JD
   * @return the newly created instance of {@link Property} containing null
   *         value of the type parameter */
  public static <@Nullable T> Property<T> nullValue() {
    return new Property<>();
  }
  /** A factory method of class {@link Property} of an {@Integer} as in
   * 
   * <pre>
   * Property&lt;String&gt; genesis = Cookbook.value(2);
   * </pre>
   * @param i JD
   * @return the newly created instance of {@link Ingredient} */
  public static Property<Integer> value(final int i) {
    return new Property<>(Integer.valueOf(i));
  }
  /** A factory method for class {@link Ingredient} as in
   * 
   * <pre>
   * Property&lt;String&gt; genesis = Cookbook.value(&quot;&quot;);
   * </pre>
   * @param <T> JD
   * @param t JD
   * @return the newly created instance of {@link Property} */
  public static <@Nullable T> Property<@Nullable T> value(final T t) {
    return new Property<>(t);
  }
  /** A property stores a value of some type (which is passed by parameter). A
   * property may be either atomic which behaves simply like a variable, with
   * appropriate {@link #get()} and {@link #set(Object)} methods. A computed
   * property typically depends on other properties, which may either valued, or
   * computed, and hence depending on yet other properties. A change to a
   * property's value is triggers invalidates all properties that depend on it.
   * @param <T> type of value of this property
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  public static class Property<@Nullable T> implements Function0<T>, Cloneable {
    private static long maxVersion(final Iterable<Property<?>> ps) {
      long $ = 0;
      for (final Property<?> c : ps)
        $ = max($, c.version());
      return $;
    }
    /** Instantiates this class.
     * @param λ JD */
    public Property(final Function0<? extends T> λ) {
      this.ϑ = λ;
    }
    /** Instantiates this class.
     * @param t initial cached value */
    public Property(final T t) {
      cache(t);
    }
    /** Instantiates this class. */
    public Property() {
      // Nothing to do, we start with a default null value
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking one argument
     * @param <A> argument's type
     * @param f a one argument function that returns a new value for this
     *          instance
     * @return a function with one argument named
     *         {@link Applicator#to(Object...)} which when applied
     *         <ol>
     *         <li>changes the current instance
     *         <li>returns <code><b>this</b></code>
     *         </ol> */
    public <@Nullable A> Applicator1<T, A> apply(final Function1<T, A> f) {
      return ¢ -> set(() -> f.ϑ(¢.ϑ()), ¢);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking two arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param f a two argument function that returns a new value for this
     *          instance
     * @return a function with two arguments named {@link Applicator2#to} which
     *         when applied changes the current instance returning
     *         <code><b>this</b></code> */
    public <@Nullable A1, @Nullable A2> Applicator2<@Nullable T, @Nullable A1, @Nullable A2> apply(final Function2<T, A1, A2> f) {
      return (¢1, ¢2) -> set(() -> f.ϑ(¢1.ϑ(), ¢2.ϑ()), ¢1, ¢2);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking four arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param <A3> 3rd argument's type
     * @param f a one argument function that returns a new value for this
     *          instance
     * @return a function with four arguments named {@link #toString()} which
     *         when applied changes the current instance and returning
     *         <code><b>this</b></code> */
    public <@Nullable A1, @Nullable A2, @Nullable A3> Applicator3<T, A1, A2, A3> apply(final Function3<T, A1, A2, A3> f) {
      return (¢1, ¢2, ¢3) -> set(() -> f.ϑ(¢1.ϑ(), ¢2.ϑ(), ¢3.ϑ()), ¢1, ¢2, ¢3);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking four arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param <A3> 3rd argument's type
     * @param <A4> 4th argument's type
     * @param f a one argument function that returns a new value for this
     *          instance
     * @return a function with four arguments named {@link #toString()} which
     *         when applied changes the current instance and returning
     *         <code><b>this</b></code> */
    public <@Nullable A1, @Nullable A2, @Nullable A3, @Nullable A4> Applicator4<T, A1, A2, A3, A4> apply(final Function4<T, A1, A2, A3, A4> f) {
      return (¢1, ¢2, ¢3, ¢4) -> set(() -> f.ϑ(¢1.ϑ(), ¢2.ϑ(), ¢3.ϑ(), ¢4.ϑ()), ¢1, ¢2, ¢3, ¢4);
    }
    /** @return the last value computed or set for this instance. */
    public final T cache() {
      return cache;
    }
    @Override @SuppressWarnings("unchecked") public Property<T> clone() {
      try {
        return (Property<T>) super.clone();
      } catch (final CloneNotSupportedException x) {
        throw new RuntimeException(x);
      }
    }
    /** @return current value stored in this instance, recomputed if necessary */
    public T get() {
      return ϑ();
    }
    /** Add another property on which this instance depends
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    public Property<T> ingredient(final Property<?> ¢) {
      run(() -> ¢.dependents.add(Property.this)).unless(¢.dependents.contains(this));
      run(() -> prerequisites.add(¢)).unless(prerequisites.contains(this));
      return this;
    }
    /** Add another property on which this instance depends
     * @param ps JD
     * @return <code><b>this</b></code> */
    public Property<T> ingredients(final Property<?>... ps) {
      for (final Property<?> ¢ : ps)
        ingredient(¢);
      return this;
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking four arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param <A3> 3rd argument's type
     * @param <A4> 4th argument's type
     * @param ϑ a one argument function that returns a new value for this
     *          instance
     * @return a function with three arguments named
     *         {@link Applicator3#to(Object, Object, Object)} which when applied
     *         changes the current instance and returning
     *         <code><b>this</b></code> */
    /** Used for fluent API; sets the current value of this instance
     * @param f a no-arguments function that returns a value for this instance
     * @return <code><b>this</b></code> */
    public Property<T> of(final Function0<T> f) {
      return set(f);
    }
    /** Used for fluent API; sets the current value of this instance
     * @param t JD
     * @return <code><b>this</b></code>* */
    public Property<T> of(final T t) {
      cache(t);
      ϑ = null;
      version = latestDependentVersion() + 1; // Invalidate all dependents
      prerequisites.clear();
      return this;
    }
    /** forcibly set the value stored in this instance, ignoring the function
     * used for computing it, and marks this instance as updated with respect to
     * all prerequisites.
     * @param t JD
     * @return <code><b>this</b></code> */
    public @Nullable T set(final T t) {
      version = latestPrequisiteVersion() + 1;
      return cache(t);
    }
    /** @return <code><b>true</b></code> <em>iff</em> the value in this cell is
     *         updated with respect to all its preqrequisites */
    public boolean updated() {
      if (ϑ == null)
        return true;
      for (final Property<?> ¢ : prerequisites)
        if (version() < ¢.version() || !¢.updated())
          return false;
      return true;
    }
    /** @return the version of this instance */
    public long version() {
      return version;
    }
    /** @return T current value stored in this instance, recomputed if necessary */
    @Override public T ϑ() {
      if (updated())
        return cache();
      assert ϑ != null;
      for (final Property<?> ¢ : prerequisites)
        ¢.ϑ();
      assert ϑ != null;
      return set(ϑ.ϑ());
    }
    @Nullable T cache(@SuppressWarnings("hiding") final T cache) {
      return this.cache = cache;
    }
    final long latestDependentVersion() {
      return maxVersion(dependents);
    }
    long latestPrequisiteVersion() {
      return maxVersion(prerequisites);
    }
    /** @param ϑ a no-arguments function that returns a value for this instance
     * @param cs instances on which the cell depends
     * @return <code><b>this</b></code> **/
    Property<T> set(final Function0<T> ϑ, final Property<?>... cs) {
      this.ϑ = ϑ; // Set the encapsulated function
      // Clear prerequisites
      dependents.removeAll(prerequisites);
      prerequisites.clear();
      ingredients(cs);
      version = latestDependentVersion() + 1;
      return this;
    }

    /** The last value computed for this instance */
    @Nullable T cache = null;
    /** other properties that depend on this instance */
    final List<Property<?>> dependents = new ArrayList<>();
    /** other properties on which this instance depends */
    final List<Property<?>> prerequisites = new ArrayList<>();
    /** version of this instance */
    long version = 0;
    /** returns the instance updated value when invoked */
    @Nullable Function0<? extends @Nullable T> ϑ = null;

    @SuppressWarnings({ "javadoc", "static-method", "null" }) public static class TEST {
      @Test public void seriesA00() {
        final Property<String> a = value("");
        azzert.aye(a.dependents.isEmpty()).andAye(a.prerequisites.isEmpty());
        azzert.zero(a.version);
        a.of("World!");
        azzert.aye(a.dependents.isEmpty()).andAye(a.prerequisites.isEmpty());
        azzert.that(a.ϑ(), iz("World!"));
        azzert.that(a.version, is(1L));
        final Property<String> b = new Property<>();
        azzert.aye(b.dependents.isEmpty()).andAye(b.prerequisites.isEmpty());
        azzert.zero(b.version);
        b.apply((final String ¢) -> "Hello, " + ¢).to(a);
        azzert.that(b.dependents, empty());
        azzert.nay(b.prerequisites.isEmpty());
        azzert.that(b.prerequisites, contains(azzert.is(a)));
        b.apply((final String ¢) -> "Hello, " + a.ϑ()).to(a);
        b.apply((final String ¢) -> "Hello, " + a.ϑ()).to(a);
      }
    }

    @FunctionalInterface interface Applicator1<@Nullable T, A> {
      Property<T> to(Property<A> ¢);
    }

    @FunctionalInterface interface Applicator2<@Nullable T, @Nullable A1, @Nullable A2> {
      Property<T> to(Property<A1> ¢1, Property<A2> ¢2);
    }

    @FunctionalInterface interface Applicator3<@Nullable T, @Nullable A1, @Nullable A2, @Nullable A3> {
      Property<T> to(Property<A1> ¢1, Property<A2> ¢2, Property<A3> ¢3);
    }

    @FunctionalInterface interface Applicator4<@Nullable T, @Nullable A1, @Nullable A2, @Nullable A3, @Nullable A4> {
      Property<T> to(Property<A1> ¢1, Property<A2> ¢2, Property<A3> ¢3, Property<A4> ¢4);
    }
  }
}