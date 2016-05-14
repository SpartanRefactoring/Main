package il.org.spartan.refactoring.wring;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Scalpel;
import il.org.spartan.refactoring.utils.expose;

/**
 * A {@link Wring} to convert <code>{;; g(); {}{;{;{;}};} }</code> into
 * <code>g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public class BlockSimplify extends Wring.ReplaceCurrentNode<Block> {
  static Statement reorganizeNestedStatement(final Statement s, Scalpel scalpel) {
    final List<Statement> ss = Extract.statements(s);
    switch (ss.size()) {
      case 0:
        return s.getAST().newEmptyStatement();
      case 1:
        return scalpel.duplicate(ss.get(0));
      default:
        return reorganizeStatement(s, scalpel);
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
  private static Block reorganizeStatement(final Statement s, Scalpel scalpel) {
    final List<Statement> ss = Extract.statements(s);
    final Block $ = s.getAST().newBlock();
    scalpel.duplicateInto(ss, expose.statements($));
    return $;
  }
  @Override Statement replacement(final Block b) {
    final List<Statement> ss = Extract.statements(b);
    if (identical(ss, expose.statements(b)))
      return null;
    final ASTNode parent = b.getParent();
    if (!(parent instanceof Statement) || parent instanceof TryStatement)
      return reorganizeStatement(b, scalpel);
    switch (ss.size()) {
      case 0:
        return b.getAST().newEmptyStatement();
      case 1:
        final Statement s = ss.get(0);
        if (Is.blockEssential(s)) {
          final Block $ = b.getAST().newBlock();
          scalpel.duplicateInto(ss, expose.statements($));
          return $;
        }
        return scalpel.duplicate(s);
      default:
        return reorganizeNestedStatement(b, scalpel);
    }
  }
  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Simplify block";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REMOVE_REDUNDANT_PUNCTUATION;
  }
}