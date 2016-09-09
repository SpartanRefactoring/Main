package il.org.spartan.iteration.closures;

/** An interface supplying a function to convert objects of one kind into
 * objects of another. This is realized by an "object function pointer". To
 * create such a pointer, create a subclass that implements this interface
 * (typically as an anonymous class), giving an implementation to function
 * {@link #_}, and then pass an instance of this subclass class.
 * @author Yossi Gil.
 * @param <F> type of values that the function takes
 * @param <T> type of values that the function returns */
public interface Converter<F, T> {
  T _(F f);

  public class Identity<T> implements Converter<T, T> {
    @Override public T _(final T t) {
      return t;
    }
  }
}
