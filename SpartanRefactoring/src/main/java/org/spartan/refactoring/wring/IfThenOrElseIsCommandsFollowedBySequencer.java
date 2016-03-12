package org.spartan.refactoring.wring;

import static org.spartan.refactoring.utils.Funcs.asBlock;
import static org.spartan.refactoring.utils.Funcs.elze;
import static org.spartan.refactoring.utils.Funcs.then;
import static org.spartan.refactoring.utils.Restructure.duplicateInto;
import static org.spartan.refactoring.wring.Wrings.addAllReplacing;
import static org.spartan.refactoring.wring.Wrings.makeShorterIf;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.Extract;
import org.spartan.refactoring.utils.Is;
import org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} to convert <code> f() {
  x++;
  y++;
  if (a) {
     i++;
     j++;
     k++;
  }
}</code> }</code> into <code>if (x) {
 *   f();
 *   return a;
 * }
 * g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfThenOrElseIsCommandsFollowedBySequencer extends Wring<IfStatement> {
  static boolean endsWithSequencer(final Statement s) {
    return Is.sequencer(Extract.lastStatement(s));
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement _) {
    return "Remove redundant else (possibly after inverting if statement)";
  }
  @Override Rewrite make(final IfStatement s) {
    return new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        final List<Statement> remainder = Extract.statements(elze(shorterIf));
        shorterIf.setElseStatement(null);
        final Block parent = asBlock(s.getParent());
        final Block newParent = s.getAST().newBlock();
        if (parent != null) {
          addAllReplacing(newParent.statements(), parent.statements(), s, shorterIf, remainder);
          r.replace(parent, newParent, g);
        } else {
          newParent.statements().add(shorterIf);
          duplicateInto(remainder, newParent.statements());
          r.replace(s, newParent, g);
        }
      }
    };
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return elze(s) != null && (endsWithSequencer(then(s)) || endsWithSequencer(elze(s)));
  }
}