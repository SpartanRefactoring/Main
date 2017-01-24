package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
public class CleanerVisitor extends ASTVisitor {
  @Override public boolean visit(final Javadoc ¢) {
    ¢.delete();
    return true;
  }

  @Override public boolean visit(final LineComment ¢) {
    ¢.delete();
    return true;
  }

  @Override public boolean visit(final BlockComment ¢) {
    ¢.delete();
    return true;
  }

  @Override public boolean visit(final ImportDeclaration ¢) {
    ¢.delete();
    return true;
  }
}
