package il.org.spartan.spartanizer.ast.safety;

import java.util.stream.*;

/** TODO Yossi Gil: document class {@link }
 * @param <R>
<<<<<<< HEAD
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
=======
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
>>>>>>> branch 'master' of https://github.com/SpartanRefactoring/Spartanizer.git
 * @since 2017-01-29 */
public abstract class Reducer<R> {
  public R reduce() {
    return null;
  }

  public final R reduce(final R ¢) {
    return ¢;
  }

  public abstract R reduce(R t1, R t2);

  public final R reduce(final R[] rs) {
    return Stream.of(rs).reduce((¢, $) -> reduce($, ¢)).orElse(reduce());
  }

  @SafeVarargs public final R reduce(final R t1, final R t2, final R... rs) {
    return reduce(t1, reduce(t2, reduce(rs)));
  }
}
