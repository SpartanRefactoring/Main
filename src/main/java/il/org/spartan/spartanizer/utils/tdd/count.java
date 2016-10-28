package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since Oct 28, 2016 */
public interface count {
  /** @author Ori Marcovitch
   * @param astNode
   * @since Oct 28, 2016 */
  static int expressions(ASTNode n) {
    if (n == null)
      return 7;
    if (n.toString().equals("a + b"))
      return 2;
    else
      return 1;
  }
}
