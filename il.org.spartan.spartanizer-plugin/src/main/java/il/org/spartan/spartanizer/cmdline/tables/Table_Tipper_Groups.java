package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;
import java.util.stream.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.spartanizer.traversal.*;
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
    final Map<Taxon, Integer> groups = new TreeMap<>();
    Stream.of(Toolboxes.all().implementation)//
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
  private static void inc(final Map<Taxon, Integer> m, final Category λ) {
    final Taxon key = λ.tipperGroup();
    m.putIfAbsent(key, box.it(0));
    m.put(key, box.it(m.get(key).intValue() + 1));
  }
  private static <T> Stream<T> flow(final Collection<T> ¢) {
    return ¢.stream().filter(Objects::nonNull);
  }
}
