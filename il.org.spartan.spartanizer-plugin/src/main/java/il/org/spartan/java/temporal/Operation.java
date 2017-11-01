package il.org.spartan.java.temporal;

import java.util.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Operation {
  void go();
  default boolean isBefore(Operation o) {
    return false;
  }
  default boolean isAfter(Operation o) {
    return false;
  }
  default boolean isCollateral(Operation o) {
    return !isBefore(o) && !isAfter(o);
  }
  default List<Operation> knownBefores() {
    return an.empty.list();
  }
  default List<Operation> knownAfters() {
    return an.empty.list();
  }
  default List<Operation> knownCollaterals() {
    return an.empty.list();
  }
}
