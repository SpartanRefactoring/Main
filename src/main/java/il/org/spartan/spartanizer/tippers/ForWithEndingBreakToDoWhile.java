package il.org.spartan.spartanizer.tippers;

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
  private static final long serialVersionUID = -0x495BE7BBC2F6B88EL;

  @Override public ASTNode replacement(final ForStatement s) {
    final AST create = s.getAST();
    final DoStatement $ = create.newDoStatement();
    $.setExpression(make.notOf(step.expression(az.ifStatement(extract.lastStatement(s)))));
    final Block b = create.newBlock();
    @NotNull final List<Statement> ls = extract.statements(copy.of(step.body(s)));
    for (int j = 0; j < ls.size() - 1; ++j)
      step.statements(b).add(copy.of(ls.get(j)));
    $.setBody(b);
    return $;
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    if (!iz.ifStatement(extract.lastStatement(¢)))
      return false;
    final Statement $ = az.ifStatement(extract.lastStatement(¢)).getThenStatement();
    return iz.block($) && extract.statements(az.block($)).size() == 1 && iz.breakStatement(extract.statements(az.block($)).get(0))
        || iz.breakStatement(az.ifStatement(extract.lastStatement(¢)).getThenStatement());
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "Replace for {... if(e) break;} loop by do{...} while(!e) loop";
  }

  @Override public Examples examples() {
    return //
    convert("for(int i=0;i<10;i++){x = x+5; if(i > 5 && i < 9) break;}") //
        .to("do{x = x+5;} while(i <= 5 || i>=9);") //
    ;
  }
}
