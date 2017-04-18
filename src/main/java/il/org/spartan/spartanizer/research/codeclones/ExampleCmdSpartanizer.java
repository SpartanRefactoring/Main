package il.org.spartan.spartanizer.research.codeclones;

import org.eclipse.jdt.core.dom.*;

/** TODO oran1248: document class
 * @author oran1248
 * @since 2017-04-18 */
public class ExampleCmdSpartanizer extends GeneralCmdSpartanizer {
  int whileCounter;

  @Override protected void setUp() {
    whileCounter = 0;
  }

  @Override protected ASTVisitor astVisitor() {
    return new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") WhileStatement node) {
        ++whileCounter;
        return true;
      }
    };
  }

  @Override protected void tearDown() {
    System.out.println("Total whiles: " + whileCounter);
  }
}
