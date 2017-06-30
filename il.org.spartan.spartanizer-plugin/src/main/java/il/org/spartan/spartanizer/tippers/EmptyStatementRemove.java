package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-06-30 */
public final class EmptyStatementRemove extends NodeMatcher<EmptyStatement> {

  public EmptyStatementRemove() {
    andAlso("", ()-> iz.block(parent)); 
  }
  private static final long serialVersionUID = -0x682E5E8477E228C6L;

  @Override public Examples examples() {
    return convert("if (a) {f(); ;}").to("if (a) {f();}") //
        .convert("; if (a) f(); ").to("if (a) f();") //
        .convert("if (a) f(); ;").to("if (a) f();") //
    ;
  }
  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    remove.statement(current, r, g);
    return null;
  }
}
