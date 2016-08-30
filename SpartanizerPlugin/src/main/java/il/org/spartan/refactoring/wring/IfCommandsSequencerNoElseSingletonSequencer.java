package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.utils.*;

/** convert
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
public final class IfCommandsSequencerNoElseSingletonSequencer extends Wring.ReplaceToNextStatement<IfStatement> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and use next statement)";
  }

  @Override ASTRewrite go(final ASTRewrite r, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s) || !iz.sequencer(nextStatement) || !endsWithSequencer(navigate.then(s)))
      return null;
    final IfStatement asVirtualIf = subject.pair(navigate.then(s), nextStatement).toIf(s.getExpression());
    if (wizard.same(navigate.then(asVirtualIf), navigate.elze(asVirtualIf))) {
      r.replace(s, navigate.then(asVirtualIf), g);
      r.remove(nextStatement, g);
      return r;
    }
    if (!shoudlInvert(asVirtualIf))
      return null;
    final IfStatement canonicalIf = invert(asVirtualIf);
    final List<Statement> ss = extract.statements(navigate.elze(canonicalIf));
    canonicalIf.setElseStatement(null);
    if (!iz.block(s.getParent())) {
      ss.add(0, canonicalIf);
      r.replace(s, subject.ss(ss).toBlock(), g);
      r.remove(nextStatement, g);
    } else {
      final ListRewrite lr = insertAfter(s, ss, r, g);
      lr.replace(s, canonicalIf, g);
      lr.remove(nextStatement, g);
    }
    return r;
  }
}
