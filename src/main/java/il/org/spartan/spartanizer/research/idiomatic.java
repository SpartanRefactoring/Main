package il.org.spartan.spartanizer.research;

import static il.org.spartan.azzert.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> utility functions of reasonably wide use.
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>
 * @since 2013/07/01
 * @author Ori Marcovitch
 * @since 20/10/2016 */
public interface idiomatic {
  /** Single quote: */
  final String QUOTE = "'";
  /** an evaluating trigger */
  final Trigger eval = new Trigger() {
    @Override public <T> T eval(final Supplier<T> ¢) {
      return ¢.get();
    }
  };
  /** an ignoring trigger */
  final Trigger tIgnore = new Trigger() {
    @Override public <T> T eval(@SuppressWarnings("unused") final Supplier<T> __) {
      return null;
    }
  };

  static void addImport(final CompilationUnit u, final ASTRewrite r) {
    final ImportDeclaration d = u.getAST().newImportDeclaration();
    d.setStatic(true);
    d.setOnDemand(true);
    d.setName(u.getAST().newName("il.org.spartan.spartanizer.research.idiomatic"));
    wizard.addImport(u, r, d);
  }
  /** @param <T> JD
   * @param $ result
   * @return an identical supplier which is also a {@link Holder} */
  static <T> Holder<T> eval(final Supplier<T> $) {
    return () -> $.get();
  }
  /** @param condition JD
   * @return */
  /** <code>incase</code>
   * @param <T> JD
   * @param condition
   * @param t JD
   * @return T */
  static <T> T incase(final boolean condition, final T t) {
    return condition ? t : null;
  }
  /** A filter, which prints an appropriate log message and returns null in case
   * of {@link Exception} thrown by {@link Producer#λ()}
   * @param <T> JD
   * @param $ JD
   * @return result of invoking the parameter, or <code><b>null</b></code> if an
   *         exception occurred. */
  static <T> T katching(final Producer<T> $) {
    try {
      return $.λ();
    } catch (final Exception ¢) {
      ¢.printStackTrace();
      return null;
    }
  }
  /** Quote a given {@link String}
   * @param $ some {@link String} to be quoted
   * @return parameter, quoted */
  static String quote(final String $) {
    return $ != null ? QUOTE + $ + QUOTE : "<null reference>";
  }
  /** @param ¢ JD
   * @return an identical runnable which is also a {@link Runner} */
  static Runner run(final Runnable ¢) {
    return new Runner(¢);
  }
  /** @param <T> JD
   * @param ¢ JD
   * @return Yielder<T> */
  static <T> Storer<T> take(final T ¢) {
    return new Storer<>(¢);
  }
  /** @param condition JD
   * @return */
  static Trigger unless(final boolean condition) {
    return vhen(!condition);
  }
  /** @param <T> JD
   * @param condition when should the action take place
   * @param t JD
   * @return non-boolean parameter, in case the boolean parameter is true, or
   *         null, otherwise */
  static <T> T unless(final boolean condition, final T t) {
    return incase(!condition, t);
  }

  static class ObjectHolder<T> {
    T t;

    public ObjectHolder(final T t) {
      this.t = t;
    }
    public ConditionHolder nulls() {
      return new ConditionHolder(t == null);
    }
  }

  static class ConditionHolder {
    final boolean b;

    public ConditionHolder(final boolean b) {
      this.b = b;
    }
    public <T> SupplierHolder<T> eval(final Supplier<T> ¢) {
      return new SupplierHolder<>(¢, b);
    }
  }

  static class SupplierHolder<T> {
    final Supplier<T> s;
    final boolean b;

    public SupplierHolder(final Supplier<T> ¢, final boolean b) {
      s = ¢;
      this.b = b;
    }
    public T elze(final Supplier<T> ¢) {
      return (b ? s : ¢).get();
    }
  }

  static <T> ObjectHolder<T> when(final T ¢) {
    return new ObjectHolder<>(¢);
  }
  static ConditionHolder when(final boolean ¢) {
    return new ConditionHolder(¢);
  }
  /** @param condition JD
   * @return */
  static Trigger vhen(final boolean condition) {
    return condition ? eval : tIgnore;
  }

  /** Like eval.when but returning void
   * @author Ori Marcovitch
   * @since 2016 */
  abstract static class Executor {
    public abstract <T> void when(final boolean c);
  }

  static Executor execute(final Runnable r) {
    return new Executor() {
      final Runnable runnable = r;

      @Override public void when(final boolean condition) {
        if (condition)
          runnable.run();
      }
    };
  }
  static <T> Storer<T> default¢(final T ¢) {
    return new Storer<>(¢);
  }

  /** Supplier with {@link #when(boolean)} method
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  interface Holder<T> extends Supplier<T> {
    /** Return value when condition is <code><b>true</b></code>
     * @param unless condition on which value is returned
     * @return {@link #get()} when the parameter is <code><b>true</b></code> ,
     *         otherwise code><b>null</b></code>. */
    default T unless(final boolean unless) {
      return when(!unless);
    }
    /** Return value when condition is <code><b>true</b></code>
     * @return {@link #get()} when the parameter is <code><b>true</b></code> ,
     *         otherwise code><b>null</b></code>.
     * @param when condition on which value is returned */
    default T when(final boolean when) {
      return when ? get() : null;
    }
    default T to(final T ¢) {
      return get() == null ? ¢ : get();
    }
  }

  /** A class which is just like {@link Supplier} , except that it uses the
   * shorter name ( {@link #λ()} and that it allows for {@link Exception} s to
   * be thrown by the getters.
   * @author Yossi Gil
   * @param <T> JD
   * @since 2016` */
  @FunctionalInterface interface Producer<T> {
    /** @return next value provided by this instance
     * @throws Exception JD */
    T λ() throws Exception;
  }

  /** Evaluate a {@link Runnable} when a condition applies or unless a condition
   * applies.
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  static class Runner implements Runnable {
    private final Runnable run;

    /** Instantiates this class.
     * @param run JD */
    Runner(final Runnable run) {
      this.run = run;
    }
    @Override public void run() {
      run.run();
    }
    /** <code>unless</code>
     * @param unless condition n which execution occurs. */
    public void unless(final boolean unless) {
      when(!unless);
    }
    void when(final boolean when) {
      if (when)
        run();
    }
  }

  /** Store a value to be returned with {@link #get()} function
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  static class Storer<T> implements Holder<T> {
    /** */
    final T inner;

    /** Instantiates this class.
     * @param inner JD */
    Storer(final T inner) {
      this.inner = inner;
    }
    /** see @see java.util.function.Supplier#get() (auto-generated) */
    @Override public T get() {
      return inner;
    }
  }

  @SuppressWarnings({ "javadoc", "static-method" }) static class TEST {
    @Test public void use0() {
      azzert.notNull(new Storer<>(this));
    }
    @Test public void use08() {
      azzert.isNull(unless(true).eval(() -> new Object()));
    }
    @Test public void use09() {
      azzert.notNull(unless(false).eval(() -> new Object()));
    }
    @Test public void use1() {
      azzert.notNull(new Storer<>(this));
      new Storer<>(this).when(true);
    }
    @Test public void use10() {
      azzert.notNull(vhen(true).eval(() -> new Object()));
    }
    @Test public void use11() {
      azzert.isNull(vhen(false).eval(() -> new Object()));
    }
    @Test public void use2() {
      azzert.notNull(take(this));
      azzert.isNull(take(this).when(false));
    }
    @Test public void use3() {
      azzert.that(take(this).when(true), is(this));
    }
    @Test public void use4() {
      azzert.isNull(take(this).when(false));
    }
    @Test public void use5() {
      azzert.that(take(this).unless(false), is(this));
    }
    @Test public void use6() {
      azzert.isNull(take(this).unless(true));
    }
    @Test public void use7() {
      azzert.isNull(take(this).unless(true));
      azzert.isNull(take(null).unless(true));
      azzert.isNull(take(null).unless(false));
    }
    String mapper(final String ¢) {
      return ¢ + ¢;
    }
    String mapper(final Integer ¢) {
      return ¢ + "";
    }
    @Test public void useMapper() {
      final List<String> before = new ArrayList<>();
      before.add("1");
      before.add("2");
      before.add("3");
      final List<String> after = on(before).map(x -> mapper(x));
      assertEquals("11", after.get(0));
      assertEquals("22", after.get(1));
      assertEquals("33", after.get(2));
    }
    @SuppressWarnings("boxing") @Test public void useMapper2() {
      final List<Integer> before = new ArrayList<>();
      before.add(1);
      before.add(2);
      before.add(3);
      final List<String> after = on(before).map(x -> mapper(x));
      assertEquals("1", after.get(0));
      assertEquals("2", after.get(1));
      assertEquals("3", after.get(2));
    }
    @SuppressWarnings("boxing") @Test public void useFilter() {
      final List<Integer> before = new ArrayList<>();
      before.add(1);
      before.add(2);
      before.add(3);
      final List<Integer> after = on(before).filter(x -> x % 2 == 1);
      assertEquals(1, after.get(0).intValue());
      assertEquals(3, after.get(1).intValue());
    }
    @Test public void useReduce() {
      final List<String> before = new ArrayList<>();
      before.add("1");
      before.add("2");
      before.add("3");
      assertEquals("123", on(before).reduce((x, y) -> x + y));
    }
    @Test public void useMax() {
      final List<String> before = new ArrayList<>();
      before.add("1");
      before.add("2");
      before.add("3");
      assertEquals("3", on(before).max(String::compareTo));
    }
    @Test public void useMin() {
      final List<String> before = new ArrayList<>();
      before.add("1");
      before.add("2");
      before.add("3");
      assertEquals("1", on(before).min(String::compareTo));
    }
    @SuppressWarnings("boxing") @Test public void whenNullsEval() {
      final Object o = new Object();
      when(o).nulls().eval(() -> o.hashCode()).elze(() -> o.hashCode());
    }
  }

  /** @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  interface Trigger {
    /** @param <T> JD
     * @param t JD
     * @return */
    <T> T eval(final Supplier<T> t);
    /** @param <T> JD
     * @param $ JD
     * @return */
    default <T> T eval(final T $) {
      return eval(() -> $);
    }
  }

  //////////////////////////////////////////////////////
  /////////////////// Collections //////////////////////
  //////////////////////////////////////////////////////
  /** A class to hold a collection which on, operations can be made, like map,
   * apply, reduce, ...
   * @param ¢
   * @return */
  static <T, CT extends Collection<T>> CollectionHolder<T, CT> on(final CT ¢) {
    return new CollectionHolder<>(¢);
  }

  class CollectionHolder<T, CT extends Collection<T>> {
    final CT collection;

    public CollectionHolder(final CT collection) {
      this.collection = collection;
    }
    public void apply(final Consumer<? super T> mapper) {
      collection.stream().forEach(mapper);
    }
    @SuppressWarnings("unchecked") public <R, CR extends Collection<R>> CR map(final Function<? super T, ? extends R> mapper) {
      return (CR) collection.stream().map(mapper).collect(new GenericCollector<R>(collection.getClass()));
    }
    @SuppressWarnings("unchecked") public CT filter(final Predicate<? super T> mapper) {
      return (CT) collection.stream().filter(mapper).collect(new GenericCollector<T>(collection.getClass()));
    }
    public T reduce(final BinaryOperator<T> reducer) {
      return collection.stream().reduce(reducer).get();
    }
    public T max(final Comparator<? super T> comperator) {
      return collection.stream().max(comperator).get();
    }
    public T min(final Comparator<? super T> comperator) {
      return collection.stream().min(comperator).get();
    }
  }

  /** This is not good. java cannot infer types.
   * @param mapper
   * @return */
  static <T, R> MapperLambdaHolder<T, R> mapp(final Function<T, R> mapper) {
    return new MapperLambdaHolder<>(mapper);
  }

  class MapperLambdaHolder<T, R> {
    final Function<T, R> mapper;

    public MapperLambdaHolder(final Function<T, R> mapper) {
      this.mapper = mapper;
    }
    @SuppressWarnings("unchecked") public <CT extends Collection<T>, CR extends Collection<R>> CR to(final CT ¢) {
      return (CR) ¢.stream().map(mapper).collect(new GenericCollector<R>(¢.getClass()));
    }
    // @SuppressWarnings("boxing") @Test public void useNewMapper() {
    // final List<Integer> before = new ArrayList<>();
    // before.add(1);
    // before.add(2);
    // before.add(3);
    // final List<String> after = mapp(x -> mapper(x)).apply(before);
    // assertEquals("1", after.get(0));
    // assertEquals("2", after.get(1));
    // assertEquals("3", after.get(2));
    // }
  }

  /** A class to collect collections without need to specify their type. should
   * work for most of common collections.
   * @author Ori Marcovitch
   * @since 2016 */
  @SuppressWarnings("rawtypes") class GenericCollector<R> implements Collector<R, Collection<R>, Collection<R>> {
    private final Class<? extends Collection> cls;

    public GenericCollector(final Class<? extends Collection> cls) {
      this.cls = cls;
    }
    @SuppressWarnings("unchecked") private <I> Function<I, Collection<R>> castingIdentity() {
      return i -> (Collection<R>) i;
    }
    @SuppressWarnings("unchecked") @Override public Supplier<Collection<R>> supplier() {
      return () -> {
        try {
          return cls.getConstructor().newInstance();
        } catch (final Exception x) {
          x.printStackTrace();
        }
        return null;
      };
    }
    @Override public BiConsumer<Collection<R>, R> accumulator() {
      return (c, t) -> c.add(t);
    }
    @Override public BinaryOperator<Collection<R>> combiner() {
      return (left, right) -> {
        left.addAll(right);
        return left;
      };
    }
    @Override public Function<Collection<R>, Collection<R>> finisher() {
      return castingIdentity();
    }
    @Override public Set<Characteristics> characteristics() {
      return new HashSet<>();
    }
  }
}
