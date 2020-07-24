package il.org.spartan.collections;

import java.util.HashMap;

/** A map of {@link String} to the type <code><b>int</b></code>, similar to the
 * standard implementation of {@link HashMap} except that:
 * <ol>
 * <li>function {@link #put(String, int)} does not return the old value.
 * <li>the {@link #get(String)} function throws a null pointer exception if the
 * key is not present in the map.
 * <li>function {@link #contains(String)} is used to check if an element is in
 * the table.
 * </ol>
 *
 * @author Yossi Gil
 * @since 21/08/2007 */
public class StringIntMap extends HashMap<String, Integer> {
  private static final long serialVersionUID = 0x64C009D269E5556CL;

  public boolean contains(final String key) {
    return super.containsKey(key);
  }

  public int get(final String key) {
    return super.get(key).intValue();
  }

  public void put(final String key, final int value) {
    super.put(key, Integer.valueOf(value));
  }
}
