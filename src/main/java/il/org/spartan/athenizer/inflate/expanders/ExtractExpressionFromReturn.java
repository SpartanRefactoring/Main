package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class ExtractExpressionFromReturn extends CarefulTipper<ReturnStatement> implements TipperCategory.InVain {
  @Override public String description(ReturnStatement r) {
    return "Extract expression from " + r + " statement";
  }

  @Override public Tip tip(final ReturnStatement ret) {
    if (expression(ret) == null || !iz.assignment(expression(ret)))
      return null;
    return new Tip(description(ret), ret, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Assignment a = az.assignment(expression(ret));
        final AST create = r.getAST();
        final ExpressionStatement exp = create.newExpressionStatement(duplicate.of(expression(ret)));
        final ReturnStatement retNoExp = create.newReturnStatement();
        retNoExp.setExpression(duplicate.of(left(a)));
        az.block(ret.getParent());
        final ListRewrite l = r.getListRewrite(ret.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(retNoExp, ret, g);
        l.insertAfter(exp, ret, g);
        l.remove(ret, g);
      }
    };
  }
}
