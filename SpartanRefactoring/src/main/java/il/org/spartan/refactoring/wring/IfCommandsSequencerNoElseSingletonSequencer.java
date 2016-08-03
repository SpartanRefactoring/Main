package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
 * 
 * <pre>
 * if (x) {
 *   ;
 *   f();
 *   return a;
 * } else {
 *   ;
 *   g();
 *   {
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
public final class IfCommandsSequencerNoElseSingletonSequencer extends Wring.ReplaceToNextStatement<IfStatement> {
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and use next statement)";
  }
  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!Is.vacuousElse(s) || !Is.sequencer(nextStatement) || !endsWithSequencer(then(s)))
      return null;
    final IfStatement asVirtualIf = Subject.pair(then(s), nextStatement).toIf(s.getExpression());
    if (same(then(asVirtualIf), elze(asVirtualIf))) {
      r.replace(s, then(asVirtualIf), g);
      r.remove(nextStatement, g);
      return r;
    }
    if (!shoudlInvert(asVirtualIf))
      return null;
    final IfStatement canonicalIf = invert(asVirtualIf);
    final List<Statement> ss = extract.statements(elze(canonicalIf));
    canonicalIf.setElseStatement(null);
    if (!Is.block(s.getParent())) {
      ss.add(0, canonicalIf);
      r.replace(s, Subject.ss(ss).toBlock(), g);
      r.remove(nextStatement, g);
    } else {
      final ListRewrite lr = insertAfter(s, ss, r, g);
      lr.replace(s, canonicalIf, g);
      lr.remove(nextStatement, g);
    }
    return r;
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}