package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class LocalVariableUninitializedDead extends VariableFragmnetUninitialized implements TipperCategory.Deadcode {
  private static final long serialVersionUID = 0x14812B0904DFB002L;

  public LocalVariableUninitializedDead() {
    andAlso(Proposition.of("Local variable is unused", () -> collect.usesOf(name).in(scope.of(name)).isEmpty()));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused local variable " + trivia.gist(¢.getName());
  }

  @Override public Example[] examples() {
    return new Example[] { //
        convert("int b; a = 3; f(b); f(a,b);a = f(a,b); b= f(a,b);}")//
            .to("a = 3; f(b); f(a,b);a = f(a,b); b= f(a,b);") };
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    wizard.removeFragment(object(), r, g);
    return r;
  }
}
