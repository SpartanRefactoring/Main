package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.dispatch.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
 *
 * <pre>
 * f() {
 *   x++;
 *   y++;
 *   if (a) {
 *     i++;
 *     j++;
 *     k++;
 *   }
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * if (x) {
 *   f();
 *   return a;
 * }
 * g();
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThenOrElseIsCommandsFollowedBySequencer extends Wring<IfStatement> implements Kind.DistributiveRefactoring {
  static boolean endsWithSequencer(final Statement ¢) {
    return iz.sequencer(hop.lastStatement(¢));
  }

  @Override public boolean demandsToSuggestButPerhapsCant(final IfStatement ¢) {
    return elze(¢) != null && (endsWithSequencer(then(¢)) || endsWithSequencer(elze(¢)));
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant else (possibly after inverting if statement)";
  }

  @Override public Suggestion suggest(final IfStatement s) {
    return new Suggestion(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        final List<Statement> remainder = extract.statements(elze(shorterIf));
        shorterIf.setElseStatement(null);
        final Block parent = az.block(s.getParent());
        final Block newParent = s.getAST().newBlock();
        if (parent != null) {
          addAllReplacing(statements(newParent), statements(parent), s, shorterIf, remainder);
          r.replace(parent, newParent, g);
        } else {
          statements(newParent).add(shorterIf);
          duplicate.into(remainder, statements(newParent));
          r.replace(s, newParent, g);
        }
      }
    };
  }
}
