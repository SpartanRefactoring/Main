package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Restructure.duplicateInto;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>{;; g(); {}{;{;{;}};} }</code> into
 * <code>g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public class BlockSimplify extends Wring.ReplaceCurrentNode<Block> {
  @SuppressWarnings("unchecked")
  public static boolean safeBlockSimplification(List<Statement> ss) {
    List<String> l = new ArrayList<>();
    for (Statement s : ss)
      if (s instanceof VariableDeclarationStatement) {
        for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) ((VariableDeclarationStatement) s)
            .fragments())
          if (checkExistOrAdd(f.getName(), l))
            return false;
      } else if (s instanceof ForStatement) {
        for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) ((ForStatement) s).initializers())
          if (checkExistOrAdd(f.getName(), l))
            return false;
      } else if (s instanceof TryStatement) {
        for (VariableDeclarationExpression e : (List<VariableDeclarationExpression>) ((TryStatement) s).resources()) {
          for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) ((VariableDeclarationExpression) e)
              .fragments())
            if (checkExistOrAdd(f.getName(), l))
              return false;
        }
        for (CatchClause c : (List<CatchClause>) ((TryStatement) s).catchClauses()) {
            if (checkExistOrAdd(((CatchClause) c).getException().getName(), l))
              return false;
        }
      }
    return true;
  }
  static Statement reorganizeNestedStatement(final Statement s) {
    final List<Statement> ss = Extract.statements(s);
    switch (ss.size()) {
      case 0:
        return s.getAST().newEmptyStatement();
      case 1:
        return duplicate(ss.get(0));
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
    final List<Statement> ss = Extract.statements(s);
    final Block $ = s.getAST().newBlock();
    duplicateInto(ss, statements($));
    return $;
  }
  @Override Statement replacement(final Block b) {
    final List<Statement> ss = Extract.statements(b);
    if (identical(ss, statements(b)) || !safeBlockSimplification(ss))
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
  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Simplify block";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.REMOVE_SYNTACTIC_BAGGAGE;
  }
  private static boolean checkExistOrAdd(SimpleName n, List<String> l) {
    String s = n.getIdentifier();
    if (l.contains(s))
      return true;
    l.add(s);
    return false;
  }
}