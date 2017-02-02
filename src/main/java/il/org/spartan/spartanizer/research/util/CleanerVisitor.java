package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;

/** A visitor to clean a tree of all comments and imports
 * @author Ori Marcovitch
 * @since 2016 */
public class CleanerVisitor extends ASTVisitor {
  @Override public boolean visit(@NotNull final Javadoc ¢) {
    ¢.delete();
    return true;
  }

  @Override public boolean visit(@NotNull final LineComment ¢) {
    ¢.delete();
    return true;
  }

  @Override public boolean visit(@NotNull final BlockComment ¢) {
    ¢.delete();
    return true;
  }

  @Override public boolean visit(@NotNull final ImportDeclaration ¢) {
    ¢.delete();
    return true;
  }
}
