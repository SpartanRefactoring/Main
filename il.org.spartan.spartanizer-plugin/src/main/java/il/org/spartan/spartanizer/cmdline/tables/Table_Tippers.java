package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.spartanizer.traversal.*;
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
    try (Table t = new Table(this)) {
      final List<Tipper<? extends ASTNode>>[] implementation = Configurations.all().implementation;
      for (int i = 0; i < implementation.length; ++i)
        if (implementation[i] != null)
          for (final Tipper<?> ¢ : implementation[i])
            if (¢ != null && !(¢ instanceof Category.Bloater))
              t //
                  .col("Category", ¢.tipperGroup())//
                  .col("Tipper", Tippers.name(¢))//
                  .col("Node Type Number", i + "") //
                  .col("Node Class", wizard.intToClassName(i))//
                  .col("Actual class", name(¢.myActualOperandsClass()))//
                  .col("Abstract class", name(¢.getAbstractOperandClass()))//
                  .nl();
      System.err.println(t.description());
    }
  }
  public static String name(final Class<?> ¢) {
    return ¢ == null ? "???" : ¢.getSimpleName();
  }
}
