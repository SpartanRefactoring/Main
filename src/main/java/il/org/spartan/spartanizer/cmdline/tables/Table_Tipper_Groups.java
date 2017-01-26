package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class Table_Tipper_Groups {
  public static void main(final String[] args) {
    new Table_Tipper_Groups().go();
  }

  public void go() {
    final Map<String, Integer> categories = new TreeMap<>();
    for (final List<Tipper<? extends ASTNode>> element : Toolbox.defaultInstance().implementation)
      if (element != null)
        for (final Tipper<?> ¢ : element)
          if (¢ != null) {
            final String key = ¢.tipperGroup() + "";
            categories.putIfAbsent(key, box.it(0));
            categories.put(key, box.it(categories.get(key).intValue() + 1));
          }
    final int total = categories.values().stream().reduce((x, y) -> box.it(x.intValue() + y.intValue())).get().intValue();
    try (Table r = new Table(this)) {
      categories.keySet()
          .forEach(λ -> r//
              .col("Category", λ)//
              .col("Count", categories.get(λ))//
              .col("Fraction", 1. * categories.get(λ).intValue() / total).nl());
      System.err.println(r.description());
    }
    system.dumpOutput(system.bash("column -s \\& -t /tmp/groups.tex"));
  }
}
