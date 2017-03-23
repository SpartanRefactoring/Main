package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-11-27 */
public class TableTippers {
  public static void main(final String[] args) {
    new TableTippers().go();
  }

  public void go() {
    try (@NotNull Table t = new Table(this)) {
      final List<Tipper<? extends ASTNode>>[] implementation = Toolbox.defaultInstance().implementation;
      for (int i = 0; i < implementation.length; ++i)
        if (implementation[i] != null)
          for (@Nullable final Tipper<?> ¢ : implementation[i])
            if (¢ != null)
              t//
                  .col("Category", ¢.tipperGroup())//
                  .col("Tipper", Toolbox.name(¢))//
                  .col("Node Type Number", i) //
                  .col("Node Class", Toolbox.intToClassName(i))//
                  .col("Actual class", wizard.nodeName(¢.myActualOperandsClass()))//
                  .col("Abstract class", wizard.nodeName(¢.myAbstractOperandsClass())) //
                  .nl();
      System.err.println("Output found in " + t.description());
    }
  }
}
