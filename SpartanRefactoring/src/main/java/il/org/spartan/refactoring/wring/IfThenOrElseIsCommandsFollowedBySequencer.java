package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.asBlock;
import static il.org.spartan.refactoring.utils.Funcs.elze;
import static il.org.spartan.refactoring.utils.Funcs.then;
import static il.org.spartan.refactoring.utils.Restructure.duplicateInto;
import static il.org.spartan.refactoring.wring.Wrings.addAllReplacing;
import static il.org.spartan.refactoring.wring.Wrings.makeShorterIf;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.utils.expose;

/**
 * A {@link Wring} to convert <code> f() {
  x++;
  y++;
  if (a) {
     i++;
     j++;
     k++;
  }
}</code> into <code>if (x) {
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
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant else (possibly after inverting if statement)";
  }
  @Override Rewrite make(final IfStatement s) {
    return new Rewrite(description(s), s) {
      @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        final List<Statement> remainder = Extract.statements(elze(shorterIf));
        shorterIf.setElseStatement(null);
        final Block parent = asBlock(s.getParent());
        final Block newParent = s.getAST().newBlock();
        final List<Statement> ss = expose.statements(newParent);
        if (parent != null) {
          addAllReplacing(ss, expose.statements(parent), s, shorterIf, remainder);
          scalpel.operate(parent).replaceWith(newParent);
        } else {
          ss.add(shorterIf);
          duplicateInto(remainder, ss);
          scalpel.operate(s).replaceWith(newParent);
        }
      }
    };
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return elze(s) != null && (endsWithSequencer(then(s)) || endsWithSequencer(elze(s)));
  }
  @Override WringGroup wringGroup() {
    return WringGroup.SIMPLIFY_NESTED_BLOCKS;
  }
}