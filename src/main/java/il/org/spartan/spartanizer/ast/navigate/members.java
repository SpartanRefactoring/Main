package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Collect members of classes and the such
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-22 */
public interface members {
  @Nullable
  static List<? extends ASTNode> of(final EnumDeclaration ¢) {
    final ArrayList<ASTNode> $ = new ArrayList<>(enumConstants(¢));
    $.addAll(step.bodyDeclarations(¢));
    return $;
  }

  @Nullable
  static List<? extends BodyDeclaration> of(final TypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }

  @Nullable
  static List<? extends BodyDeclaration> of(final AnnotationTypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }

  @Nullable
  static List<? extends BodyDeclaration> of(@NotNull final AnonymousClassDeclaration ¢) {
    assert ¢ != null;
    final List<BodyDeclaration> $ = step.bodyDeclarations(¢);
    assert $ != null;
    return $;
  }

  static List<? extends ASTNode> of(final ASTNode ¢) {
    return iz.anonymousClassDeclaration(¢) ? of(az.anonymousClassDeclaration(¢))
        : iz.enumDeclaration(¢) ? of(az.enumDeclaration(¢)) //
            : iz.typeDeclaration(¢) ? of(az.typeDeclaration(¢))//
                : iz.annotationTypeDeclaration(¢) ? of(az.annotationTypeDeclration(¢))//
                    : null;
  }
}
