package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** convert {@code {;;g();{} {;{;{;} };} } } into {@code g();}
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class BlockSimplify extends ReplaceCurrentNode<Block>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x5231A5575D7AB4B4L;

  @Override public Examples examples() {
    return //
    convert("{;;g();{} {;{;{;} };} } ") //
        .to("g();") //
        .ignores("g();") //
    ;
  }
  static Statement reorganizeNestedStatement(final Statement ¢) {
    final List<Statement> $ = extract.statements(¢);
    switch ($.size()) {
      case 0:
        return make.emptyStatement(¢);
      case 1:
        return copy.of(the.headOf($));
      default:
        return reorganizeStatement(¢);
    }
  }
  @SuppressWarnings("boxing") private static boolean identical(final List<Statement> os1, final List<Statement> os2) {
    return os1.size() == os2.size() && range.to(os1.size()).stream().allMatch(λ -> os1.get(λ) == os2.get(λ));
  }
  private static Block reorganizeStatement(final Statement s) {
    final List<Statement> ss = extract.statements(s);
    final Block $ = s.getAST().newBlock();
    copy.into(ss, statements($));
    return $;
  }
  @Override public String description(final Block ¢) {
    return "Simplify block with  " + extract.statements(¢).size() + " statements";
  }
  @Override public Statement replacement(final Block b) {
    final List<Statement> ss = extract.statements(b);
    if (identical(ss, statements(b)) || haz.hidings(ss))
      return null;
    final ASTNode parent = az.statement(parent(b));
    if (parent == null || iz.tryStatement(parent))
      return reorganizeStatement(b);
    if (iz.synchronizedStatement(parent))
      return null;
    switch (ss.size()) {
      case 0:
        return make.emptyStatement(b);
      case 1:
        final Statement $ = the.headOf(ss);
        if (iz.blockEssential($))
          return subject.statement($).toBlock();
        return copy.of($);
      default:
        return reorganizeNestedStatement(b);
    }
  }
}
