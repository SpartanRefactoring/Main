package il.org.spartan.utils.fluent;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-13 */
public interface de {
  static <T> To<T> fault(final T value) {
    return λ -> value == null ? λ : value;
  }

  interface To<T> {
    T to(T t);
  }
}
