package an;

import java.util.ArrayList;
import java.util.List;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-01 */
public enum empty {
  ;
  public static <T> List<T> list() {
    return new ArrayList<>();
  }
}
