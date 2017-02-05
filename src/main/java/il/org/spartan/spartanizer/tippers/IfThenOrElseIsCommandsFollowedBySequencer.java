package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;



/** convert {@code f() { x++; y++; if (a) { i++; j++; k++; } } } into {@code if
 * (x) { f(); return a; } g(); }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThenOrElseIsCommandsFollowedBySequencer extends CarefulTipper<IfStatement>//
    implements TipperCategory.CommnonFactoring {
  static boolean endsWithSequencer(final Statement ¢) {
    return iz.sequencer(hop.lastStatement(¢));
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant else (possibly after inverting if statement)";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return elze(¢) != null && (endsWithSequencer(then(¢)) || endsWithSequencer(elze(¢)));
  }

  @Override  public Tip tip( final IfStatement s) {
    return new Tip(description(s), s, getClass()) {
      @Override public void go( final ASTRewrite r, final TextEditGroup g) {
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
