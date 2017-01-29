package il.org.spartan.spartanizer.ast.safety;

/** TODO Yossi Gil: document class {@link }
 * @param <R>
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-29 */
public abstract class Reducer<R> {
  public R reduce() {
    return null;
  }

  public final R reduce(final R ¢) {
    return ¢;
  }

  public abstract R reduce(R t1, R t2);

  public final R reduce(final R[] ts) {
    R $ = reduce();
    for (final R ¢ : ts)
      $ = reduce($, ¢);
    return $;
  }

  @SafeVarargs public final R reduce(final R t1, final R t2, final R... ts) {
    R $ = reduce(t1, t2);
    for (final R ¢ : ts)
      $ = reduce($, ¢);
    return $;
  }
}
