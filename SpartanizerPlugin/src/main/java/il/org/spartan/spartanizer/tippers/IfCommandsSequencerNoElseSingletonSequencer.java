package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code if (x) { f(); return a; } else { g(); {} } } into {@code if
 * (x) { f(); return a; } g(); }
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2015-07-29 */
public final class IfCommandsSequencerNoElseSingletonSequencer extends ReplaceToNextStatement<IfStatement>//
    implements TipperCategory.EarlyReturn {
  private static final long serialVersionUID = -5423686618924537619L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Invert conditional and use next statement)";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final IfStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!iz.vacuousElse(s) || !iz.sequencer(nextStatement) || !wizard.endsWithSequencer(then(s)))
      return null;
    final IfStatement asVirtualIf = subject.pair(then(s), nextStatement).toIf(s.getExpression());
    if (wizard.same(then(asVirtualIf), elze(asVirtualIf))) {
      $.replace(s, then(asVirtualIf), g);
      $.remove(nextStatement, g);
      return $;
    }
    if (!shoudlInvert(asVirtualIf))
      return null;
    final IfStatement canonicalIf = invert(asVirtualIf);
    final List<Statement> ss = extract.statements(elze(canonicalIf));
    canonicalIf.setElseStatement(null);
    if (!iz.block(s.getParent())) {
      ss.add(0, canonicalIf);
      $.replace(s, subject.ss(ss).toBlock(), g);
      $.remove(nextStatement, g);
    } else {
      final ListRewrite lr = insertAfter(s, ss, $, g);
      lr.replace(s, canonicalIf, g);
      lr.remove(nextStatement, g);
    }
    return $;
  }
}
