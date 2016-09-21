package il.org.spartan.spartanizer.ast;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.*;

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

  public static boolean dollar(final List<SimpleName> ns) {
    for (final SimpleName ¢ : ns)
      if ("$".equals(identifier(¢)))
        return true;
    return false;
  }

  public static boolean hasHidings(final List<Statement> ss) {
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

  public static boolean hasNoModifiers(final BodyDeclaration ¢) {
    return !¢.modifiers().isEmpty();
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
          if (!continue¢(¢.getName()))
            return false;
        return true;
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

  static boolean hasAnnotation(final List<IExtendedModifier> ms) {
    for (final IExtendedModifier ¢ : ms)
      if (¢.isAnnotation())
        return true;
    return false;
  }

  public static boolean unknownNumberOfEvaluations(final SimpleName n, final Statement s) {
    ASTNode child = n;
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n)) {
      if (iz.is(ancestor, WHILE_STATEMENT, DO_STATEMENT, ANONYMOUS_CLASS_DECLARATION)
          || iz.is(ancestor, FOR_STATEMENT) && initializers((ForStatement) ancestor).indexOf(child) != -1
          || iz.is(ancestor, ENHANCED_FOR_STATEMENT) && ((EnhancedForStatement) ancestor).getExpression() != child)
        return true;
      child = ancestor;
    }
    return false;
  }
}
