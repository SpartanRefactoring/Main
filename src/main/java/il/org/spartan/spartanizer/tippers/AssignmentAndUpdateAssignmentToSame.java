package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** convert {@code
 * a += 3;
 * b += 6;
 * } to {@code
 * a  += 3 + 6;
 * }AssignmentUpdateAndSameUpdate
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-04 */
public final class AssignmentAndUpdateAssignmentToSame extends GoToNextStatement<Assignment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 6704758959472646819L;

  @Override @NotNull public Example[] examples() {
    return new Example[] { //
        convert("a+=3;b=6;").to("a+=3+6"), //
    };
  }

  @Override @NotNull public String description(final Assignment ¢) {
    return "Consolidate assignment to " + to(¢) + " with subsequent update assignment";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, @NotNull final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() != ASSIGN || !iz.statement(parent(a1)))
      return null;
    @Nullable final Assignment a2 = extract.assignment(nextStatement);
    final Assignment.Operator o = a2.getOperator();
    if (o == ASSIGN)
      return null;
    @Nullable final Expression to = to(a1);
    if (!wizard.same(to, to(a2)) || !sideEffects.free(to))
      return null;
    $.replace(from(a1), subject.operands(from(a1), from(a2)).to(assign2infix(o)), g);
    $.remove(nextStatement, g);
    return $;
  }
}
