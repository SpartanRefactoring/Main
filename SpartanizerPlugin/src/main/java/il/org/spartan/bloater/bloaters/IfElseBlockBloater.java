package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Convert : {@code
 * if(condition) block1 else block2
 * } to : {@code if(condition){block1}else{block2} } Tested in {@link Issue0971}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-12-27 */
public class IfElseBlockBloater extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -0x3001ADCEF3C65216L;

  @Override public ASTNode replacement(final IfStatement s) {
    if (s == null || iz.block(then(s)) && elze(s) == null || iz.block(then(s)) && elze(s) != null && iz.block(elze(s)))
      return null;
    final IfStatement $ = copy.of(s);
    if (!iz.block(then(s))) {
      final Block b = s.getAST().newBlock();
      statements(b).add(copy.of(then(s)));
      $.setThenStatement(b);
    }
    if (elze(s) == null || iz.block(elze(s)))
      return $;
    final Block b = s.getAST().newBlock();
    statements(b).add(copy.of(elze(s)));
    $.setElseStatement(b);
    return $;
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

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return null;
  }
}
