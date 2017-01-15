package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Convert :
 *
 * <pre>
 * if(condition) block1 else block2
 * </pre>
 *
 * to :
 *
 * <pre>
 * if(condition){block1}else{block2}
 * </pre>
 *
 * Tested in {@link Issue0971}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
public class IfElseBlockExpander extends ReplaceCurrentNode<IfStatement> implements TipperCategory.Expander {
  @Override public ASTNode replacement(final IfStatement s) {
    if (s == null || iz.block(step.then(s)) && step.elze(s) == null || iz.block(step.then(s)) && step.elze(s) != null && iz.block(step.elze(s)))
      return null;
    final IfStatement $ = copy.of(s);
    if (!iz.block(step.then(s))) {
      final Block b = s.getAST().newBlock();
      step.statements(b).add(copy.of(step.then(s)));
      $.setThenStatement(b);
    }
    if (step.elze(s) != null && !iz.block(step.elze(s))) {
      final Block b = s.getAST().newBlock();
      step.statements(b).add(copy.of(step.elze(s)));
      $.setElseStatement(b);
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return null;
  }
}
