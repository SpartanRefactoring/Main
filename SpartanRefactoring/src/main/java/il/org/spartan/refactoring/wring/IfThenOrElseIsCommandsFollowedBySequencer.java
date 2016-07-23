package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import static il.org.spartan.refactoring.utils.extract.*;

import static il.org.spartan.refactoring.wring.Wrings.*;

/**
 * A {@link Wring} to convert <code> f() { x++; y++; if (a) { i++; j++; k++; }
 * }</code> into <code>if (x) { f(); return a; } g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfThenOrElseIsCommandsFollowedBySequencer extends Wring<IfStatement> implements Kind.SIMPLIFY_NESTED_BLOCK {
  static boolean endsWithSequencer(final Statement s) {
    return Is.sequencer(extract.lastStatement(s));
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Remove redundant else (possibly after inverting if statement)";
  }
  @Override Suggestion make(final IfStatement s) {
    return new Suggestion(description(s), s) {
      @SuppressWarnings("unused") @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final IfStatement shorterIf = makeShorterIf(s);
        final List<Statement> remainder = extract.statements(elze(shorterIf));
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
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}