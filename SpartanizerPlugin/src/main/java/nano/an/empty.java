package nano.an;

import java.util.*;

/** Empty collections.
 * @author Ori Roth
 * @since 2017-04-16 */
public interface empty {
  static <T> List<T> list() {
    return new LinkedList<>();
  }
}
