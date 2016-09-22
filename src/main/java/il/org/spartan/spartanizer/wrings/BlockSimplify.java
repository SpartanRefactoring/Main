package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

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
public final class BlockSimplify extends ReplaceCurrentNode<Block> implements Kind.InVain {
  static Statement reorganizeNestedStatement(final Statement s) {
    final List<Statement> ss = extract.statements(s);
    switch (ss.size()) {
      case 0:
        return make.emptyStatement(s); 
      case 1:
        return duplicate.of(first(ss));
      default:
        return reorganizeStatement(s);
    }
  }

  private static boolean identical(final List<Statement> os1, final List<Statement> os2) {
    if (os1.size() != os2.size())
      return false;
    for (int ¢ = 0; ¢ < os1.size(); ++¢)
      if (os1.get(¢) != os2.get(¢))
        return false;
    return true;
  }

  private static Block reorganizeStatement(final Statement s) {
    final List<Statement> ss = extract.statements(s);
    final Block $ = s.getAST().newBlock();
    duplicate.into(ss, statements($));
    return $;
  }

  @Override public String description(final Block ¢) {
    return "Simplify block with  " + extract.statements(¢).size() + " sideEffects";
  }

  @Override public Statement replacement(final Block b) {
    final List<Statement> ss = extract.statements(b);
    if (identical(ss, statements(b)) || haz.hasHidings(ss))
      return null;
    final ASTNode parent = az.asStatement(parent(b));
    if (parent == null || iz.tryStatement(parent))
      return reorganizeStatement(b);
    switch (ss.size()) {
      case 0:
        return make.emptyStatement(b);
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
