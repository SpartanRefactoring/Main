package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.utils.Examples;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-23 */
public final class LocalInitializedIfAssignmentUpdating extends LocalInitializedIfAssignmentPattern {
  public LocalInitializedIfAssignmentUpdating() {
    andAlso("Assignment must be updating", //
        () -> operator != ASSIGN);
    andAlso("Initializer has no side effects", //
        () -> sideEffects.free(initializer));
    andAlso("Initializer is deterministic ", //
        () -> sideEffects.deterministic(initializer));
    andAlso("Condition does not use initializer", //
        () -> compute.usedIdentifiers(condition).noneMatch(λ -> λ.equals(identifier)));
    andAlso("From does not use initializer", //
        () -> compute.usedIdentifiers(from).noneMatch(λ -> λ.equals(identifier)));
  }

  private static final long serialVersionUID = -0x3B3BD65F8057A88DL;

  // TODO: make the example test work
  @Override public Examples examples() {
    return null;
    // return convert("int a = 2;if (b) a += 3;").to("int a = y ? 2 + 3 : 2");
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    remove.statement(nextIf, $, g);
    $.replace(initializer,
        subject.pair(//
            subject.pair(initializer, from).to(op.assign2infix(operator)), //
            initializer //
        ).toCondition(condition), g);
    return $;
  }
}
