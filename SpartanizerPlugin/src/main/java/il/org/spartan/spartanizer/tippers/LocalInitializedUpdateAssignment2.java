package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** convert {@code
 * int a;
 * a = 3;
 * } into {@code
 * int a = 3;
 * }
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedUpdateAssignment2 extends LocalInitializedStatement implements TipperCategory.Unite {
  private static final long serialVersionUID = -0x601DD969FC862E65L;

  @Override public String description() {
    return "Consolidate declaration of " + name + " with its subsequent initialization";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Assignment a = extract.assignment(nextStatement);
    if (a == null || !wizard.eq(name, to(a)))
      return null;
    final Operator o = a.getOperator();
    if (o == ASSIGN)
      return null;
    final InfixExpression newInitializer = subject.pair(to(a), from(a)).to(op.assign2infix(o));
    final InlinerWithValue i = new Inliner(name, $, g).byValue(initializer);
    if (!i.canInlineinto(newInitializer) || i.replacedSize(newInitializer) - metrics.size(nextStatement, initializer) > 0)
      return null;
    $.replace(initializer, newInitializer, g);
    i.inlineInto(newInitializer);
    $.remove(nextStatement, g);
    return $;
  }

  @Override public Examples examples() {
    return convert("int a;a=3;").to("int a=3;");
  }
}
