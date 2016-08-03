package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
 * 
 * <pre>
 * if {a) g();}
 * </pre>
 * 
 * into
 * 
 * <pre>
 * if (a)
 *   g();
 * </pre>
 * 
 * @author Yossi Gil
 * @since 2015-09-09 */
public class BlockSingleton extends Wring.ReplaceCurrentNode<Block> {
  @Override Statement replacement(final Block b) {
    final ASTNode parent = parent(b);
    if (!(parent instanceof Statement) || parent instanceof TryStatement || parent instanceof SynchronizedStatement)
      return null;
    final List<Statement> ss = statements(b);
    if (ss.size() != 1)
      return null;
    final Statement $ = ss.get(0);
    return Is.blockEssential($) ? null : duplicate($);
  }
  @Override String description(@SuppressWarnings("unused") final Block __) {
    return "Remove redundant curly braces.";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REMOVE_SYNTACTIC_BAGGAGE;
  }
}