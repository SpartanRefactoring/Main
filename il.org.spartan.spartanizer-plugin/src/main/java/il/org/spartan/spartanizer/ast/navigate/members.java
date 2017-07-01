package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Collect members of classes and the such
 * @author Yossi Gil
 * @since 2016-12-22 */
public interface members {
  static List<BodyDeclaration> of(final EnumDeclaration ¢) {
    final List<BodyDeclaration> ret = as.list(enumConstants(¢));
    ret.addAll(step.bodyDeclarations(¢));
    return ret;
  }
  static List<? extends BodyDeclaration> of(final TypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }
  static List<? extends BodyDeclaration> of(final AnnotationTypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }
  static List<? extends BodyDeclaration> of(final AnonymousClassDeclaration ¢) {
    assert ¢ != null;
    final List<BodyDeclaration> ret = step.bodyDeclarations(¢);
    assert ret != null;
    return ret;
  }
  static List<? extends BodyDeclaration> of(final ASTNode ¢) {
    return iz.anonymousClassDeclaration(¢) ? of(az.anonymousClassDeclaration(¢))
        : iz.enumDeclaration(¢) ? of(az.enumDeclaration(¢)) //
            : iz.typeDeclaration(¢) ? of(az.typeDeclaration(¢))//
                : iz.annotationTypeDeclaration(¢) ? of(az.annotationTypeDeclration(¢))//
                    : null;
  }
}
