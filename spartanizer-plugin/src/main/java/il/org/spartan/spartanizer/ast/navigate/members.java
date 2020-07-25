package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.enumConstants;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** Collect members of classes and the such
 * @author Yossi Gil
 * @since 2016-12-22 */
public interface members {
  static List<BodyDeclaration> of(final EnumDeclaration ¢) {
    final List<BodyDeclaration> $ = as.list(enumConstants(¢));
    $.addAll(step.bodyDeclarations(¢));
    return $;
  }
  static List<? extends BodyDeclaration> of(final TypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }
  static List<? extends BodyDeclaration> of(final AnnotationTypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }
  static List<? extends BodyDeclaration> of(final AnonymousClassDeclaration ¢) {
    assert ¢ != null;
    final List<BodyDeclaration> $ = step.bodyDeclarations(¢);
    assert $ != null;
    return $;
  }
  static List<? extends BodyDeclaration> of(final ASTNode ¢) {
    return iz.anonymousClassDeclaration(¢) ? of(az.anonymousClassDeclaration(¢))
        : iz.enumDeclaration(¢) ? of(az.enumDeclaration(¢)) //
            : iz.typeDeclaration(¢) ? of(az.typeDeclaration(¢))//
                : iz.annotationTypeDeclaration(¢) ? of(az.annotationTypeDeclration(¢))//
                    : null;
  }
}
