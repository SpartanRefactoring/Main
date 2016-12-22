package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-22 */
public interface definition {
  enum Kind {
    local, lambda, interface¢, class¢, method, catch¢, enum¢, enumConstant, field, foreach, for¢, parameter, try¢, annotation,;
    public static boolean has(final String name) {
      if (name != null)
        for (final Kind ¢ : values())
          if (name.equals(¢ + ""))
            return true;
      return false;
    }
  }

  static Kind kind(final SimpleName ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
        return kind((VariableDeclarationFragment) $);
      case ASTNode.SINGLE_VARIABLE_DECLARATION:
        return kind((SingleVariableDeclaration) $);
      case ASTNode.METHOD_DECLARATION:
        return !parameters((MethodDeclaration) $).contains(¢) ? Kind.method : Kind.parameter;
      case ASTNode.TYPE_DECLARATION:
        return !((TypeDeclaration) $).isInterface() ? Kind.class¢ : Kind.interface¢;
      case ASTNode.ENUM_CONSTANT_DECLARATION:
        return Kind.enumConstant;
      case ASTNode.ENUM_DECLARATION:
        return Kind.enum¢;
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        return Kind.annotation;
      default:
        assert false : $.getClass().getSimpleName();
        return null;
    }
  }

  static Kind kind(final VariableDeclarationFragment ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.FIELD_DECLARATION:
        return Kind.field;
      case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
        return kind((VariableDeclarationExpression) $);
      case ASTNode.VARIABLE_DECLARATION_STATEMENT:
        return Kind.local;
      default:
        assert false : $.getClass().getSimpleName();
        return null;
    }
  }

  static Kind kind(final SingleVariableDeclaration ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.ENHANCED_FOR_STATEMENT:
        return Kind.foreach;
      case ASTNode.CATCH_CLAUSE:
        return Kind.catch¢;
      case ASTNode.METHOD_DECLARATION:
        return Kind.parameter;
      case ASTNode.TYPE_DECLARATION:
        return !((TypeDeclaration) $).isInterface() ? Kind.class¢ : Kind.interface¢;
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        return Kind.annotation;
      case ASTNode.LAMBDA_EXPRESSION:
        return Kind.lambda;
      default:
        assert false : $.getClass().getSimpleName();
        return null;
    }
  }

  static Kind kind(final VariableDeclarationExpression ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.FOR_STATEMENT:
        return Kind.for¢;
      case ASTNode.TRY_STATEMENT:
        return Kind.try¢;
      default:
        assert false : $.getClass().getSimpleName();
        return null;
    }
  }
}
