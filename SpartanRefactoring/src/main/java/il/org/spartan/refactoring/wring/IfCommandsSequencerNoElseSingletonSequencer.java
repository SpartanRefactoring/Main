package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.elze;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.refactoring.utils.Funcs.then;
import static il.org.spartan.refactoring.wring.Wrings.*;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (x) {
 *   ;
 *   f();
 *   return a;
 * } else {
 *   ;
 *   g();
 *   {
 *   }
 * }</code> into <code>if (x) {
 *   f();
 *   return a;
 * }
 * g();</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfCommandsSequencerNoElseSingletonSequencer extends Wring.ReplaceToNextStatement<IfStatement> {
  @Override String description(@SuppressWarnings("unused") final IfStatement _) {
    return "Invert conditional and use next statement";
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
    final List<Statement> ss = Extract.statements(elze(canonicalIf));
    canonicalIf.setElseStatement(null);
    if (!Is.block(s.getParent())) { // case 2
      ss.add(0, canonicalIf);
      r.replace(s, Subject.ss(ss).toBlock(), g);
      r.remove(nextStatement, g);
    } else { // case 3
      r.replace(nextStatement, r.createGroupNode(ss.toArray(new ASTNode[ss.size()])), g);
      comments.setBase(s);
      comments.setCore(canonicalIf);
    }
    return r;
  }
  @Override WringGroup wringGroup() {
	return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}