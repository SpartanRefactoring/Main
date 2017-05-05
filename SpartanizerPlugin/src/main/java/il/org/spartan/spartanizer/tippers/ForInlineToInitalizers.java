package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Test case is {@link Issue0456} function documented in {@link #examples}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-04-06 */
public class ForInlineToInitalizers extends ForStatementPattern implements TipperCategory.Loops {
  private static final long serialVersionUID = -0x185A5E964CCBD32L;
  List<SimpleName> updatedNotDeclared = an.empty.list();
  List<SimpleName> candidates = an.empty.list();

  public ForInlineToInitalizers() {
    updaters.forEach(λ -> candidates.addAll(updatedVars(λ)));
    // remove variables declared in initialization list
    candidates.removeAll(initializers.stream().filter(iz::variableDeclarationExpression).map(az::variableDeclarationExpression)
        .flatMap(λ -> step.fragments(λ).stream()).map(step::name).collect(Collectors.toList()));
    andAlso("Exists updated variable not declared in for initalizers list", () -> !updatedNotDeclared.isEmpty());
  }
  private static List<SimpleName> updatedVars(final Expression u) {
    final List<SimpleName> $ = an.empty.list();
    final Expression e = extract.core(u);
    Expression d;
    if ((d = step.operand(az.postfixExpression(e))) != null || (d = step.operand(az.prefixExpression(e))) != null
        || (d = step.left(az.assignment(e))) != null)
      $.add(az.simpleName(d));
    return $;
  }
  @Override protected ASTRewrite go(@SuppressWarnings("unused") final ASTRewrite __, @SuppressWarnings("unused") final TextEditGroup g) {
    return null;
  }
  // TODO Yuval Simon: more meaningful description? which variable?
  @Override public String description() {
    return "Inline to for initalizers list";
  }
  @Override public Examples examples() {
    return convert("int i = 1; for(;i < 5; ++i) f(i);").to("for(int i = 1;i < 5; ++i) f(i);").ignores("int i = 1; for(;i < 5; ++i) f(i); g(i);");
  }
}
