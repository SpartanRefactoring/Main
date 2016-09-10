package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

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
  static boolean endsWithSequencer(final Statement s) {
    return iz.sequencer(hop.lastStatement(s));
  }

  @Override String description(@SuppressWarnings("unused") final IfStatement ____) {
    return "Remove redundant else (possibly after inverting if statement)";
  }

  @Override Rewrite make(final IfStatement s) {
    return new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        final List<Statement> remainder = extract.statements(step.elze(shorterIf));
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

  @Override boolean scopeIncludes(final IfStatement s) {
    return step.elze(s) != null && (endsWithSequencer(step.then(s)) || endsWithSequencer(step.elze(s)));
  }
}
