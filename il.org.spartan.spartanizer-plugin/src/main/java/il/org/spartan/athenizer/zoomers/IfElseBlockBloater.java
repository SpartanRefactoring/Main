package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoom.zoomers.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Convert : {@code
 * if(condition) block1 else block2
 * } to : {@code if(condition){block1}else{block2} } Tested in {@link Issue0971}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-12-27 */
public class IfElseBlockBloater extends IfAbstractPattern implements Category.Bloater {
  private static final long serialVersionUID = 0x31C1FFA8E3CBA70DL;

  public IfElseBlockBloater() {
    andAlso("At least the if or the elze are not in a block",
        () -> !iz.block(current.getThenStatement()) || current.getElseStatement() != null && !iz.block(current.getElseStatement()));
  }
  @Override public Examples examples() {
    return //
    convert("if(f()) g();")//
        .to("if(f()) {g();}"). //
        convert("if(f()) g(); else h();")//
        .to("if(f()) {g();} else {h();}"). //
        convert("if(x) {a();b();} else h();")//
        .to("if(x) {a();b();} else {h();}")//
    ;
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final IfStatement $ = copy.of(current);
    if (!iz.block(then(current))) {
      final Block b = current.getAST().newBlock();
      statements(b).add(copy.of(then(current)));
      $.setThenStatement(b);
    }
    if (elze(current) != null && !iz.block(elze(current))) {
      final Block b = current.getAST().newBlock();
      statements(b).add(copy.of(elze(current)));
      $.setElseStatement(b);
    }
    r.replace(current, $, g);
    return r;
  }
}
