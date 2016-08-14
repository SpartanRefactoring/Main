package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.extract.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>if (x) { ; f(); return a; } else { ; g(); {
 * } }</code> into <code>if (x) { f(); return a; } g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfCommandsSequencerNoElseSingletonSequencer extends Wring.ReplaceToNextStatement<IfStatement> implements Kind.ConsolidateStatements {
  @Override String description(final IfStatement s) {
    return "Invert conditional and use next statement of if(" + s.getExpression() + ") ...";
  }
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s) || !Is.sequencer(nextStatement) || !endsWithSequencer(then(s)))
      return null;
    final IfStatement asVirtualIf = Subject.pair(then(s), nextStatement).toIf(s.getExpression());
    if (same(then(asVirtualIf), elze(asVirtualIf))) { // case 1
      r.replace(s, then(asVirtualIf), g);
      r.remove(nextStatement, g);
      return r;
    }
    if (!shoudlInvert(asVirtualIf))
      return null;
    final IfStatement canonicalIf = invert(asVirtualIf);
    final List<Statement> ss = extract.statements(elze(canonicalIf));
    canonicalIf.setElseStatement(null);
    if (!Is.block(s.getParent())) { // case 2
      ss.add(0, canonicalIf);
      r.replace(s, Subject.ss(ss).toBlock(), g);
      r.remove(nextStatement, g);
    } else { // case 3
      final List<Statement> $ = new LinkedList<>();
      scalpel.duplicateInto(ss, $);
      $.add(0, canonicalIf);
      scalpel.operate(s, nextStatement).replaceWith($.toArray(new ASTNode[$.size()]));
    }
    return r;
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}