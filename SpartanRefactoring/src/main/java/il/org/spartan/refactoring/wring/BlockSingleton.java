package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.parent;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.get;

/**
 * A {@link Wring} to convert <code>if (a) (g();}</code> into
 * <code>if (a) g();</code>
 *
 * @author Yossi Gil
 * @since 2015-09-09
 */
public class BlockSingleton extends Wring.ReplaceCurrentNode<Block> {
  @Override Statement replacement(final Block b) {
    final List<Statement> ss = get.statements(b);
    final ASTNode parent = parent(b);
    if (!(parent instanceof Statement) || parent instanceof TryStatement || parent instanceof SynchronizedStatement
        || ss.size() == 1 && ss.get(0) instanceof VariableDeclarationStatement || ss.size() != 1)
      return null;
    final Statement $ = ss.get(0);
    return Is.blockEssential($) ? null : duplicate($);
  }
  @Override String description(@SuppressWarnings("unused") final Block _) {
    return "Remove redundant curly braces.";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REMOVE_REDUNDANT_PUNCTUATION;
  }
}