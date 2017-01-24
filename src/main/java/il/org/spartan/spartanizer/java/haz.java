package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016-09-12 */
public enum haz {
  ;
  public static boolean annotation(final VariableDeclarationFragment ¢) {
    return annotation((VariableDeclarationStatement) ¢.getParent());
  }

  public static boolean annotation(final VariableDeclarationStatement ¢) {
    return !extract.annotations(¢).isEmpty();
  }

  /** @param ¢ JD
   * @return */
  public static boolean anyStatements(final MethodDeclaration ¢) {
    return ¢ != null && step.statements(¢) != null && !step.statements(¢).isEmpty();
  }

  public static boolean binding(final ASTNode ¢) {
    return ¢ != null && ¢.getAST() != null && ¢.getAST().hasResolvedBindings();
  }

  /** Determines whether the method's return type is boolean.
   * @param ¢ method
   * @return */
  public static boolean booleanReturnType(final MethodDeclaration ¢) {
    return ¢ != null && step.returnType(¢) != null && iz.booleanType(step.returnType(¢));
  }

  public static boolean cent(final ASTNode ¢) {
    return !collect.usesOf("¢").inside(¢).isEmpty();
  }

  /** Determine whether an {@link ASTNode} contains as a children a
   * {@link ContinueStatement}
   * @param ¢ JD
   * @return <code> true </code> iff ¢ contains any continue statement
   * @see {@link convertWhileToFor} */
  @SuppressWarnings("boxing") public static boolean ContinueStatement(final ASTNode ¢) {
    return ¢ != null
        && new Recurser<>(¢, 0).postVisit((x) -> x.getRoot().getNodeType() != ASTNode.CONTINUE_STATEMENT ? x.getCurrent() : x.getCurrent() + 1) > 0;
  }

  public static boolean dollar(final ASTNode ¢) {
    return !collect.usesOf("$").inside(¢).isEmpty();
  }

  public static boolean dollar(final List<SimpleName> ns) {
    return ns.stream().anyMatch(¢ -> "$".equals(identifier(¢)));
  }

  /** @param ¢ JD
   * @return */
  public static boolean expression(final MethodInvocation ¢) {
    return ¢ != null && step.expression(¢) != null;
  }

  public static boolean final¢(final List<IExtendedModifier> ms) {
    return ms.stream().anyMatch(¢ -> IExtendedModifiersRank.find(¢) == IExtendedModifiersRank.FINAL);
  }

  static boolean hasAnnotation(final List<IExtendedModifier> ¢) {
    return ¢.stream().anyMatch(IExtendedModifier::isAnnotation);
  }

  public static boolean hasNoModifiers(final BodyDeclaration ¢) {
    return !¢.modifiers().isEmpty();
  }

  public static boolean hidings(final List<Statement> ss) {
    return new Predicate<List<Statement>>() {
      final Set<String> dictionary = new HashSet<>();

      boolean ¢(final CatchClause ¢) {
        return ¢(¢.getException());
      }

      boolean ¢(final ForStatement ¢) {
        return ¢(step.initializers(¢));
      }

      boolean ¢(final List<Expression> xs) {
        return xs.stream().anyMatch(¢ -> iz.variableDeclarationExpression(¢) && ¢(az.variableDeclarationExpression(¢)));
      }

      boolean ¢(final SimpleName ¢) {
        return ¢(identifier(¢));
      }

      boolean ¢(final SingleVariableDeclaration ¢) {
        return ¢(step.name(¢));
      }

      boolean ¢(final Statement ¢) {
        return ¢ instanceof VariableDeclarationStatement ? ¢((VariableDeclarationStatement) ¢) //
            : ¢ instanceof ForStatement ? ¢((ForStatement) ¢) //
                : ¢ instanceof TryStatement && ¢((TryStatement) ¢);
      }

      boolean ¢(final String ¢) {
        if (dictionary.contains(¢))
          return true;
        dictionary.add(¢);
        return false;
      }

      boolean ¢(final TryStatement ¢) {
        return ¢¢¢(step.resources(¢)) || ¢¢(step.catchClauses(¢));
      }

      boolean ¢(final VariableDeclarationExpression ¢) {
        return ¢¢¢¢(step.fragments(¢));
      }

      boolean ¢(final VariableDeclarationFragment ¢) {
        return ¢(step.name(¢));
      }

      boolean ¢(final VariableDeclarationStatement ¢) {
        return ¢¢¢¢(fragments(¢));
      }

      boolean ¢¢(final List<CatchClause> cs) {
        return cs.stream().anyMatch(this::¢);
      }

      boolean ¢¢¢(final List<VariableDeclarationExpression> xs) {
        return xs.stream().anyMatch(this::¢);
      }

      boolean ¢¢¢¢(final List<VariableDeclarationFragment> fs) {
        return fs.stream().anyMatch(this::¢);
      }

      @Override public boolean test(final List<Statement> ¢¢) {
        return ¢¢.stream().anyMatch(this::¢);
      }
    }.test(ss);
  }

  /** @param ¢ JD
   * @return */
  public static boolean methods(final AbstractTypeDeclaration ¢) {
    return step.methods(¢) != null && !step.methods(¢).isEmpty();
  }

  public static boolean sideEffects(final Statement ¢) {
    final ExpressionStatement $ = az.expressionStatement(¢);
    return $ != null && !sideEffects.free($.getExpression());
  }

  public static boolean sideEffects(final Expression ¢) {
    return ¢ != null && !sideEffects.free(¢);
  }

  public static boolean unknownNumberOfEvaluations(final ASTNode n, final Statement s) {
    ASTNode child = n;
    for (final ASTNode ancestor : hop.ancestors(n)) {
      if (s == n)
        break;
      if (iz.nodeTypeIn(ancestor, WHILE_STATEMENT, DO_STATEMENT, ANONYMOUS_CLASS_DECLARATION))
        return true;
      if (iz.expressionOfEnhancedFor(child, ancestor))
        continue;
      if (iz.nodeTypeEquals(ancestor, FOR_STATEMENT) && (yieldAncestors.untilOneOf(updaters((ForStatement) ancestor)).inclusiveFrom(child) != null
          || yieldAncestors.untilNode(condition((ForStatement) ancestor)).inclusiveFrom(child) != null))
        return true;
      child = ancestor;
    }
    return false;
  }

  public static boolean unknownNumberOfEvaluations(final MethodDeclaration d) {
    final Block body = body(d);
    if (body != null)
      for (final Statement ¢ : statements(body))
        if (haz.unknownNumberOfEvaluations(d, ¢))
          return true;
    return false;
  }

  public static boolean variableDefinition(final ASTNode n) {
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.FALSE);
    n.accept(new ASTVisitor() {
      boolean continue¢(final List<VariableDeclarationFragment> fs) {
        return fs.stream().anyMatch(¢ -> continue¢(step.name(¢)));
      }

      boolean continue¢(final SimpleName ¢) {
        if (iz.identifier("$", ¢))
          return false;
        $.set(Boolean.TRUE);
        return true;
      }

      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        return continue¢(step.name(¢));
      }

      @Override public boolean visit(final FieldDeclaration node) {
        return continue¢(fragments(node));
      }

      @Override public boolean visit(final SingleVariableDeclaration node) {
        return continue¢(node.getName());
      }

      @Override public boolean visit(final VariableDeclarationExpression node) {
        return continue¢(fragments(node));
      }

      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        return continue¢(step.name(¢));
      }

      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        return continue¢(fragments(¢));
      }
    });
    return $.get().booleanValue();
  }
}
