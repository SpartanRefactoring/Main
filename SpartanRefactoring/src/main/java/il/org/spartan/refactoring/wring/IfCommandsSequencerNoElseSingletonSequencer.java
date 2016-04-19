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
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.utils.Source;
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
    final List<Statement> ss = Extract.statements(elze(canonicalIf));
    canonicalIf.setElseStatement(null);
    ASTNode rep = null;
    if (!Is.block(s.getParent())) {
      ss.add(0, canonicalIf);
      rep = Subject.ss(ss).toBlock();
      r.replace(s, rep, g);
      r.remove(nextStatement, g);
    } else {
      final ListRewrite lr = insertAfter(s, ss, r, g);
      rep = canonicalIf;
      lr.replace(s, rep, g);
      lr.remove(nextStatement, g);
    }
    List<ASTNode> nl = Source.getComments(s, r);
    nl.addAll(Source.getComments(nextStatement, r));
    nl.add(rep);
    r.replace(rep, r.createGroupNode(nl.toArray(new ASTNode[nl.size()])), g);
    return r;
  }
  @Override WringGroup wringGroup() {
	return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}