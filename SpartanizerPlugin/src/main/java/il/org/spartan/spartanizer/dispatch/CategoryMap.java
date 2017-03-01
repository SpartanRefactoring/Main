/**
 * 
 */
package il.org.spartan.spartanizer.dispatch;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.PreferencesResources.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-02-22 */
public class CategoryMap {
  public static void main(final String[] args) {
    final Toolbox t = Toolbox.freshCopyOfAllTippers();
    System.out.printf("Currently, there are a total of %d tippers offered on %d classes", box.it(t.tippersCount()), box.it(t.nodesTypeCount()));
  }
  
  public static TipperGroup groupFor(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }

  @SuppressWarnings("rawtypes") private final static Map<Class<? extends Tipper>, TipperGroup> categoryMap = new HashMap<>();

  @SuppressWarnings("rawtypes") public static Map<Class<? extends Tipper>, TipperGroup> getCategoryMap() {
    if (categoryMap.isEmpty()) {
      final Toolbox t = Toolbox.freshCopyOfAllTippers();
      Stream.of(t.implementation).filter(Objects::nonNull).forEach(ts -> ts.forEach(λ -> categoryMap.put(λ.getClass(), λ.tipperGroup())));
    }
    return categoryMap;
  }
}
