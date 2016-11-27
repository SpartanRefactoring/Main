package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class TipperGroupsReport {
  public static void main(final String[] args) {
    new TipperGroupsReport().go();
  }

  public void go() {
    final Map<String, Integer> categories = new TreeMap<>();
    for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
      if (Toolbox.defaultInstance().implementation[i] != null)
        for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
          if (¢ != null) {
            final String key = ¢.tipperGroup() + "";
            categories.putIfAbsent(key, box.it(0));
            categories.put(key, box.it(categories.get(key).intValue() + 1));
          }
    final CSVLineWriter c = new CSVLineWriter("/tmp/" + this.getClass().getSimpleName() + ".categories." + system.now());
    int n = 0;
    for (final String ¢ : categories.keySet()) {
      c.put("N", ++n);
      c.put("Category", ¢);
      c.put("Count", categories.get(¢));
      c.nl();
    }
    System.err.println(categories.size() + " lines input can be found in " + c.close());
  }
}
