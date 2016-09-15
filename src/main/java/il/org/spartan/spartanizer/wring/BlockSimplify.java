package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert
 *
 * <pre>
 * {
 *   ;
 *   ;
 *   g();
 *   {
 *   }
 *   {
 *     ;
 *     {
 *       ;
 *       {
 *         ;
 *       }
 *     }
 *     ;
 *   }
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * g();
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class BlockSimplify extends ReplaceCurrentNode<Block> implements Kind.NOP {
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

  static Statement reorganizeNestedStatement(final Statement s) {
    final List<Statement> ss = extract.statements(s);
    switch (ss.size()) {
      case 0:
        return s.getAST().newEmptyStatement();
      case 1:
        return duplicate.of(first(ss));
      default:
        return reorganizeStatement(s);
    }
  }

  private static boolean identical(final List<Statement> os1, final List<Statement> os2) {
    if (os1.size() != os2.size())
      return false;
    for (int i = 0; i < os1.size(); ++i)
      if (os1.get(i) != os2.get(i))
        return false;
    return true;
  }

  private static Block reorganizeStatement(final Statement s) {
    final List<Statement> ss = extract.statements(s);
    final Block $ = s.getAST().newBlock();
    duplicate.into(ss, statements($));
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final Block __) {
    return "Simplify block";
  }

  @Override public Statement replacement(final Block b) {
    final List<Statement> ss = extract.statements(b);
    if (identical(ss, statements(b)) || hasHidings(ss))
      return null;
    final ASTNode parent = b.getParent();
    if (!(parent instanceof Statement) || parent instanceof TryStatement)
      return reorganizeStatement(b);
    switch (ss.size()) {
      case 0:
        return b.getAST().newEmptyStatement();
      case 1:
        final Statement s = first(ss);
        if (iz.blockEssential(s))
          return subject.statement(s).toBlock();
        return duplicate.of(s);
      default:
        return reorganizeNestedStatement(b);
    }
  }
}
