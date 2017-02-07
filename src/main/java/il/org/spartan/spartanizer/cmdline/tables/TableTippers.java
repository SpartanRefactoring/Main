package il.org.spartan.spartanizer.cmdline.tables;

import il.org.spartan.spartanizer.ast.navigate.*;
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
    try (Table r = new Table(this)) {
      for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
        if (Toolbox.defaultInstance().implementation[i] != null)
          for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
            if (¢ != null)
              r//
                  .col("Category", ¢.tipperGroup())//
                  .col("Tipper", ¢.getClass().getSimpleName())//
                  .col("Node Type Number", i) //
                  .col("Node Class", Toolbox.intToClassName(i))//
                  .col("Actual class", wizard.nodeName(¢.myActualOperandsClass()))//
                  .col("Abstract class", wizard.nodeName(¢.myAbstractOperandsClass())) //
                  .nl();
      System.err.println("Output found in " + r.description());
    }
  }
}
