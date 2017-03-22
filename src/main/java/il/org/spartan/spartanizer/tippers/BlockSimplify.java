package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.range.*;

/** convert {@code { ; ; g(); {} { ; { ; { ; } } ; } } } into {@code g();}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-29 */
public final class BlockSimplify extends ReplaceCurrentNode<Block>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 5922696779617973428L;

  static Statement reorganizeNestedStatement(@NotNull final Statement ¢) {
    @NotNull final List<Statement> $ = extract.statements(¢);
    switch ($.size()) {
      case 0:
        return make.emptyStatement(¢);
      case 1:
        return copy.of(first($));
      default:
        return reorganizeStatement(¢);
    }
  }

  @SuppressWarnings("boxing") private static boolean identical(@NotNull final List<Statement> os1, @NotNull final List<Statement> os2) {
    return os1.size() == os2.size() && range.to(os1.size()).stream().allMatch(λ -> os1.get(λ) == os2.get(λ));
  }

  private static Block reorganizeStatement(@NotNull final Statement s) {
    @NotNull final List<Statement> ss = extract.statements(s);
    final Block $ = s.getAST().newBlock();
    copy.into(ss, statements($));
    return $;
  }

  @Override @NotNull public String description(final Block ¢) {
    return "Simplify block with  " + extract.statements(¢).size() + " sideEffects";
  }

  @Override public Statement replacement(@NotNull final Block b) {
    @NotNull final List<Statement> ss = extract.statements(b);
    if (identical(ss, statements(b)) || haz.hidings(ss))
      return null;
    @Nullable final ASTNode parent = az.statement(parent(b));
    if (parent == null || iz.tryStatement(parent))
      return reorganizeStatement(b);
    switch (ss.size()) {
      case 0:
        return make.emptyStatement(b);
      case 1:
        final Statement $ = first(ss);
        if (iz.blockEssential($))
          return subject.statement($).toBlock();
        return copy.of($);
      default:
        return reorganizeNestedStatement(b);
    }
  }
}
