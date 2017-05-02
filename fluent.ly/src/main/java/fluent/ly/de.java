package fluent.ly;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-13 */
public interface de {
  interface To<T> {
    T to(T t);
  }

  static <T> To<T> fault(final T value) {
    return λ -> value == null ? λ : value;
  }
}
