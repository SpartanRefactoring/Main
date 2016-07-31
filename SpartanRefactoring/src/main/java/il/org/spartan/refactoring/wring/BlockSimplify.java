package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.expose.statements;
import static il.org.spartan.refactoring.utils.extract.statements;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>{;; g(); {}{;{;{;}};} }</code> into
 * <code>g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public class BlockSimplify extends Wring.ReplaceCurrentNode<Block> {
  public static boolean hasHidings(final List<Statement> ss) {
    return new Predicate<List<Statement>>() {
      @Override public boolean test(final List<Statement> ss) {
        for (final Statement s : ss)
          if (¢(s))
            return true;
        return false;
      }
      boolean ¢(final CatchClause c) {
        return ¢(c.getException());
      }
      boolean ¢(final ForStatement ¢) {
        return ¢(expose.initializers(¢));
      }
      boolean ¢(final List<Expression> es) {
        for (final Expression e : es)
          if (e instanceof VariableDeclarationExpression)
            if (¢((VariableDeclarationExpression) e))
              return true;
        return false;
      }
      boolean ¢(final SimpleName ¢) {
        return ¢(¢.getIdentifier());
      }
      boolean ¢(final SingleVariableDeclaration d) {
        return ¢(d.getName());
      }
      boolean ¢(final Statement ¢) {
        if (¢ instanceof VariableDeclarationStatement)
          return ¢((VariableDeclarationStatement) ¢);
        if (¢ instanceof ForStatement)
          return ¢((ForStatement) ¢);
        if (¢ instanceof TryStatement)
          return ¢((TryStatement) ¢);
        return false;
      }
      boolean ¢(final String ¢) {
        if (dictionary.contains(¢))
          return true;
        dictionary.add(¢);
        return false;
      }
      boolean ¢(final TryStatement s) {
        return ¢¢¢(expose.resources(s)) || ¢¢(expose.catchClauses(s));
      }
      boolean ¢(final VariableDeclarationExpression ¢) {
        return ¢¢¢¢(expose.fragments(¢));
      }
      boolean ¢(final VariableDeclarationFragment f) {
        return ¢(f.getName());
      }
      boolean ¢(final VariableDeclarationStatement ds) {
        return ¢¢¢¢(fragments(ds));
      }
      boolean ¢¢(final List<CatchClause> cs) {
        for (final CatchClause c : cs)
          if (¢(c))
            return true;
        return false;
      }
      boolean ¢¢¢(final List<VariableDeclarationExpression> es) {
        for (final VariableDeclarationExpression e : es)
          if (¢(e))
            return true;
        return false;
      }
      boolean ¢¢¢¢(final List<VariableDeclarationFragment> fragments) {
        for (final VariableDeclarationFragment x : fragments)
          if (¢(x))
            return true;
        return false;
      }

      final Set<String> dictionary = new HashSet<>();
    }.test(ss);
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
    final List<Statement> ss = statements(s);
    final Block $ = s.getAST().newBlock();
    duplicateInto(ss, statements($));
    return $;
  }
  static Statement reorganizeNestedStatement(final Statement s) {
    final List<Statement> ss = statements(s);
    switch (ss.size()) {
      case 0:
        return s.getAST().newEmptyStatement();
      case 1:
        return duplicate(ss.get(0));
      default:
        return reorganizeStatement(s);
    }
  }
  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Simplify block";
  }
  @Override Statement replacement(final Block b) {
    final List<Statement> ss = statements(b);
    if (identical(ss, statements(b)) || hasHidings(ss))
      return null;
    final ASTNode parent = b.getParent();
    if (!(parent instanceof Statement) || parent instanceof TryStatement)
      return reorganizeStatement(b);
    switch (ss.size()) {
      case 0:
        return b.getAST().newEmptyStatement();
      case 1:
        final Statement s = ss.get(0);
        if (Is.blockEssential(s))
          return Subject.statement(s).toBlock();
        return duplicate(s);
      default:
        return reorganizeNestedStatement(b);
    }
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REMOVE_SYNTACTIC_BAGGAGE;
  }
}