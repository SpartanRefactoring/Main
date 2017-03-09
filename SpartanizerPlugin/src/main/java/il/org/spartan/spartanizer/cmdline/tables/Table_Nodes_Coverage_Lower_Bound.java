package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.*;

/** Generates table presenting lower bound on {@link ASTNode}s coverage
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public class Table_Nodes_Coverage_Lower_Bound extends Table_Nodes_Coverage {
  {
    analyze = λ -> spartanalyzer.once(λ);
  }
}
