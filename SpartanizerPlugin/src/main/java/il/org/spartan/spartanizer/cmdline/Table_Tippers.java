package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class Table_Tippers {
  public static void main(final String[] args) {
    new Table_Tippers().go();
  }

  public void go() {
    try (Relation r = new Relation(this)) {
      for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
        if (Toolbox.defaultInstance().implementation[i] != null)
          for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
            if (¢ != null)
              r //
                  .put("Category", ¢.tipperGroup())//
                  .put("Tipper", ¢.getClass().getSimpleName())//
                  .put("Node Type Number", i + "") //
                  .put("Node Class", Toolbox.intToClassName(i))//
                  .put("Actual class", name(¢.myActualOperandsClass()))//
                  .put("Abstract class", name(¢.myAbstractOperandsClass()))//
                  .nl();
      System.err.println(r.description());
    }
  }

  public static String name(final Class<?> ¢) {
    return ¢ == null ? "???" : ¢.getSimpleName();
  }
}
