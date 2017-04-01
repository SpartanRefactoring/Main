package il.org.spartan.utils;

import java.util.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-01 */
public enum empty {
  ;
  public static <T> List<T> list() {
    return new ArrayList<>();
  }
}
