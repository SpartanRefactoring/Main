package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-22 */
public interface definition {
  enum Kind {
    local {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final List<ASTNode> $ = new ArrayList<>();
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
        final List<VariableDeclarationFragment> fs = fragments(s);
        assert fs != null;
        addRest($, f, fs);
        final Block b = az.block(parent(s));
        assert b != null;
        return addRest($, s, statements(b));
      }
    },
    lambda {
    },
    interface¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final ASTNode $ = parent(parent(¢));
        return !iz.compilationUnit($) ? members.of($) : step.types(az.compilationUnit($));
      }
    },
    class¢ {
      @Override public List<? extends ASTNode> specificScope(final SimpleName ¢) {
        final ASTNode $ = parent(parent(¢));
        return !iz.compilationUnit($) ? members.of($) : step.types(az.compilationUnit($));
      }
    },
    method {
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
    foreach {
    },
    for¢ {
    },
    parameter {
    },
    try¢ {
    },
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
    };
    public static boolean has(final String name) {
      if (name != null)
        for (final Kind ¢ : values())
          if (name.equals(¢ + ""))
            return true;
      return false;
    }

    public List<? extends ASTNode> scope(final SimpleName ¢) {
      final List<? extends ASTNode> $ = specificScope(¢);
      assert $ != null : fault.dump() + //
          "\n\t this = " + this + //
          "\n\t n=" + ¢ + //
          "\n\t p=" + parent(¢) + "/" + parent(¢).getClass().getSimpleName() + //
          "\n\t p^2=" + parent(parent(¢)) + "/" + parent(parent(¢)).getClass().getSimpleName() + //
          "\n\t m(p^2)=" + members.of(parent(parent(¢))) + "/" + parent(parent(¢)).getClass().getSimpleName() + //
          "\n\t definition.kind() = " + definition.kind(¢) + //
          fault.done();
      return $;
    }

    @SuppressWarnings("static-method") List<? extends ASTNode> specificScope(final SimpleName ¢) {
      return members.of(parent(parent(¢)));
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
      case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
        return Kind.annotationMemberDeclaration;
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

  static List<? extends ASTNode> addRest(final List<ASTNode> $, final ASTNode n, final List<? extends ASTNode> ns) {
    boolean add = false;
    for (final ASTNode x : ns)
      if (add)
        $.add(x);
      else
        add = x == n;
    return $;
  }

  static List<? extends ASTNode> scope(final SimpleName ¢) {
    return kind(¢).scope(¢);
  }
}
