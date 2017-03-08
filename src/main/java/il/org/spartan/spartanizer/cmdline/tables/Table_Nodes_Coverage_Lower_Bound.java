package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

/** Generates table presenting lower bound on {@link ASTNode}s coverage
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public class Table_Nodes_Coverage_Lower_Bound extends Table_Nodes_Coverage {
  static {
    clazz = Table_Nodes_Coverage_Lower_Bound.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Table_Nodes_Coverage.main(args);
  }

  @Override protected String analyze(final String from) {
    return spartanalyzer.once(from);
  }
}
