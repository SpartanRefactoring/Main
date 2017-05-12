package il.org.spartan.spartanizer.research.util;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** \@{"a","b"} to @{a}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-09 */
public class AnnotationCleanerVisitor extends ASTVisitor {
  @Override public boolean visit(final SingleMemberAnnotation ¢) {
    if (iz.arrayInitializer(value(¢)) && (¢ + "").contains("\""))
      ¢.delete();
    return true;
  }
}
