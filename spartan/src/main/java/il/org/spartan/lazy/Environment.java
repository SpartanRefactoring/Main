/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static fluent.ly.idiomatic.run;
import static java.lang.Math.max;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.reap.Ingredient;
import il.org.spartan.reap.Recipe;

/** This interface represents the concept of a <i>lazy symbolic spreadsheet</i>,
 * made by DAG of interdependent {@link Property}s. A {@link Property} is either
 * an
 * <ol>
 * <li>{@link Ingredient} is a generic storing a value of the type parameter,
 * <i>or</i> a
 * <li>{@link Recipe} for making such a value from <i>prerequisite</i> cells.
 * </ol>
 * <p>
 * The protocol of cells include:
 * <ol>
 * <li>{@link Property#¢()}, returning the cell's value, recomputing it if it is
 * less recent than any {@link Ingredient} on which it depends, directly or
 * indirectly. The computed value is cached, and used in subsequent calls to
 * prevent unnecessary re-computation.
 * <li>{@link Property#cache()}, returning the last value cached in the cell.
 * <li>{@link Property#of(Object)}, whose parameter must be of the correct type,
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
 * <li>Inner classes of {@link ____META} provide examples and extensive unit
 * testing.
 * <li>There are no other members: A client class that
 * <code><b>implements</b></code> this interface should be ok.
 * <ol>
 * apply
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public interface Environment {
  static <T, A> Binder1<T, A> bind(final Function1<T, A> ¢) {
    return new Property<T>().bind(¢);
  }
  static <T, A1, A2> Binder2<T, A1, A2> bind(final Function2<T, A1, A2> ¢) {
    return new Property<T>().bind(¢);
  }
  static <T, A1, A2, A3> Binder3<T, A1, A2, A3> bind(final Function3<T, A1, A2, A3> ¢) {
    return new Property<T>().bind(¢);
  }
  static <T, A1, A2, A3, A4> Binder4<T, A1, A2, A3, A4> bind(final Function4<T, A1, A2, A3, A4> ¢) {
    return new Property<T>().bind(¢);
  }
  static <T> Property<T> function(final Function0<T> ¢) {
    return new Property<T>().ϑ(¢);
  }
  /** A factory method of class {@link Property} returning an undefined value
   * for a cell
   * @param <T> JD
   * @return newly created instance of {@link Property} containing null value of
   *         the type parameter */
  static <T> Property<T> undefined() {
    return new Property<>();
  }
  /** A factory method of class {@link Property} of an {@link Integer} as in
   *
   * <pre>
   *  Property&lt;Integer&gt; genesis =  {@link Environment} .value(2);
   * </pre>
   *
   * @param ¢ JD
   * @return newly created instance of {@link Ingredient} */
  static Property<Boolean> value(final boolean ¢) {
    return new Property<>(Boolean.valueOf(¢));
  }
  /** A factory method of class {@link Property} of an {@link Integer} as in
   *
   * <pre>
   *  Property&lt;Integer&gt; genesis =  {@link Environment} .value(2);
   * </pre>
   *
   * @param ¢ JD
   * @return newly created instance of {@link Ingredient} */
  static Property<Integer> value(final int ¢) {
    return new Property<>(Integer.valueOf(¢));
  }
  /** A factory method for class {@link Ingredient} as in
   *
   * <pre>
   * Property&lt;String&gt; genesis = Cookbook.value(&quot;&quot;);
   * </pre>
   *
   * @param <T> JD
   * @param ¢ JD
   * @return newly created instance of {@link Property} */
  static <T> Property<T> value(final T ¢) {
    return new Property<>(¢);
  }

  @SuppressWarnings("static-method")
  interface ____META {
    class TEST {
      private static final String EMPTY = "";
      private static final int FIRST_MAGIC_NUMBER = 1729;
      private static final String HELLO = "Hello";
      private static final int SECOND_MAGIC_NUMBER = FIRST_MAGIC_NUMBER << 1 ^ FIRST_MAGIC_NUMBER;
      private static final String SEPARATOR = ", ";
      private static final String WORLD = "World!";
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

      @Before public void init() {
        nullProperty = null;
        undefinedProperty = undefined();
        hello = value(HELLO);
        world = value(WORLD);
        separator = value(SEPARATOR);
        emptyString = value(EMPTY);
        helloWorld = bind((final String ¢1, final String ¢2, final String ¢3) -> (¢1 + ¢2 + ¢3)).to(hello, separator, world);
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
        helloWorld.bind((final String ¢1, final String ¢2, final String ¢3, final String ¢4) -> (¢1 + ¢2 + ¢3 + ¢4)).to(hello, world, hello, world);
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
        final Property<String> $ = bind((final String ¢1, final String ¢2, final String ¢3) -> (¢1 + ¢2 + ¢3)).to(hello, separator, world);
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
        //
      }
      @Test public void seriesA8() {
        //
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
    }
  }

  @FunctionalInterface
  interface Binder1<T, A> {
    Property<T> to(Property<A> ¢);
  }

  @FunctionalInterface
  interface Binder2<T, A1, A2> {
    Property<T> to(Property<A1> ¢1, Property<A2> ¢2);
  }

  @FunctionalInterface
  interface Binder3<T, A1, A2, A3> {
    Property<T> to(Property<A1> ¢1, Property<A2> ¢2, Property<A3> ¢3);
  }

  @FunctionalInterface
  interface Binder4<T, A1, A2, A3, A4> {
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
  class Property<T> implements Function0<T>, Cloneable {
    private static long maxVersion(final Iterable<Property<?>> ps) {
      long $ = 0;
      for (final Property<?> c : ps)
        $ = max($, c.version());
      return $;
    }

    /** The last value computed for this instance */
    T cache;
    /** other properties that depend on this instance */
    final List<Property<?>> dependents = new ArrayList<>();
    /** other properties on which this instance depends */
    final List<Property<?>> prerequisites = new ArrayList<>();
    /** version of this instance */
    long version;
    /** returns the instance updated value when invoked */
    Function0<? extends T> ϑ;
    private boolean frozen;

    /** Instantiates this class. */
    public Property() {}
    /** Instantiates this class.
     * @param λ JD */
    public Property(final Function0<? extends T> ϑ) {
      this.ϑ = ϑ;
    }
    /** Instantiates this class.
     * @param t initial cached value */
    public Property(final T t) {
      cache(t);
    }
    /** @return current value stored in this instance, recomputed if
     *         necessary */
    @Override public T ¢() {
      update();
      return cache();
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking one argument
     * @param <A> argument's type
     * @param ¢ a one argument function that returns a new value for this
     *        instance
     * @return a function with one argument named {@link Binder#to(Object)}
     *         which when applied
     *         <ol>
     *         <li>changes the current instance
     *         <li>returns <code><b>this</b></code>
     *         </ol>
    */
    public Property<T> bind(final Function0<T> ¢) {
      return ϑ(() -> ¢.¢());
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking one argument
     * @param <A> argument's type
     * @param f a one argument function that returns a new value for this
     *        instance
     * @return a function with one argument named {@link Binder#to(Object)}
     *         which when applied
     *         <ol>
     *         <li>changes the current instance
     *         <li>returns <code><b>this</b></code>
     *         </ol>
    */
    public <A> Binder1<T, A> bind(final Function1<T, A> f) {
      return ¢ -> ϑ(() -> f.ϑ(¢.¢()), ¢);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking two arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param a a two argument function that returns a new value for this
     *        instance
     * @return a function with two arguments named {@link Binder2#to} which when
     *         applied changes the current instance returning
     *         <code><b>this</b></code> */
    public <A1, A2> Binder2<T, A1, A2> bind(final Function2<T, A1, A2> a) {
      return (¢1, ¢2) -> ϑ(() -> a.ϑ(¢1.¢(), ¢2.¢()), ¢1, ¢2);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking four arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param <A3> 3rd argument's type
     * @param a a one argument function that returns a new value for this
     *        instance
     * @return a function with four arguments named {@link #toString()} which
     *         when applied changes the current instance and returning
     *         <code><b>this</b></code> */
    public <A1, A2, A3> Binder3<T, A1, A2, A3> bind(final Function3<T, A1, A2, A3> a) {
      return (¢1, ¢2, ¢3) -> ϑ(() -> a.ϑ(¢1.¢(), ¢2.¢(), ¢3.¢()), ¢1, ¢2, ¢3);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking four arguments
     * @param <A1> 1st argument's type
     * @param <A2> 2nd argument's type
     * @param <A3> 3rd argument's type
     * @param <A4> 4th argument's type
     * @param a a one argument function that returns a new value for this
     *        instance
     * @return a function with four arguments named {@link #toString()} which
     *         when applied changes the current instance and returning
     *         <code><b>this</b></code> */
    public <A1, A2, A3, A4> Binder4<T, A1, A2, A3, A4> bind(final Function4<T, A1, A2, A3, A4> a) {
      return (¢1, ¢2, ¢3, ¢4) -> ϑ(() -> a.ϑ(¢1.¢(), ¢2.¢(), ¢3.¢(), ¢4.¢()), ¢1, ¢2, ¢3, ¢4);
    }
    /** Used for fluent API; sets the current value of this instance to a be a
     * function taking one argument
     * @param <A> argument's type
     * @param ¢ a one argument function that returns a new value for this
     *        instance
     * @return a function with one argument named {@link Binder#to(Object)}
     *         which when applied
     *         <ol>
     *         <li>changes the current instance
     *         <li>returns <code><b>this</b></code>
     *         </ol>
    */
    public Property<T> bind2(final Function0<T> ¢) {
      this.ϑ = (Function0<T>) () -> ¢.¢();
      prerequisites.clear();
      ingredients(this);
      return this;
    }
    /** @return last value computed or set for this instance. */
    public final T cache() {
      return cache;
    }
    @Override @SuppressWarnings("unchecked") public Property<T> clone() {
      try {
        return (Property<T>) super.clone();
      } catch (final CloneNotSupportedException ¢) {
        throw new RuntimeException(¢);
      }
    }
    public void freeze() {
      frozen = true;
    }
    public T freezeGet() {
      freeze();
      final T $ = get();
      melt();
      return $;
    }
    /** @return current value stored in this instance, recomputed if
     *         necessary */
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
    public void melt() {
      frozen = false;
    }
    /** Used for fluent API; sets the current value of this instance
     * @param ¢ a no-arguments function that returns a value for this instance
     * @return <code><b>this</b></code> */
    public Property<T> of(final Function0<T> ¢) {
      return ϑ(¢);
    }
    /** Used for fluent API; sets the current value of this instance
     * @param ¢ JD
     * @return <code><b>this</b></code>* */
    public Property<T> of(final T ¢) {
      cache(¢);
      ϑ = null;
      version = latestDependentVersion() + 1;
      prerequisites.clear();
      return this;
    }
    public Property<T> push(@SuppressWarnings("unused") final Function0<T> __) {
      return this;
    }
    public Property<T> push(@SuppressWarnings("unused") final T __) {
      return this;
    }
    /** forcibly set the value stored in this instance, ignoring the function
     * used for computing it, and marks this instance as updated with respect to
     * all prerequisites.
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    public T set(final T ¢) {
      version = latestPrequisiteVersion() + 1;
      return cache(¢);
    }
    /** puts this instance in an undefined state
     * @return <code><b>this</b></code> */
    public Property<T> undefine() {
      cache(null);
      return this;
    }
    public void update() {
      if (frozen || updated())
        return;
      frozen = true;
      for (final Property<?> ¢ : prerequisites)
        ¢.update();
      version = latestPrequisiteVersion() + 1;
      assert ϑ != null;
      try {
        assert set(ϑ.¢()) != null;
      } catch (final Exception ¢) {
        ¢.printStackTrace();
        undefine();
      }
      frozen = false;
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
    /** @return version of this instance */
    public long version() {
      return version;
    }
    T cache(@SuppressWarnings("hiding") final T cache) {
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
     * @return <code><b>this</b></code> */
    Property<T> ϑ(@SuppressWarnings("hiding") final Function0<T> ϑ, final Property<?>... cs) {
      this.ϑ = ϑ;
      prerequisites.clear();
      ingredients(cs);
      version = 0;
      return this;
    }
  }
}