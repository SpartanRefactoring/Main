// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static fluent.ly.___.*;

import java.io.*;
import java.util.*;

import fluent.ly.*;
import il.org.spartan.streotypes.*;

/** A general purpose implementation of the <b>Memento Design Pattern</b> using
 * object serialization
 * @author Yossi Gil, the Technion.
 * @since 24/07/2008
 * @param <T> type of object to be saved, must be {@link Serializable} */
@Instantiable
public class Memento<T> {
  /** An object's snapshot as obtained by serialization */
  private final byte[] snapshot;

  /** Instantiate the class with a snapshot of the given object
   * @param t a potentially-<code><b>null</b></code> reference to a
   *        {@link Serializable} object to store
   * @throws Bug.Contract.Precondition in case object could not be serialized */
  public Memento(final T t) {
    snapshot = object2bytes(t);
  }
  /** Restore the saved snapshot
   * @return a copy of the saved object */
  public T restore() {
    try {
      @SuppressWarnings("unchecked") final T $ = (T) new ObjectInputStream(new ByteArrayInputStream(snapshot)).readObject();
      return $;
    } catch (final ClassNotFoundException | IOException ¢) {
      unreachable(¢.getMessage());
      throw new RuntimeException(¢);
    }
  }
  /** Determine whether a given object is equal to the saved snapshot
   * @param ¢ an object to compare to the saved snapshot
   * @return <code><b>true</b></code>, <i>iff</i> the given object is deeply
   *         equal to the snapshot
   * @throws Bug.Contract.Precondition case object could not be serialized */
  public boolean same(final T ¢) {
    return Arrays.equals(snapshot, object2bytes(¢));
  }
  /** Determine the size in bytes of the recorded snapshot
   * @return a non-negative integer representing the size of the object in
   *         bytes */
  public int size() {
    return snapshot.length;
  }
  /** Convert an object to a byte array
   * @param t an object to convert
   * @return a byte array snapshot of the parameter as obtained by serializing
   *         it
   * @throws Bug.Contract.Precondition in case object could not be serialized */
  private byte[] object2bytes(final T t) {
    try {
      final ByteArrayOutputStream $ = new ByteArrayOutputStream();
      new ObjectOutputStream($).writeObject(t);
      return $.toByteArray();
    } catch (final NotSerializableException ¢) {
      dump.of(¢);
      throw new Bug.Contract.Precondition("Cannot serialize object of class " + ¢.getMessage());
    } catch (final IOException ¢) {
      unreachable(¢.getMessage());
      throw new RuntimeException(¢);
    }
  }
}
