/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.azzert.*;

import java.util.function.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

/**
 * An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> utility functions of reasonably wide use.
 *
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>
 * @since 2013/07/01
 */
public interface idiomatic {
  ;
  /**
   * TODO Javadoc(2016): automatically generated for method <code>yield</code>
   *
   * @param <T> JD
   * @param t JD
   * @return Yielder<T> TODO Javadoc(2016) automatically generated for returned
   *         value of method <code>yield</code>
   */
  public static <T> Storer<T> take(final T t) {
    return new Storer<T>(t);
  }
  /**
   * TODO Javadoc(2016): automatically generated for method <code>yield</code>
   *
   * @param <T> JD
   * @param t JD
   * @return Yielder<T> TODO Javadoc(2016) automatically generated for returned
   *         value of method <code>yield</code>
   */
  static <T> Whener<T> eval(final Supplier<@Nullable T> t) {
    return () -> t.get();
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static <T> @Nullable T incase(final boolean condition, final T t) {
    return condition ? t : null;
  }
  /**
   * Quote a given {@link String}
   *
   * @param $ some {@link String} to be quoted
   * @return the parameter, quoted
   */
  public static String quote(final @Nullable String $) {
    return $ != null ? QUOTE + $ + QUOTE : "<null reference>";
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static Trigger unless(final boolean condition) {
    return when(!condition);
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static <T> @Nullable T unless(final boolean condition, final T t) {
    return incase(!condition, t);
  }
  /**
   * @param condition JD
   * @return TODO document return type
   */
  public static Trigger when(final boolean condition) {
    return condition ? eval : ignore;
  }

  /** Single quote: */
  public static final String QUOTE = "'";
  /** an evaluating trigger */
  static final Trigger eval = new Trigger() {
    @Override public <@Nullable T> T eval(final Supplier<T> __) {
      return null;
    }
  };
  /** an ignoring trigger */
  static final Trigger ignore = new Trigger() {
    @Override public <@Nullable T> T eval(final Supplier<T> __) {
      return null;
    }
  };

  /**
   * Supplier with {@link #when(boolean)} method
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  /**
   * TODO(2016) Javadoc: automatically generated for type <code>idiomatic</code>
   *
   * @param <T>
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  //  abstract class Whener<T> implements Supplier<T> {
  interface Whener<T> extends Supplier<T> {
    /**
     * Return value when condition is <code><b>true</b></code>
     *
     * @param unless condition on which value is returned
     * @return the {@link #get()} when the parameter is <code><b>true</b></code>,
     *         otherwise code><b>null</b></code>.
     */
    default @Nullable T unless(final boolean unless) {
      return when(!unless);
    }
    /**
     * Return value when condition is <code><b>true</b></code>
     *
     * @return the {@link #get()} when the parameter is <code><b>true</b></code>,
     *         otherwise code><b>null</b></code>.
     * @param when condition on which value is returned
     */
    default @Nullable T when(final boolean when) {
      return when ? get() : null;
    }
  }

  /**
   * Store a value to be returned with {@link #get()} function
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  static class Storer<T> implements Whener<T> {
    /**
     * Instantiates this class.
     *
     * @param inner JD
     */
    public Storer(final T inner) {
      this.inner = inner;
    }
    /** see @see java.util.function.Supplier#get() (auto-generated) */
    @Override public T get() {
      return inner;
    }

    /** */
    public final T inner;
  }

  /**
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  interface Trigger {
    /**
     * @param <T> JD
     * @param t JD
     * @return TODO document return type
     */
    <@Nullable T> T eval(final Supplier<T> t);
    /**
     * @param <T> JD
     * @param t JD
     * @return TODO document return type
     */
    default <@Nullable T> T eval(final T t) {
      return eval(() -> t);
    }
  }

  @SuppressWarnings("javadoc") public static class ZZZ {
    @Test public void use0() {
      azzert.notNull(new Storer<ZZZ>(this));
    }
    @Test public void use1() {
      azzert.notNull(new Storer<ZZZ>(this));
      new Storer<ZZZ>(this).when(true);
    }
    @Test public void use2() {
      azzert.notNull(take(this));
      azzert.isNull(take(this).when(false));
    }
    @Test public void use3() {
      azzert.that(take(this).when(true), is(this));
    }
  }
}
