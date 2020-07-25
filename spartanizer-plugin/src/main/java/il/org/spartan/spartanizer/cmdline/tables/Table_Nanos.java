package il.org.spartan.spartanizer.cmdline.tables;

import static il.org.spartan.spartanizer.cmdline.tables.Table_Tippers.name;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import fluent.ly.system;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.tables.Table;

/** Generate a CSV file including all preliminary information we have on
 * patterns, i.e., without applying these.
 * @author Ori Marcovitch
 * @since Dec 4, 2016 */
public class Table_Nanos {
  public static void main(final String[] args) {
    new Table_Nanos().go();
  }
  public void go() {
    try (Table t = new Table(this)) {
      final List<Tipper<? extends ASTNode>>[] implementation = new Nanonizer().traversals.traversal.toolbox.implementation;
      for (int i = 0; i < implementation.length; ++i)
        if (implementation[i] != null)
          for (final Tipper<?> ¢ : implementation[i])
            if (¢ instanceof NanoPatternTipper)
              t//
                  .col("Name", name(¢.getClass()))//
                  .col("Node Class", wizard.intToClassName(i))//
                  .col("Description", ¢.description())//
                  .col("Example", ((NanoPatternTipper<?>) ¢).symbolycReplacement())//
                  .col("Replacement", ((NanoPatternTipper<?>) ¢).symbolycReplacement())//
                  .nl();
      System.err.println(t.description());
    }
    system.dumpOutput(system.bash("column -s \\& -t /tmp/nanos.tex"));
  }
}
