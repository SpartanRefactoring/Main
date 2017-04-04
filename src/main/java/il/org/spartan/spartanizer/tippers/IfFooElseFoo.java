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

/** @author Yossi Gil
 * @since 2015-09-05 */
public final class IfFooElseFoo extends IfAbstractPattern implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = -0x104869FF3436EC68L;

  public IfFooElseFoo() {
    andAlso("Then and else are identical", //
        () -> wizard.eq(then, elze));
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    trick.insertBefore(current(), wizard.decompose(current().getExpression()), r, g);
    trick.insertAfter(current(), as.list(then), r, g);
    remove.statement(current(), r, g);
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Eliminate 'if' with two identical branches";
  }

  @Override public Examples examples() {
    return //
    convert("if(f()==g())h();else h();")//
        .to("f();g();h();");
  }
}
