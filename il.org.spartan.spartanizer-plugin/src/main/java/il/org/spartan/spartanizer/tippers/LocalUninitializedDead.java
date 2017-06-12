package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalUninitializedDead extends LocalUninitialized implements Category.Deadcode {
  private static final long serialVersionUID = 0x14812B0904DFB002L;

  public LocalUninitializedDead() {
    andAlso("Local variable is not used anywhere", //
        () -> collect.usesOf(name).in(scope.of(name)).isEmpty());
  }
  @Override public String description() {
    return "Remove unused local variable " + name;
  }
  @Override public Examples examples() {
    return //
    convert("int c; int b; a = 3; f(b); f(a,b);a = f(a,b); b= f(a,b);")//
        .to("int b; a = 3; f(b); f(a,b);a = f(a,b); b= f(a,b);");
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    il.org.spartan.spartanizer.ast.factory.remove.deadFragment(current(), r, g);
    return r;
  }
}
