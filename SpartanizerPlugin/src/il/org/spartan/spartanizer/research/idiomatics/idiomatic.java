package il.org.spartan.spartanizer.research.idiomatics;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;

/** An empty {@code enum} with a variety of {@code public
 * static} utility functions of reasonably wide use.
 * @author Yossi Gil
 * @since 2013/07/01
 * @author Ori Marcovitch
 * @since 20/10/2016 */
public interface idiomatic {
  /** Single quote: */
  String QUOTE = "'";
  /** an evaluating trigger */
  Trigger eval = new Trigger() {
    @Override public <T> T eval(final Supplier<T> ¢) {
      return ¢.get();
    }
  };
  /** an ignoring trigger */
  Trigger tIgnore = new Trigger() {
    @Override public <T> T eval(@SuppressWarnings("unused") final Supplier<T> __) {
      return null;
    }
  };

  static void addImport(final CompilationUnit u, final ASTRewrite r) {
    final ImportDeclaration d = u.getAST().newImportDeclaration();
    d.setStatic(true);
    d.setOnDemand(true);
    d.setName(u.getAST().newName("il.org.spartan.spartanizer.research.idiomatic"));
    misc.addImport(u, r, d);
  }

  /** @param <T> JD
   * @param $ result
   * @return an identical supplier which is also a {@link Holder} */
  static <T> Holder<T> eval(final Supplier<T> $) {
    return $::get;
  }

  /* @param condition JD
   *
   * @return */
  /** {@code incase}
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
   * @return result of invoking the parameter, or {@code null if an
   *         exception occurred. */
  static <T> T katching(final Producer<T> $) {
    try {
      return $.λ();
    } catch (final Exception ¢) {
      return note.bug(¢);
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

  static <T> Storer<T> take(final T ¢) {
    return new Storer<>(¢);
  }

  static Trigger unless(final boolean condition) {
    return then(!condition);
  }

  /** @param <T> JD
   * @param condition when should the misc take place
   * @param t JD
   * @return non-boolean parameter, in case the boolean parameter is true, or
   *         null, otherwise */
  static <T> T unless(final boolean condition, final T t) {
    return incase(!condition, t);
  }

  class ObjectHolder<T> {
    final T t;

    public ObjectHolder(final T t) {
      this.t = t;
    }

    public ConditionHolder nulls() {
      return new ConditionHolder(t == null);
    }
  }

  class ConditionHolder {
    final boolean b;

    public ConditionHolder(final boolean b) {
      this.b = b;
    }

    public <T> SupplierHolder<T> eval(final Supplier<T> ¢) {
      return new SupplierHolder<>(¢, b);
    }
  }

  class SupplierHolder<T> {
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
  static Trigger then(final boolean condition) {
    return condition ? eval : tIgnore;
  }

  /** Like eval.when but returning void
   * @author Ori Marcovitch
   * @since 2016 */
  abstract class Executor {
    public abstract <T> void when(boolean c);
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
  @FunctionalInterface
  interface Holder<T> extends Supplier<T> {
    /** Return value when condition is {@code true}
     * @param unless condition on which value is returned
     * @return {@link #get()} when the parameter is {@code true} , otherwise
     *         {@code null. */
    default T unless(final boolean unless) {
      return when(!unless);
    }

    /** Return value when condition is {@code true}
     * @return {@link #get()} when the parameter is {@code true} , otherwise
     *         {@code null.
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
  @FunctionalInterface
  interface Producer<T> {
    /** @return next value provided by this instance
     * @throws Exception JD */
    T λ();
  }

  /** Evaluate a {@link Runnable} when a condition applies or unless a condition
   * applies.
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  class Runner implements Runnable {
    private final Runnable run;

    /** Instantiates this class.
     * @param run JD */
    Runner(final Runnable run) {
      this.run = run;
    }

    @Override public void run() {
      run.run();
    }

    /** {@code unless}
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
  class Storer<T> implements Holder<T> {
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

  /** @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  interface Trigger {
    /** @param <T> JD
     * @param t JD
     * @return */
    <T> T eval(Supplier<T> t);

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
      collection.forEach(mapper);
    }

    @SuppressWarnings("unchecked") public <R, CR extends Collection<R>> CR map(final Function<? super T, ? extends R> mapper) {
      return (CR) collection.stream().map(mapper).collect(new GenericCollector<R>(collection.getClass()));
    }

    @SuppressWarnings("unchecked") public CT filter(final Predicate<? super T> mapper) {
      return (CT) collection.stream().filter(mapper).collect(new GenericCollector<>(collection.getClass()));
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
      return (CR) ¢.stream().map(mapper).collect(new GenericCollector<>(¢.getClass()));
    }
    // @SuppressWarnings("boxing") @Test public void useNewMapper() {
    // final List<Integer> before = an.empty.list();
    // before.add(1);
    // before.add(2);
    // before.add(3);
    // final List<String> after = mapp(x -> mapper(x)).apply(before);
    // wizard.assertEquals("1", after.get(0));
    // wizard.assertEquals("2", after.get(1));
    // wizard.assertEquals("3", after.get(2));
    // }
  }

  /** A class to collect collections without need to specify their __. should
   * work for most of common collections.
   * @author Ori Marcovitch
   * @since 2016 */
  @SuppressWarnings("rawtypes")
  class GenericCollector<R> implements Collector<R, Collection<R>, Collection<R>> {
    private final Class<? extends Collection> cls;

    public GenericCollector(final Class<? extends Collection> cls) {
      this.cls = cls;
    }

    @SuppressWarnings("unchecked") private <I> Function<I, Collection<R>> castingIdentity() {
      return λ -> (Collection<R>) λ;
    }

    @Override @SuppressWarnings("unchecked") public Supplier<Collection<R>> supplier() {
      return () -> {
        try {
          return cls.getConstructor().newInstance();
        } catch (final Exception $) {
          return note.bug($);
        }
      };
    }

    @Override public BiConsumer<Collection<R>, R> accumulator() {
      return Collection::add;
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
