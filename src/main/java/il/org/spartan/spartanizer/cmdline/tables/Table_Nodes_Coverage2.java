package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

/** Generates table presenting {@link ASTNode}s coverage
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-09 */
public class Table_Nodes_Coverage2 {
  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    CoverageVisitor.main(args);
  }
}
