package il.org.spartan.spartanizer.research.util;

import static il.org.spartan.spartanizer.ast.navigate.step.value;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import il.org.spartan.spartanizer.ast.safety.iz;

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
