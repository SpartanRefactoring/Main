package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

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

  /** Determines whether the method's return type is boolean.
   * @param ¢ method
   * @return */
  public static boolean booleanReturnType(final MethodDeclaration ¢) {
    return ¢ != null && step.returnType(¢) != null && iz.booleanType(step.returnType(¢));
  }

  public static boolean cent(final ASTNode ¢) {
    return !Collect.usesOf("¢").inside(¢).isEmpty();
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
    return !Collect.usesOf("$").inside(¢).isEmpty();
  }

  public static boolean dollar(final List<SimpleName> ns) {
    for (final SimpleName ¢ : ns)
      if ("$".equals(identifier(¢)))
        return true;
    return false;
  }

  public static boolean final¢(final List<IExtendedModifier> ms) {
    for (final IExtendedModifier ¢ : ms)
      if (IExtendedModifiersRank.find(¢) == IExtendedModifiersRank.FINAL)
        return true;
    return false;
  }

  public static boolean hasNoModifiers(final BodyDeclaration ¢) {
    return !¢.modifiers().isEmpty();
  }

  public static boolean hidings(final List<Statement> ss) {
    return new Predicate<List<Statement>>() {
      final Set<String> dictionary = new HashSet<>();

      @Override public boolean test(final List<Statement> ¢¢) {
        for (final Statement ¢ : ¢¢)
          if (¢(¢))
            return true;
        return false;
      }

      boolean ¢(final CatchClause ¢) {
        return ¢(¢.getException());
      }

      boolean ¢(final ForStatement ¢) {
        return ¢(step.initializers(¢));
      }

      boolean ¢(final List<Expression> xs) {
        for (final Expression ¢ : xs)
          if (¢ instanceof VariableDeclarationExpression && ¢((VariableDeclarationExpression) ¢))
            return true;
        return false;
      }

      boolean ¢(final SimpleName ¢) {
        return ¢(¢.getIdentifier());
      }

      boolean ¢(final SingleVariableDeclaration ¢) {
        return ¢(¢.getName());
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
        return ¢(¢.getName());
      }

      boolean ¢(final VariableDeclarationStatement ¢) {
        return ¢¢¢¢(fragments(¢));
      }

      boolean ¢¢(final List<CatchClause> cs) {
        for (final CatchClause ¢ : cs)
          if (¢(¢))
            return true;
        return false;
      }

      boolean ¢¢¢(final List<VariableDeclarationExpression> xs) {
        for (final VariableDeclarationExpression ¢ : xs)
          if (¢(¢))
            return true;
        return false;
      }

      boolean ¢¢¢¢(final List<VariableDeclarationFragment> fs) {
        for (final VariableDeclarationFragment x : fs)
          if (¢(x))
            return true;
        return false;
      }
    }.test(ss);
  }

  public static boolean sideEffects(final Expression ¢) {
    return !sideEffects.free(¢);
  }

  public static boolean sideEffects(final MethodDeclaration d) {
    final Block body = d.getBody();
    if (body != null)
      for (final Statement ¢ : statements(body))
        if (haz.sideEffects(¢))
          return true;
    return false;
  }

  public static boolean sideEffects(final Statement ¢) {
    final ExpressionStatement $ = az.expressionStatement(¢);
    return $ != null && sideEffects($.getExpression());
  }

  public static boolean unknownNumberOfEvaluations(final MethodDeclaration d) {
    final Block body = body(d);
    if (body != null)
      for (final Statement ¢ : statements(body))
        if (haz.unknownNumberOfEvaluations(d, ¢))
          return true;
    return false;
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
      if (iz.nodeTypeEquals(ancestor, FOR_STATEMENT)
          && (searchAncestors.specificallyFor(updaters((ForStatement) ancestor)).inclusiveFrom(child) != null
              || searchAncestors.specificallyFor(condition((ForStatement) ancestor)).inclusiveFrom(child) != null))
        return true;
      child = ancestor;
    }
    return false;
  }

  public static boolean variableDefinition(final ASTNode n) {
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.FALSE);
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        return continue¢(¢.getName());
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
        return continue¢(¢.getName());
      }

      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        return continue¢(fragments(¢));
      }

      boolean continue¢(final List<VariableDeclarationFragment> fs) {
        for (final VariableDeclarationFragment ¢ : fs)
          if (continue¢(¢.getName()))
            return true;
        return false;
      }

      boolean continue¢(final SimpleName ¢) {
        if (iz.identifier("$", ¢))
          return false;
        $.set(Boolean.TRUE);
        return true;
      }
    });
    return $.get().booleanValue();
  }

  static boolean binding(final ASTNode ¢) {
    return ¢ != null && ¢.getAST() != null && ¢.getAST().hasResolvedBindings();
  }

  static boolean hasAnnotation(final List<IExtendedModifier> ms) {
    for (final IExtendedModifier ¢ : ms)
      if (¢.isAnnotation())
        return true;
    return false;
  }

  /** @param ¢ JD
   * @return */
  public static boolean methods(final AbstractTypeDeclaration ¢) {
    return step.methods(¢) != null && !step.methods(¢).isEmpty();
  }
}
