package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.misc.addAllReplacing;
import static il.org.spartan.spartanizer.ast.factory.misc.makeShorterIf;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code f() { x++; y++; if (a) { i++; j++; k++; } } } into {@code if
 * (x) { f(); return a; } g(); }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThenOrElseIsCommandsFollowedBySequencer extends CarefulTipper<IfStatement>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = 0x5E1F4E074777B1C3L;

  static boolean endsWithSequencer(final Statement ¢) {
    return iz.sequencer(hop.lastStatement(¢));
  }
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant else (possibly after inverting if statement)";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return elze(¢) != null && (endsWithSequencer(then(¢)) || endsWithSequencer(elze(¢)));
  }
  @Override public Tip tip(final IfStatement s) {
    return new Tip(description(s), getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        final List<Statement> remainder = extract.statements(elze(shorterIf));
        shorterIf.setElseStatement(null);
        final Block parent = az.block(s.getParent()), newParent = s.getAST().newBlock();
        if (parent != null) {
          addAllReplacing(statements(newParent), statements(parent), s, shorterIf, remainder);
          r.replace(parent, newParent, g);
        } else {
          statements(newParent).add(shorterIf);
          copy.into(remainder, statements(newParent));
          r.replace(s, newParent, g);
        }
      }
    };
  }
}
