package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.remove;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.utils.Examples;

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
