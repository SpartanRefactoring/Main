package il.org.spartan.java.temporal;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
public interface Operation {
  default void go() {/**/}
  default boolean registerNonCollateral(@SuppressWarnings("unused") Operation o) {
    return false;
  }
}
