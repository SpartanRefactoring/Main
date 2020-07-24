package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.athenizer.zoom.zoomers.Issue0977;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.java.namespace.scope;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Expand cases in a {@link SwitchStatement}: {@code switch (x) { case 1: f(1);
 * case 2: f(2); throw new Exception(); default: f(3); } } turns into
 * {@code switch (x) { case 1: f(1); f(2); throw new Exception(); case 2: f(2);
 * throw new Exception(); default: f(3); } } Test file: {@link Issue0977}
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @author Yuval Simon
 * @since 2016-12-28 */
public class CasesSplit extends CarefulTipper<SwitchStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x47C7172BFCDFA467L;

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "split cases within switch";
  }
  @Override public Tip tip(final SwitchStatement s) {
    final SwitchCase ret = caseWithNoSequencer(s);
    final List<Statement> $ = getAdditionalStatements(statements(s), ret);
    return new Tip(description(s), myClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Map<String, String> mapNames = getMapOldToNewNames($);
        final ListRewrite l = r.getListRewrite(s, SwitchStatement.STATEMENTS_PROPERTY);
        $.forEach(mapNames.isEmpty() ? λ -> l.insertBefore(copy.of(λ), ret, g) : λ -> l.insertBefore(replaceNames(copy.of(λ), mapNames), ret, g));
        if (!iz.sequencerComplex(the.lastOf($)))
          l.insertBefore(s.getAST().newBreakStatement(), ret, g);
      }
    };
  }
  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    return caseWithNoSequencer(¢) != null;
  }
  private static SwitchCase caseWithNoSequencer(final SwitchStatement x) {
    SwitchCase $ = null;
    for (final Statement ret : statements(x)) // TOUGH
      if (iz.sequencerComplex(ret))
        $ = null;
      else if (ret instanceof SwitchCase) {
        if ($ != null)
          return (SwitchCase) ret;
        $ = az.switchCase(ret);
      }
    return null;
  }
  private static List<Statement> getAdditionalStatements(final List<Statement> ss, final SwitchCase c) {
    final List<Statement> $ = an.empty.list();
    boolean additionalStatements = false;
    for (final Statement ¢ : ss.subList(ss.indexOf(c), ss.size())) {
      if (¢ instanceof SwitchCase)
        additionalStatements = true;
      else if (additionalStatements)
        $.add(¢);
      if (iz.sequencerComplex(¢))
        return $;
    }
    return $;
  }
  static Map<String, String> getMapOldToNewNames(final List<Statement> ss) {
    final Map<String, String> $ = new HashMap<>();
    ss.forEach(n -> {
      if (iz.variableDeclarationStatement(n))
        extract.fragments(n).forEach(
            λ -> $.put(λ.getName().getIdentifier(), scope.newName(λ, az.variableDeclarationStatement(n).getType(), λ.getName().getIdentifier())));
    });
    return $;
  }
  static Statement replaceNames(final Statement target, final Map<String, String> m) {
    target.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.simpleName(¢) && m.containsKey(az.simpleName(¢).getIdentifier()))
          az.simpleName(¢).setIdentifier(m.get(az.simpleName(¢).getIdentifier()));
      }
    });
    return target;
  }
}
