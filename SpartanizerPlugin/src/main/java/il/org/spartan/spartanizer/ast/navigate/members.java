package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;

/** Collect members of classes and the such
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-12-22 */
public interface members {
  @NotNull
  static List<BodyDeclaration> of(final EnumDeclaration ¢) {
    @NotNull final List<BodyDeclaration> $ = new ArrayList<>(enumConstants(¢));
    $.addAll(step.bodyDeclarations(¢));
    return $;
  }

  static List<? extends BodyDeclaration> of(final TypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }

  static List<? extends BodyDeclaration> of(final AnnotationTypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }

  @NotNull
  static List<? extends BodyDeclaration> of(@NotNull final AnonymousClassDeclaration ¢) {
    assert ¢ != null;
    @NotNull final List<BodyDeclaration> $ = step.bodyDeclarations(¢);
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
