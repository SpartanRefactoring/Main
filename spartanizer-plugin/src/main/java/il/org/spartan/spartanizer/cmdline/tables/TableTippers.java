package il.org.spartan.spartanizer.cmdline.tables;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.traversal.Tippers;
import il.org.spartan.spartanizer.traversal.Toolbox;
import il.org.spartan.tables.Table;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class TableTippers {
  public static void main(final String[] args) {
    new TableTippers().go();
  }
  public void go() {
    try (Table t = new Table(this)) {
      final List<Tipper<? extends ASTNode>>[] implementation = Toolbox.full().implementation;
      for (int i = 0; i < implementation.length; ++i)
        if (implementation[i] != null)
          for (final Tipper<?> ¢ : implementation[i])
            if (¢ != null)
              t//
                  .col("Category", ¢.tipperGroup())//
                  .col("Tipper", Tippers.name(¢))//
                  .col("Node Type Number", i) //
                  .col("Node Class", wizard.intToClassName(i))//
                  // .col("Actual class",
                  // wizard.nodeName(¢.myActualOperandsClass()))//
                  .col("Abstract class", wizard.nodeName(¢.getAbstractOperandClass())) //
                  .nl();
      System.err.println("Output found in " + t.description());
    }
  }
}
