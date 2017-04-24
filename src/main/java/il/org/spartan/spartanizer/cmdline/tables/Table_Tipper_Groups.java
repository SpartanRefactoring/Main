package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class Table_Tipper_Groups {
  public static void main(final String[] args) {
    new Table_Tipper_Groups().go();
  }

  public void go() {
    final Map<TipperGroup, Integer> groups = new TreeMap<>();
    Stream.of(Configurations.all().implementation)//
        .filter(λ -> λ != null && !λ.isEmpty())//
        .forEach(ts -> flow(ts).forEach(λ -> inc(groups, λ)));
    final int total = groups.values().stream().mapToInt(Integer::intValue).reduce((x, y) -> (x + y)).getAsInt();
    try (Table t = new Table(this)) {
      groups.keySet()
          .forEach(λ -> t//
              .col("Category", λ)//
              .col("Count", groups.get(λ))//
              .col("Fraction", 1. * groups.get(λ).intValue() / total).nl());
      System.err.println(t.description());
    }
    system.dumpOutput(system.bash("column -s \\& -t /tmp/groups.tex"));
  }

  private static void inc(final Map<TipperGroup, Integer> categories, final TipperCategory λ) {
    final TipperGroup key = λ.tipperGroup();
    categories.putIfAbsent(key, nano.ly.box.it(0));
    categories.put(key, nano.ly.box.it(categories.get(key).intValue() + 1));
  }

  private static <T> Stream<T> flow(final Collection<T> ¢) {
    return ¢.stream().filter(Objects::nonNull);
  }
}
