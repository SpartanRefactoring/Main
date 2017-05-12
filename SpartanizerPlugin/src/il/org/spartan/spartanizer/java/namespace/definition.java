package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since 2016-12-22 */
public interface definition {
  enum Kind {
    annotation {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final ASTNode $ = parent(parent(¢));
        return !iz.compilationUnit($) ? members.of($) : step.types(az.compilationUnit($));
      }
    },
    annotationMemberDeclaration {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        return members.of(parent(parent(¢)));
      }
    },
    catch¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final CatchClause $ = az.catchClause(parent(parent(¢)));
        return as.list($.getBody());
      }
    },
    class¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final ASTNode $ = parent(parent(¢));
        return !iz.compilationUnit($) ? members.of($) : step.types(az.compilationUnit($));
      }
    },
    enum¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final ASTNode $ = parent(parent(¢));
        return !iz.compilationUnit($) ? members.of($) : step.types(az.compilationUnit($));
      }
    },
    enumConstant {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        return members.of(parent(parent(¢)));
      }
    },
    field {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        return members.of(parent(parent(parent(¢))));
      }
    },
    for¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final VariableDeclarationFragment f = az.variableDeclrationFragment(parent(¢));
        assert f != null;
        final VariableDeclarationExpression e = az.variableDeclarationExpression(parent(f));
        assert e != null;
        final ForStatement s = az.forStatement(parent(e));
        assert s != null;
        final List<ASTNode> $ = as.list(the.rest(f, fragments(e)));
        $.addAll(the.rest(e, initializers(s)));
        $.add(expression(s));
        $.addAll(updaters(s));
        $.add(body(s));
        return $;
      }
    },
    foreach {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final EnhancedForStatement $ = az.enhancedFor(parent(parent(¢)));
        return as.list($.getBody());
      }
    },
    interface¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final ASTNode $ = parent(parent(¢));
        return !iz.compilationUnit($) ? members.of($) : step.types(az.compilationUnit($));
      }
    },
    lambda {
      @Override public List<? extends ASTNode> specificScope(final SimpleName n) {
        final SingleVariableDeclaration d = az.singleVariableDeclaration(parent(n));
        assert d != null;
        final LambdaExpression $ = az.lambdaExpression(parent(d));
        assert $ != null : d;
        return as.list($);
      }
    },
    local {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final List<ASTNode> $ = an.empty.list();
        final VariableDeclarationFragment f = az.variableDeclrationFragment(parent(¢));
        if (f.getInitializer() != null)
          $.add(f.getInitializer());
        final VariableDeclarationStatement s = az.variableDeclarationStatement(parent(f));
        assert s != null : fault.dump() + //
        "\n\t ¢ = " + ¢ + //
        "\n\t f = " + f + //
        "\n\t i = " + f.getInitializer() + //
        "\n\t p = " + f.getInitializer() + parent(f) + "/" + parent(f).getClass().getSimpleName()//
            + fault.done();
        assert fragments(s) != null;
        $.addAll(the.rest(f, fragments(s)));
        $.addAll(hop.subsequentStatements(s));
        return $;
      }
    },
    method {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        return members.of(parent(parent(¢)));
      }
    },
    parameter {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final MethodDeclaration $ = az.methodDeclaration(parent(parent(¢)));
        return $.getBody() == null ? an.empty.list() : as.list($.getBody());
      }
    },
    try¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName n) {
        final VariableDeclarationFragment f = az.variableDeclrationFragment(parent(n));
        final VariableDeclarationExpression e = az.variableDeclarationExpression(parent(f));
        final TryStatement s = az.tryStatement(parent(e));
        final List<ASTNode> $ = an.empty.list();
        $.addAll(the.rest(f, fragments(e)));
        $.addAll(the.rest(e, resources(s)));
        $.add(body(s));
        $.addAll(catchClauses(s));
        return $;
      }
    };
    public List<? extends ASTNode> scope(final SimpleName ¢) {
      final List<? extends ASTNode> $ = specificScope(¢);
      assert $ != null : fault.dump() + //
          "\n\t this = " + this + //
          "\n\t n=" + ¢ + //
          "\n\t p^2=" + parent(parent(¢)) + "/" + parent(parent(¢)).getClass().getSimpleName() + //
          "\n\t m(p^2)=" + members.of(parent(parent(¢))) + "/" + parent(parent(¢)).getClass().getSimpleName() + //
          "\n\t p^3=" + parent(parent(parent(¢))) + "/" + parent(parent(parent(¢))).getClass().getSimpleName() + //
          "\n\t definition.kind() = " + definition.kind(¢) + //
          fault.done();
      return $;
    }

    @SuppressWarnings("static-method") List<? extends ASTNode> specificScope(final SimpleName ¢) {
      return members.of(parent(parent(¢)));
    }

    public static boolean has(final String name) {
      return name != null && Stream.of(values()).anyMatch(λ -> name.equals(λ + ""));
    }
  }

  static Kind kind(final SimpleName ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        return Kind.annotation;
      case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
        return Kind.annotationMemberDeclaration;
      case ASTNode.ENUM_CONSTANT_DECLARATION:
        return Kind.enumConstant;
      case ASTNode.ENUM_DECLARATION:
        return Kind.enum¢;
      case ASTNode.SINGLE_VARIABLE_DECLARATION:
        return kind((SingleVariableDeclaration) $);
      case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
        return kind((VariableDeclarationFragment) $);
      case ASTNode.METHOD_DECLARATION:
        return !parameters((MethodDeclaration) $).contains(¢) ? Kind.method : Kind.parameter;
      case ASTNode.TYPE_DECLARATION:
        return !((TypeDeclaration) $).isInterface() ? Kind.class¢ : Kind.interface¢;
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
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        return Kind.annotation;
      case ASTNode.CATCH_CLAUSE:
        return Kind.catch¢;
      case ASTNode.ENHANCED_FOR_STATEMENT:
        return Kind.foreach;
      case ASTNode.LAMBDA_EXPRESSION:
        return Kind.lambda;
      case ASTNode.METHOD_DECLARATION:
        return Kind.parameter;
      case ASTNode.TYPE_DECLARATION:
        return !((TypeDeclaration) $).isInterface() ? Kind.class¢ : Kind.interface¢;
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

  static List<? extends ASTNode> scope(final SimpleName ¢) {
    return kind(¢).scope(¢);
  }

  static Kind kind(final TypeDeclaration x) {
    return !x.isInterface() ? Kind.class¢ : Kind.interface¢;
  }
}
