package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** \@{"a","b"} to @{a}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-09 */
public class AnnotationCleanerVisitor extends ASTVisitor {
  @Override public boolean visit(final SingleMemberAnnotation ¢) {
    if (iz.arrayInitializer(value(¢)) && (¢ + "").contains("\""))
      ¢.delete();
    return true;
  }
}
