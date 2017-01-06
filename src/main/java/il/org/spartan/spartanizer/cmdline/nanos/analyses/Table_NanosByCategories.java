package il.org.spartan.spartanizer.cmdline.nanos.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import il.org.spartan.spartanizer.research.patterns.common.NanoPatternTipper.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-02 */
public class Table_NanosByCategories {
  public static void main(final String[] args) {
    new Table_NanosByCategories().go();
  }

  public void go() {
    final List<Tipper<? extends ASTNode>>[] implementation = Analyze.toolboxWithNanoPatterns().implementation;
    final Map<String, Set<String>> categories = new TreeMap<>();
    for (int i = 0; i < implementation.length; ++i)
      if (implementation[i] != null)
        for (final Tipper<?> ¢ : implementation[i])
          if (¢ != null && ¢ instanceof NanoPatternTipper) {
            final NanoPatternTipper<? extends ASTNode> np = (NanoPatternTipper<? extends ASTNode>) ¢;
            final String category = Category.pretty(np.category() != null ? np.category() : Toolbox.intToClassName(i));
            categories.putIfAbsent(category, new TreeSet<>());
            categories.get(category).add(np.getClass().getSimpleName());
          }
    try (final Table r = new Table(this)) {
      for (final String categoryName : categories.keySet())
        r//
            .col("Category", categoryName)//
            .col("Size", categories.get(categoryName).size()) //
            .col("Nanos", categories.get(categoryName).toArray())//
            .nl();
      System.err.println(r.description());
    }
    system.dumpOutput(system.bash("column -s \\& -t /tmp/nanos.tex"));
  }
}
