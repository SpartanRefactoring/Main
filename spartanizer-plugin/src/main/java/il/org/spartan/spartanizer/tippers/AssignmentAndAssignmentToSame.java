package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.operator;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.issues.Issue1133;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** See {@link #examples()} for documentation
 * <p>
 * Tested by {@link Issue1133}
 * @author Yossi Gil
 * @since 2017-03-04 */
public final class AssignmentAndAssignmentToSame extends GoToNextStatement<Assignment>//
    implements Category.Collapse {
  private static final long serialVersionUID = 0x3B6B528C232B5CC8L;

  @Override public Examples examples() {
    return //
    convert("s=s.f();s=s.g();")//
        .to("s=s.f().g();") //
    ;
  }
  @Override public String description(final Assignment ¢) {
    return "Inline assignment to " + to(¢) + " into subsequent assignment";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final Assignment a1, final Statement nextStatement, final TextEditGroup g) {
    if (a1.getOperator() != ASSIGN || !iz.statement(parent(a1)))
      return null;
    final Assignment a2 = extract.assignment(nextStatement);
    if (operator(a2) != ASSIGN)
      return null;
    final SimpleName ret = az.simpleName(to(a1));
    if (!wizard.eq(ret, to(a2)) || !sideEffects.free(ret))
      return null;
    final Expression from1 = from(a1), from2 = from(a2);
    switch (collect.usesOf(ret).in(from2).size()) {
      case 0:
        return null;
      case 1:
        return go($, a1, g, ret, from1, from2);
      default:
        if (iz.deterministic(from1) && sideEffects.free(from1))
          return go($, a1, g, ret, from1, from2);
        return null;
    }
  }
  private static ASTRewrite go(final ASTRewrite $, final Assignment a1, final TextEditGroup g, final SimpleName to, final Expression from1,
      final Expression from2) {
    new Inliner(to, $, g).byValue(from1).inlineInto(from2);
    $.remove(a1.getParent(), g);
    return $;
  }
}
