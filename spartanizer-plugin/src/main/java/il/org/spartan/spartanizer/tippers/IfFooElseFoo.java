package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** @author Yossi Gil
 * @since 2015-09-05 */
public final class IfFooElseFoo extends IfAbstractPattern implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x104869FF3436EC68L;

  public IfFooElseFoo() {
    andAlso("Then and else are identical", //
        () -> wizard.eq(then, elze));
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    misc.insertBefore(current(), compute.decompose(current().getExpression()), r, g);
    misc.insertAfter(current(), as.list(then), r, g);
    remove.statement(current(), r, g);
    return r;
  }
  @Override public String description() {
    return "Eliminate 'if' with two identical branches";
  }
  @Override public Examples examples() {
    return convert("if(f()==g())h();else h();").to("f();g();h();");
  }
}
