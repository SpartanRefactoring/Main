package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.ASTNode;

/** Generates table presenting lower bound on {@link ASTNode}s coverage
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
public class Table_Nodes_Coverage_Lower_Bound extends Table_Nodes_Coverage {
  {
    analyze = nanonizer::once;
  }
}
