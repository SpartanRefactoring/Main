package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;

/** Extract items to which a given annotation applies
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-22 */
public interface annotees {
  static List<SimpleName> names(final List<VariableDeclarationFragment> fs) {
    final List<SimpleName> $ = new ArrayList<>();
    for (final VariableDeclarationFragment ¢ : fs)
      $.add(¢.getName());
    return $;
  }

  static List<SimpleName> of(final Annotation ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
        return of((VariableDeclarationExpression) $);
      case ASTNode.VARIABLE_DECLARATION_STATEMENT:
        return of((VariableDeclarationStatement) $);
      case ASTNode.SINGLE_VARIABLE_DECLARATION:
        return of((SingleVariableDeclaration) $);
      case ASTNode.FIELD_DECLARATION:
        return of((FieldDeclaration) $);
      case ASTNode.METHOD_DECLARATION:
        return of((MethodDeclaration) $);
      case ASTNode.TYPE_DECLARATION:
        return of((TypeDeclaration) $);
      case ASTNode.ENUM_CONSTANT_DECLARATION:
        return of((EnumConstantDeclaration) $);
      case ASTNode.ENUM_DECLARATION:
        return of((EnumDeclaration) $);
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        return of((AnnotationTypeDeclaration) $);
      default:
        assert false : $.getClass().getSimpleName();
        return null;
    }
  }

  static List<SimpleName> of(final AnnotationTypeDeclaration $) {
    return as.list($.getName());
  }

  static List<SimpleName> of(final EnumConstantDeclaration $) {
    return as.list($.getName());
  }

  static List<SimpleName> of(final EnumDeclaration $) {
    return as.list($.getName());
  }

  static List<SimpleName> of(final FieldDeclaration $) {
    return names(fragments($));
  }

  static List<SimpleName> of(final MethodDeclaration $) {
    return as.list($.getName());
  }

  static List<SimpleName> of(final SingleVariableDeclaration ¢) {
    return as.list(¢.getName());
  }

  static List<SimpleName> of(final TypeDeclaration $) {
    return as.list($.getName());
  }

  static List<SimpleName> of(final VariableDeclarationExpression ¢) {
    return names(fragments(¢));
  }

  static List<SimpleName> of(final VariableDeclarationStatement ¢) {
    return names(fragments(¢));
  }
}
