package il.org.spartan.spartanizer.ast.navigate;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

/** Extract items to which a given annotation applies
 * @author Yossi Gil
 * @since 2016-12-22 */
public interface annotees {
  static List<SimpleName> names(final List<VariableDeclarationFragment> ¢) {
    return ¢.stream().map(VariableDeclaration::getName).collect(toList());
  }

  @SuppressWarnings("OverlyComplexMethod") static List<SimpleName> of(final Annotation ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        return of((AnnotationTypeDeclaration) $);
      case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
        return of((AnnotationTypeMemberDeclaration) $);
      case ASTNode.ENUM_CONSTANT_DECLARATION:
        return of((EnumConstantDeclaration) $);
      case ASTNode.ENUM_DECLARATION:
        return of((EnumDeclaration) $);
      case ASTNode.FIELD_DECLARATION:
        return of((FieldDeclaration) $);
      case ASTNode.METHOD_DECLARATION:
        return of((MethodDeclaration) $);
      case ASTNode.SINGLE_VARIABLE_DECLARATION:
        return of((SingleVariableDeclaration) $);
      case ASTNode.TYPE_DECLARATION:
        return of((TypeDeclaration) $);
      case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
        return of((VariableDeclarationExpression) $);
      case ASTNode.VARIABLE_DECLARATION_STATEMENT:
        return of((VariableDeclarationStatement) $);
      default:
        assert fault.unreachable() : fault.specifically("Unexpected __", wizard.nodeName($));
        return null;
    }
  }

  static List<SimpleName> of(final AnnotationTypeMemberDeclaration $) {
    return as.list($.getName());
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
