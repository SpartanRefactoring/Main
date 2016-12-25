package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class CommentsRemover extends ASTVisitor {
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
}
