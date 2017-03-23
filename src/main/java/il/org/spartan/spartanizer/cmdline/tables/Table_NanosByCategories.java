package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** A class which generates a table of all Nano Patterns gathered by categories.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
public class Table_NanosByCategories {
  public static void main(final String[] args) {
    new Table_NanosByCategories().go();
  }

  public void go() {
    final List<Tipper<? extends ASTNode>>[] implementation = new SpartanAnalyzer().toolbox.implementation;
    @NotNull final Map<String, Set<String>> categories = new TreeMap<>();
    for (int i = 0; i < implementation.length; ++i)
      if (implementation[i] != null)
        for (final Tipper<?> ¢ : implementation[i])
          if (¢ instanceof NanoPatternTipper) {
            @NotNull final NanoPatternTipper<? extends ASTNode> np = (NanoPatternTipper<? extends ASTNode>) ¢;
            final String category = Category.pretty(np.category() != null ? np.category() + "" : Toolbox.intToClassName(i));
            categories.putIfAbsent(category, new TreeSet<>());
            categories.get(category).add(np.className());
          }
    try (@NotNull Table t = new Table(this)) {
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
