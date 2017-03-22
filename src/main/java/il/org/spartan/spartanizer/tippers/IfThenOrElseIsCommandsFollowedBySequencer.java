package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code f() { x++; y++; if (a) { i++; j++; k++; } } } into {@code if
 * (x) { f(); return a; } g(); }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-29 */
public final class IfThenOrElseIsCommandsFollowedBySequencer extends CarefulTipper<IfStatement>//
    implements TipperCategory.CommnonFactoring {
  private static final long serialVersionUID = 6782225357014020547L;

  static boolean endsWithSequencer(final Statement ¢) {
    return iz.sequencer(hop.lastStatement(¢));
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant else (possibly after inverting if statement)";
  }

  @Override public boolean prerequisite(final IfStatement ¢) {
    return elze(¢) != null && (endsWithSequencer(then(¢)) || endsWithSequencer(elze(¢)));
  }

  @Nullable @Override public Tip tip(@NotNull final IfStatement s) {
    return new Tip(description(s), s, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        @NotNull final List<Statement> remainder = extract.statements(elze(shorterIf));
        shorterIf.setElseStatement(null);
        @Nullable final Block parent = az.block(s.getParent()), newParent = s.getAST().newBlock();
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
