package il.org.spartan.utils;

import java.util.*;

/** Reducer for merging List<T>
 * @author oran1248
 * @since 2017-04-20 */
public class ListMergingReducer<T> extends ReduceCollectionsAdd<T, List<T>> {
  @Override public List<T> reduce() {
    return new LinkedList<>();
  }
}
