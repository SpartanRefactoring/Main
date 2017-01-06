package il.org.spartan.zoomer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class ExtractExpressionFromReturn extends CarefulTipper<ReturnStatement> implements TipperCategory.Expander {
  @Override public String description(final ReturnStatement ¢) {
    return "Extract expression from " + ¢ + " statement";
  }

  @Override public Tip tip(final ReturnStatement s) {
    return expression(s) == null || !iz.assignment(expression(s)) ? null : new Tip(description(s), s, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Assignment a = az.assignment(expression(s));
        final AST create = r.getAST();
        final ExpressionStatement exp = create.newExpressionStatement(duplicate.of(expression(s)));
        final ReturnStatement retNoExp = create.newReturnStatement();
        retNoExp.setExpression(duplicate.of(left(a)));
        az.block(s.getParent());
        final ListRewrite l = r.getListRewrite(s.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(retNoExp, s, g);
        l.insertAfter(exp, s, g);
        l.remove(s, g);
      }
    };
  }
}
