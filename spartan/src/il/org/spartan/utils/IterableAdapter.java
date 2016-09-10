// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static il.org.spartan.utils.___.*;

import java.util.*;

import il.org.spartan.streotypes.*;

/** An {@link Iterable} interface to an {@link Enumeration} using the
 * <b>Adapter</b> design pattern.
 * @author Yossi Gil, the Technion.
 * @since 31/07/2008
 * @param <T> type of elements in the iterated collection */
@Instantiable @Canopy public final class IterableAdapter<T> implements Iterable<T> {
  /** A factory method, generating an {@link Iterable} from a given
   * {@link Enumeration}
   * @param <T> type of elements in the iterated collection
   * @param e an enumeration to convert into an {@link Iterable}
   * @return a new {@link Iterable} created from the parameter */
  public static <T> Iterable<T> make(final Enumeration<T> e) {
    return new IterableAdapter<>(e);
  }

  final Enumeration<T> implementation;

  /** Create an {@link Iterable} from a given enumeration
   * @param implmenetation an enumeration adapted by the newly created
   *        {@link Iterable} */
  public IterableAdapter(final Enumeration<T> implmenetation) {
    this.implementation = implmenetation;
  }

  /* Return an {@link Iterator} over the encapsulated {@link Enumeration}.
   *
   * @see java.lang.Iterable#iterator() */
  @Override public Iterator<T> iterator() {
    return new Iterator<T>() {
      @Override public boolean hasNext() { // An anonymous class realizing the
        // Iterator<T> protocol
        return implementation.hasMoreElements();
      }

      @Override public T next() {
        return implementation.nextElement();
      }

      @Override public void remove() {
        require(false, "cannot remove elements from an adapted enumeration");
      }
    };
  }
}
