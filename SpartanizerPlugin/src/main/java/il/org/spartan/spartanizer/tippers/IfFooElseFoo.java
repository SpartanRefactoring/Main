package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-09-05 */
public final class IfFooElseFoo extends AbstractPattern<IfStatement> implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -1173304247743605864L;
  private Statement then;

  public IfFooElseFoo() {
    andAlso(Proposition.of("Then and else are identical",
        () -> wizard.same(//
            then = object().getThenStatement(), //
            object().getElseStatement()
        )));
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    trick.insertBefore(object(), wizard.decompose(object().getExpression()), r, g);
    trick.insertAfter(object(), as.list(then), r, g);
    remove.statement(object(), r, g);
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") IfStatement __) {
    return "Eliminate 'if' with two identical branches";
  }
}
