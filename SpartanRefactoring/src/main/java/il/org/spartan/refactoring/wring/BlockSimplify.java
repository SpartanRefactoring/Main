package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static il.org.spartan.refactoring.utils.expose.*;

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
      @Override public boolean test(List<Statement> ss) {
        for (final Statement s : ss)
          if (¢(s))
            return true;
        return false;
      }

      boolean ¢(CatchClause c) {
        return ¢(c.getException());
      }
      boolean ¢(ForStatement ¢) {
        return ¢(expose.initializers(¢));
      }
      boolean ¢(List<Expression> es) {
        for (Expression e : es)
          if (e instanceof VariableDeclarationExpression)
            if (¢((VariableDeclarationExpression) e))
              return true;
        return false;
      }
      boolean ¢(final SimpleName ¢) {
        return ¢(¢.getIdentifier());
      }
      boolean ¢(SingleVariableDeclaration d) {
        return ¢(d.getName());
      }
      boolean ¢(Statement ¢) {
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
      boolean ¢(TryStatement s) {
        return (¢¢¢(expose.resources(s)) || ¢¢(expose.catchClauses(s)));
      }
      boolean ¢(VariableDeclarationExpression ¢) {
        return ¢¢¢¢(expose.fragments(¢));
      }
      boolean ¢(VariableDeclarationFragment f) {
        return ¢(f.getName());
      }
      boolean ¢(final VariableDeclarationStatement ds) {
        return ¢¢¢¢(fragments(ds));
      }
      boolean ¢¢(List<CatchClause> cs) {
        for (CatchClause c : cs)
          if (¢(c))
            return true;
        return false;
      }
      boolean ¢¢¢(List<VariableDeclarationExpression> es) {
        for (VariableDeclarationExpression e : es)
          if (¢(e))
            return true;
        return false;
      }
      boolean ¢¢¢¢(List<VariableDeclarationFragment> fragments) {
        for (VariableDeclarationFragment x : fragments)
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
    final List<Statement> ss = extract.statements(s);
    final Block $ = s.getAST().newBlock();
    duplicateInto(ss, statements($));
    return $;
  }
  static Statement reorganizeNestedStatement(final Statement s) {
    final List<Statement> ss = extract.statements(s);
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