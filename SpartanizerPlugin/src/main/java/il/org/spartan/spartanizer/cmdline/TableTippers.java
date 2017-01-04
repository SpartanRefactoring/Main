package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class TableTippers {
  public static void main(final String[] args) {
    new TableTippers().go();
  }

  public void go() {
    try (final Table r = new Table(getClass().getSimpleName().toLowerCase())) {
      for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
        if (Toolbox.defaultInstance().implementation[i] != null)
          for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
            if (¢ != null) {
              r//
                  .col("Category", ¢.tipperGroup())//
                  .col("Tipper", ¢.getClass().getSimpleName())//
                  .col("Node Type Number", i) //
                  .col("Node Class", intToClassName(i))//
                  .col("Actual class", name(¢.myActualOperandsClass()))//
                  .col("Abstract class", name(¢.myAbstractOperandsClass()));
              r.nl();
            }
      System.err.println("Output found in " + r.description());
    }
  }

  /** @param i
   * @return */
  protected static String intToClassName(final int $) {
    try {
      return name(ASTNode.nodeClassForType($));
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return "???";
    }
  }

  /** @param myActualOperandsClass
   * @return */
  protected static String name(final Class<?> ¢) {
    return ¢ == null ? "???" : ¢.getSimpleName();
  }
}
