package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

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
