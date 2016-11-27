package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

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
    int n = 0;
    final Map<String, Integer> categories = new TreeMap<>();
    final CSVLineWriter w = new CSVLineWriter("/tmp/" + this.getClass().getSimpleName() + "." + now());
    for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
      if (Toolbox.defaultInstance().implementation[i] != null)
        for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
          if (¢ != null) {
            w//
                .put("N", ++n)//
                .put("Category", ¢.tipperGroup())//
                .put("Tipper", ¢.getClass().getSimpleName())//
                .put("Node Type Number", i) //
                .put("Node Class", intToClassName(i))//
                .put("Actual class", name(¢.myActualOperandsClass()))//
                .put("Abstract class", name(¢.myAbstractOperandsClass()));
            w.nl();
            final String key = ¢.tipperGroup() + "";
            categories.putIfAbsent(key, box.it(0));
            categories.put(key, box.it(categories.get(key).intValue() + 1));
          }
    System.err.println(n + " lines input can be found in " + w.close());
    final CSVLineWriter c = new CSVLineWriter("/tmp/" + this.getClass().getSimpleName() + ".categories." + now());
    for (final String ¢ : categories.keySet()) {
      c.put("Category", ¢);
      c.put("Count", categories.get(¢));
      c.nl();
    }
    System.err.println(categories.size() + " lines input can be found in " + c.close());
  }

  public static String now() {
    return (new Date() + "").replaceAll(" ", "-");
  }

  /** @param i
   * @return */
  private static String intToClassName(final int i) {
    try {
      return name(ASTNode.nodeClassForType(i));
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return "???";
    }
  }

  /** @param myActualOperandsClass
   * @return */
  private static String name(final Class<?> ¢) {
    return ¢ == null ? "???" : ¢.getSimpleName();
  }
}
