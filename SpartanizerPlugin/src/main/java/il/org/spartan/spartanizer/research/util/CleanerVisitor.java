package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.*;

/** A visitor to clean a tree of all comments and imports
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
