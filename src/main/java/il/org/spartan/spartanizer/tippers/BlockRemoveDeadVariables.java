package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** removes unused variable declarations example: "int i,j; j++" to "int j;
 * j++"
 * @author kobybs
 * @since 4-12-2016 */
public class BlockRemoveDeadVariables extends ReplaceCurrentNode<Block> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final Block n) {
    final Block $ = duplicate.of(n);
    final List<Statement> removalList = new ArrayList<>();
    for (final Statement s : extract.statements($)) {
      final VariableDeclarationStatement asVar = az.variableDeclarationStatement(s);
      if (asVar != null) {
        final List<VariableDeclarationFragment> as = new ArrayList<>(step.fragments(asVar));
        step.fragments(asVar).clear();
        for (final VariableDeclarationFragment ¢ : as)
          if (Collect.usesOf(¢.getName() + "").inside(n).size() > 1 || haz.sideEffects(¢.getInitializer()))
            step.fragments(asVar).add(¢);
        if (step.fragments(asVar).isEmpty())
          removalList.add(s);
      }
    }
    statements($).removeAll(removalList);
    return !wizard.same($, n) ? $ : null;
  }

  @Override @SuppressWarnings("unused") public String description(final Block n) {
    return "Eliminate dead variables";
  }
}
