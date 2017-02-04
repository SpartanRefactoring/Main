package il.org.spartan.spartanizer.cmdline.tables;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class Table_Tippers {
  public static void main(final String[] args) {
    new Table_Tippers().go();
  }

  public void go() {
    try (Table r = new Table(this)) {
      for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
        if (Toolbox.defaultInstance().implementation[i] != null)
          for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
            if (¢ != null && !(¢ instanceof TipperCategory.Bloater))
              r //
                  .col("Category", ¢.tipperGroup())//
                  .col("Tipper", ¢.getClass().getSimpleName())//
                  .col("Node Type Number", i + "") //
                  .col("Node Class", Toolbox.intToClassName(i))//
                  .col("Actual class", name(¢.myActualOperandsClass()))//
                  .col("Abstract class", name(¢.myAbstractOperandsClass()))//
                  .nl();
      System.err.println(r.description());
    }
  }

  @NotNull public static String name(@Nullable final Class<?> ¢) {
    return ¢ == null ? "???" : ¢.getSimpleName();
  }
}
