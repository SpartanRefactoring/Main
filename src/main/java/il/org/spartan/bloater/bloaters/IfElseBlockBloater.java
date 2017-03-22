package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Convert : {@code
 * if(condition) block1 else block2
 * } to : {@code if(condition){block1}else{block2} } Tested in {@link Issue0971}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
public class IfElseBlockBloater extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -3459237093161980438L;

  @Override @Nullable public ASTNode replacement(@Nullable final IfStatement s) {
    if (s == null || iz.block(then(s)) && elze(s) == null || iz.block(then(s)) && elze(s) != null && iz.block(elze(s)))
      return null;
    final IfStatement $ = copy.of(s);
    // TODO Dor please use class subject
    if (!iz.block(then(s))) {
      final Block b = s.getAST().newBlock();
      statements(b).add(copy.of(then(s)));
      $.setThenStatement(b);
    }
    if (elze(s) != null && !iz.block(elze(s))) {
      final Block b = s.getAST().newBlock();
      statements(b).add(copy.of(elze(s)));
      $.setElseStatement(b);
    }
    return $;
  }

  @Override @Nullable public String description(@SuppressWarnings("unused") final IfStatement __) {
    return null;
  }
}
