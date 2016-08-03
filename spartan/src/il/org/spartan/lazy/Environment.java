/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import static il.org.spartan.azzert.*;
import static il.org.spartan.idiomatic.*;
import static java.lang.Math.*;
import static org.hamcrest.Matchers.*;
import il.org.spartan.*;
import il.org.spartan.lazy.Cookbook.Ingredient;
import il.org.spartan.lazy.Cookbook.Recipe;
import il.org.spartan.lazy.Cookbook.__META;

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
 * <li> {@link Property#¢()}, returning the cell's value, recomputing it if it is
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
@SuppressWarnings("javadoc") public interface Environment {
  static <@Nullable T, @Nullable A> Binder1<T, A> bind(final Function1<T, A> f) {
    return new Property<T>().bind(f);
  }
  static <@Nullable T, @Nullable A1, @Nullable A2> Binder2<@Nullable T, @Nullable A1, @Nullable A2> bind(final Function2<T, A1, A2> f) {
    return new Property<T>().bind(f);
  }
  static <@Nullable T, @Nullable A1, @Nullable A2, @Nullable A3> Binder3<T, A1, A2, A3> bind(final Function3<T, A1, A2, A3> f) {
    return new Property<T>().bind(f);
  }
  static <@Nullable T, @Nullable A1, @Nullable A2, @Nullable A3, @Nullable A4> Binder4<T, A1, A2, A3, A4> bind(final Function4<T, A1, A2, A3, A4> f) {
    return new Property<T>().bind(f);
  }
  static <@Nullable T> Property<T> function(final Function0<T> f) {
    return new Property<T>().set(f);
  }
  /**
   * A factory method of class  {@link Property}  returning an undefined value for a cell
   * @param < T >  JD
   * @return  the newly created instance of  {@link Property}  containing null value of the type parameter 
   */
  static <@Nullable T> Property<T> undefined() {
    return new Property<>();
  }
  /**
   * A factory method of class  {@link Property}  of an  {@link Integer}  as in <pre> Property&lt;Integer&gt; genesis =  {@link Environment} .value(2); </pre>
   * @param i  JD
   * @return  the newly created instance of  {@link Ingredient}  
   */
  static Property<@Nullable Integer> value(final int i) {
    return new Property<>(Integer.valueOf(i));
  }
  /**
   * A factory method for class  {@link Ingredient}  as in <pre> Property&lt;String&gt; genesis = Cookbook.value(&quot;&quot;); </pre>
   * @param < T >  JD
   * @param t  JD
   * @return  the newly created instance of  {@link Property}  
   */
  static <@Nullable T> Property<@Nullable T> value(final T t) {
    return new Property<>(t);
  }

  @FunctionalInterface interface Binder1<@Nullable T, @Nullable A> {
    Property<T> to(Property<A> ¢);
  }

  @FunctionalInterface interface Binder2<@Nullable T, @Nullable A1, @Nullable A2> {
    Property<T> to(Property<A1> ¢1, Property<A2> ¢2);
  }

  @FunctionalInterface interface Binder3<@Nullable T, @Nullable A1, @Nullable A2, @Nullable A3> {
    Property<T> to(Property<A1> ¢1, Property<A2> ¢2, Property<A3> ¢3);
  }

  @FunctionalInterface interface Binder4<@Nullable T, @Nullable A1, @Nullable A2, @Nullable A3, @Nullable A4> {
    Property<T> to(Property<A1> ¢1, Property<A2> ¢2, Property<A3> ¢3, Property<A4> ¢4);
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
    /** Instantiates this class. */
    public Property() {
      // Nothing to do, we start with a default null value
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
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking one argument
     * @param <A> argument's type
     * @param f a one argument function that returns a new value for this
     *          instance
     * @return a function with one argument named {@link Binder#to(Object...)}
     *         which when applied
     *         <ol>
     *         <li>changes the current instance
     *         <li>returns <code><b>this</b></code>
     *         </ol> */
    public <@Nullable A> Binder1<T, A> bind(final Function1<T, A> f) {
      return ¢ -> set(() -> f.ϑ(¢.¢()), ¢);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking two arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param f a two argument function that returns a new value for this
     *          instance
     * @return a function with two arguments named {@link Binder2#to} which when
     *         applied changes the current instance returning
     *         <code><b>this</b></code> */
    public <@Nullable A1, @Nullable A2> Binder2<@Nullable T, @Nullable A1, @Nullable A2> bind(final Function2<T, A1, A2> f) {
      return (¢1, ¢2) -> set(() -> f.ϑ(¢1.¢(), ¢2.¢()), ¢1, ¢2);
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
    public <@Nullable A1, @Nullable A2, @Nullable A3> Binder3<T, A1, A2, A3> bind(final Function3<T, A1, A2, A3> f) {
      return (¢1, ¢2, ¢3) -> set(() -> f.ϑ(¢1.¢(), ¢2.¢(), ¢3.¢()), ¢1, ¢2, ¢3);
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
    public <@Nullable A1, @Nullable A2, @Nullable A3, @Nullable A4> Binder4<T, A1, A2, A3, A4> bind(final Function4<T, A1, A2, A3, A4> f) {
      return (¢1, ¢2, ¢3, ¢4) -> set(() -> f.ϑ(¢1.¢(), ¢2.¢(), ¢3.¢(), ¢4.¢()), ¢1, ¢2, ¢3, ¢4);
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
      return ¢();
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
     *         {@link Binder3#to(Object, Object, Object)} which when applied
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
    /** puts this instance in an undefined state
     * @return <code><b>this</b></code> */
    public Property<@Nullable T> undefine() {
      cache(null);
      return this;
    }
    /** @return <code><b>true</b></code> <em>iff</em> the value in this cell is
     *         updated with respect to all its prerequisites */
    public boolean updated() {
      if (ϑ == null)
        return true;
      if (cache() == null)
        return false;
      for (final Property<?> ¢ : prerequisites)
        if (version() <= ¢.version() || !¢.updated())
          return false;
      return true;
    }
    /** @return the version of this instance */
    public long version() {
      return version;
    }
    /** @return current value stored in this instance, recomputed if necessary */
    @Override public T ¢() {
      if (updated())
        return cache();
      assert ϑ != null;
      for (final Property<?> ¢ : prerequisites)
        ¢.update();
      version = latestPrequisiteVersion() + 1;
      try {
        return set(ϑ.¢());
      } catch (final Exception x) {
        undefine();
        return null;
      }
    }
    /** TODO Javadoc(2016): automatically generated for method
     * <code>update</code> <em>iff</em> <code>s</code> ends with any
     * @return <code>1</code> if the parameter is true, 0 otherwise
     * @return an array of
     * @return an array of length <code>0</code> of instances of <code>T</code>.
     * @return an array of {@link Something} of the same length as that of
     * @return an array of {@link Something} of length <code>12</code>
     * @return an array of the parameter values, each converted to i
     * @return an array representation of the parameter
     * @return an empty {@link Something}
     * @return a new array, containing precisely those non-
     * @return a new collection, containing only those non-
     * @return a newly created instance of {@link Something} with the value of
     * @return an identical instance which is also {@link Something}
     * @return an {@link Iterable} over the parameter
     * @return a non-<code><b>null</b></code> {@link Something} with the value
     * @return <code>0</code> if the parameter <em>iff</em> one or more of the
     *         parameters is
     * @return <code>-1</code> if the parameter <em>iff</em> the parameter has
     * @return <code>1</code> if the parameter
     * @return <code><b>null</b></code> <em>iff</em> the parameter is
     * @return <code><b>this</b></code>
     * @return <code><b>true</b></code>
     * @return <code><b>false</b></code>
     * @return <code><b>true</b></code> <i>iff</i> the parameter is
     * @return <code><b>true</b></code> <i>iff</i> the receive is empty
     * @return contents of {@link Something}
     * @return <em>iff</em> the contents of
     * @return <em>iff</em> the index is a valid index into
     * @return <em>iff</em> the {@link Object} parameter is
     * @return never! The <code><b>none</b></code> type. There is no legal value
     * @return the newly created array
     * @return the newly created {@link Something} instance
     * @return the next value in the parameter
     * @return the number of
     * @return the parameter
     * @return the parameter, after all such occurrences are removed.
     * @return the parameter, after to a non-null string.
     * @return the parameter, but in a form of
     * @return the parameter, converted into {@link Something}
     * @return the parameters, concatenated together, with
     * @return the parameters, organized as an array with entries whose type
     *         is */
    public void update() {
      // Make sure the value on the instance is as updated as possible
      get();
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
      version = 0;
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
  }

  @SuppressWarnings({ "static-method", "null" }) public static class TEST {
    private static final String EMPTY = "";
    private static final int FIRST_MAGIC_NUMBER = 1729;
    private static final String HELLO = "Hello";
    private static final int SECOND_MAGIC_NUMBER = FIRST_MAGIC_NUMBER << 1 ^ FIRST_MAGIC_NUMBER;
    private static final String SEPARATOR = ", ";
    private static final String WORLD = "World!";

    @Before public void init() {
      nullProperty = null;
      undefinedProperty = undefined();
      hello = value(HELLO);
      world = value(WORLD);
      separator = value(SEPARATOR);
      emptyString = value(EMPTY);
      helloWorld = bind((String ¢1, String ¢2, String ¢3) -> (¢1 + ¢2 + ¢3)).to(hello, separator, world);
    }
    @Test public void seriesA0() {
      azzert.that(hello.¢(), iz(HELLO));
      azzert.isNull(nullProperty);
      azzert.isNull(undefinedProperty.ϑ);
      azzert.isNull(undefinedProperty.¢());
      azzert.that(emptyString.¢(), iz(EMPTY));
    }
    @Test public void seriesA1() {
      azzert.that(hello.¢(), iz(HELLO));
      hello.undefine();
      azzert.isNull(hello.ϑ);
      azzert.isNull(hello.¢());
    }
    @Test public void seriesA2() {
      azzert.that(hello.¢(), iz(HELLO));
      hello.set(WORLD);
      azzert.that(hello.¢(), iz(WORLD));
    }
    @Test public void seriesA3() {
      azzert.that(helloWorld.¢(), iz(HELLO + SEPARATOR + WORLD));
      helloWorld.bind((String ¢1, String ¢2, String ¢3, String ¢4) -> (¢1 + ¢2 + ¢3 + ¢4)).to(hello, world, hello, world);
      azzert.that(helloWorld.¢(), iz(HELLO + WORLD + HELLO + WORLD));
    }
    @Test public void seriesA4() {
      azzert.that(supplierCalls, is(0));
      azzert.that(integer.version(), is(0L));
      azzert.nay(integer.updated());
      azzert.that(integer.¢(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.get(), is(FIRST_MAGIC_NUMBER));
      azzert.that(supplierCalls, is(1));
      azzert.that(integer.¢(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.get(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.¢(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.get(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.¢(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.get(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.¢(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.get(), is(FIRST_MAGIC_NUMBER));
      azzert.that(integer.¢(), is(FIRST_MAGIC_NUMBER));
      azzert.that(supplierCalls, is(1));
      azzert.that(integer.get(), is(FIRST_MAGIC_NUMBER));
      integer.set(Integer.valueOf(SECOND_MAGIC_NUMBER));
      azzert.aye(integer.updated());
      azzert.that(integer.get(), is(SECOND_MAGIC_NUMBER));
      azzert.that(supplierCalls, is(1L));
      azzert.that(integer.get(), is(SECOND_MAGIC_NUMBER));
      azzert.that(supplierCalls, is(1));
    }
    @Test public void seriesA5() {
      azzert.that(helloWorld.prerequisites.size(), is(3));
      azzert.that(helloWorld.dependents, empty());
      azzert.that(helloWorld.version, is(0L));
      azzert.nay(helloWorld.updated());
    }
    @Test public void seriesA6() {
      Property<String> $ = bind((String ¢1, String ¢2, String ¢3) -> (¢1 + ¢2 + ¢3)).to(hello, separator, world);
      azzert.that($.prerequisites.size(), is(3));
      azzert.that($.dependents, empty());
      azzert.that($.version, is(0L));
      azzert.that(hello.version, is(0L));
      azzert.that(separator.version, is(0L));
      azzert.that(world.version, is(0L));
      azzert.nay($.updated());
      $.get();
      azzert.that($.get(), iz(HELLO + SEPARATOR + WORLD));
      azzert.aye($.updated());
      azzert.that($.version, is(1L));
      azzert.that(hello.version, is(0L));
      azzert.that(separator.version, is(0L));
      azzert.that(world.version, is(0L));
      hello.set(WORLD);
      azzert.that($.version, is(1L));
      azzert.that(hello.version, is(1L));
      azzert.that(separator.version, is(0L));
      azzert.that(world.version, is(0L));
      azzert.nay($.updated());
      $.get();
      azzert.that($.get(), iz(WORLD + SEPARATOR + WORLD));
      azzert.that(hello.version, is(1L));
      azzert.that(separator.version, is(0L));
      azzert.that(world.version, is(0L));
      azzert.that($.version, is(2L));
      azzert.aye($.updated());
      azzert.that(helloWorld.get(), iz(WORLD + SEPARATOR + WORLD));
    }
    @Test public void seriesA7() {
      // TODO: fill this test case
    }
    @Test public void seriesA8() {
      // TODO: fill this test case
    }
    @Test public void seriesA9() {
      final Property<String> a = value(EMPTY);
      azzert.aye(a.dependents.isEmpty()).andAye(a.prerequisites.isEmpty());
      azzert.zero(a.version);
      a.of(WORLD);
      azzert.aye(a.dependents.isEmpty()).andAye(a.prerequisites.isEmpty());
      azzert.that(a.¢(), iz(WORLD));
      azzert.that(a.version, is(1L));
      final Property<String> b = new Property<>();
      azzert.aye(b.dependents.isEmpty()).andAye(b.prerequisites.isEmpty());
      azzert.zero(b.version);
      b.bind((final String ¢) -> "Hello, " + ¢).to(a);
      azzert.that(b.dependents, empty());
      azzert.nay(b.prerequisites.isEmpty());
      azzert.that(b.prerequisites, contains(azzert.is(a)));
      b.bind((final String ¢) -> "Hello, " + a.¢()).to(a);
      b.bind((final String ¢) -> "Hello, " + a.¢()).to(a);
    }

    private Property<String> emptyString;
    private Property<String> hello;
    private Property<String> helloWorld;
    private Property<String> nullProperty;
    private Property<String> separator;
    private int supplierCalls;
    private Property<String> undefinedProperty = undefined();
    private Property<String> world;
    Property<Integer> integer = function(() -> {
      ++supplierCalls;
      return Integer.valueOf(FIRST_MAGIC_NUMBER);
    });
  }
}
