package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** {@link{Issue1122}
 * @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-03-26 */
public class ForWithEndingBreakToDoWhile extends ReplaceCurrentNode<ForStatement> implements TipperCategory.Unite {
  private static final long serialVersionUID = -5286073381265258638L;

  @Override public ASTNode replacement(final ForStatement s) {
    final AST create = s.getAST();
    final DoStatement $ = create.newDoStatement();
    final IfStatement i = copy.of(az.ifStatement(extract.lastStatement(s)));
    $.setExpression(make.notOf(step.expression(i)));
    final Block b = create.newBlock();
    @NotNull final List<Statement> ls = extract.statements(copy.of(step.body(s)));
    for (int j = 0; j < ls.size() - 1; j++)
      step.statements(b).add(copy.of(ls.get(j)));
    $.setBody(b);
    return $;
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    if (!iz.ifStatement(extract.lastStatement(¢)))
      return false;
    final Statement s = az.ifStatement(extract.lastStatement(¢)).getThenStatement();
    return iz.block(s) && extract.statements(az.block(s)).size() == 1 && iz.breakStatement(extract.statements(az.block(s)).get(0))
        || iz.breakStatement(az.ifStatement(extract.lastStatement(¢)).getThenStatement());
  }


  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Replace for {... if(e) break;} loop by do{...} while(!e) loop";
  }

  @Override public Example[] examples() {
    return new Example[] { convert("for(int i=0;i<10;i++){x = x+5; if(i > 5 && i < 9) break;}").to("do{x = x+5;} while(i <= 5 || i>=9);") };
  }
}
