package il.org.spartan.spartanizer.cmdline.tables;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.ASTNode;

import fluent.ly.system;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper.Category;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.tables.Statistic;
import il.org.spartan.tables.Table;

/** A class which generates a table of all Nano Patterns gathered by categories.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
public class Table_NanosByCategories {
  public static void main(final String[] args) {
    new Table_NanosByCategories().go();
  }
  public void go() {
    final List<Tipper<? extends ASTNode>>[] implementation = new Nanonizer().traversals.traversal.toolbox.implementation;
    final Map<String, Set<String>> categories = new TreeMap<>();
    for (int i = 0; i < implementation.length; ++i)
      if (implementation[i] != null)
        for (final Tipper<?> ¢ : implementation[i])
          if (¢ instanceof NanoPatternTipper) {
            final NanoPatternTipper<? extends ASTNode> np = (NanoPatternTipper<? extends ASTNode>) ¢;
            final String category = Category.pretty(np.category() != null ? np.category() + "" : wizard.intToClassName(i));
            categories.putIfAbsent(category, new TreeSet<>());
            categories.get(category).add(np.className());
          }
    try (Table t = new Table(this)) {
      t.noStatistics().add(Statistic.Σ);
      categories.keySet()
          .forEach(λ -> t//
              .col("Category", λ)//
              .col("Size", categories.get(λ).size()) //
              .col("Nanos", categories.get(λ).toArray())//
              .nl());
      System.err.println(t.description());
      system.dumpOutput(system.bash("column -s \\& -t /tmp/" + t.name + ".tex"));
    }
  }
}
