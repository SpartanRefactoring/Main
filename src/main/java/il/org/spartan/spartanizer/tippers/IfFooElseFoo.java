package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-09-05 */
public final class IfFooElseFoo extends IfAbstractPattern implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -1173304247743605864L;

  public IfFooElseFoo() {
    andAlso(Proposition.of("Then and else are identical", //
        () -> wizard.same(then, elze)));
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    trick.insertBefore(current(), wizard.decompose(current().getExpression()), r, g);
    trick.insertAfter(current(), as.list(then), r, g);
    remove.statement(current(), r, g);
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") IfStatement __) {
    return "Eliminate 'if' with two identical branches";
  }

  @Override public Example[] examples() {
    return new Example[] { //
        convert("if(f()==g())h();else h();")//
            .to("f();g();h();") };
  }
}
