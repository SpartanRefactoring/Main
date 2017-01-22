package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.dispatch.Tippers.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Artium Nihamkin (original)
 * @author Boris van Sosin <tt><boris.van.sosin [at] gmail.com></tt> (v2)
 * @author Yossi Gil (v3)
 * @since 2013/01/01 */
public final class MethodDeclarationRenameReturnToDollar extends EagerTipper<MethodDeclaration>//
    implements TipperCategory.Dollarization {
  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override public Tip tip(final MethodDeclaration d, final ExclusionManager exclude) {
    final Type t = d.getReturnType2();
    if (t instanceof PrimitiveType && ((PrimitiveType) t).getPrimitiveTypeCode() == PrimitiveType.VOID)
      return null;
    final SimpleName $ = new Conservative(d).selectReturnVariable();
    if ($ == null)
      return null;
    if (exclude != null)
      exclude.exclude(d);
    return new Tip("Rename '" + $ + "' to $ (main variable returned by " + description(d) + ")", d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        rename($, $(), d, r, g);
      }

      SimpleName $() {
        return d.getAST().newSimpleName("$");
      }
    };
  }
}

abstract class AbstractRenamePolicy {
  private static List<ReturnStatement> prune(final List<ReturnStatement> $) {
    if ($ == null || $.isEmpty())
      return null;
    for (final Iterator<ReturnStatement> i = $.iterator(); i.hasNext();) {
      final ReturnStatement r = i.next();
      // Empty returns stop the search. Something wrong is going on.
      if (r.getExpression() == null)
        return null;
      if (iz.literal(r))
        i.remove();
    }
    return $;
  }

  private final MethodDeclaration inner;
  final List<SimpleName> localVariables;
  final List<SingleVariableDeclaration> parameters;
  final List<ReturnStatement> returnStatements;

  AbstractRenamePolicy(final MethodDeclaration inner) {
    final MethodExplorer explorer = new MethodExplorer(this.inner = inner);
    localVariables = explorer.localVariables();
    parameters = step.parameters(inner);
    returnStatements = prune(explorer.returnStatements());
  }

  abstract SimpleName innerSelectReturnVariable();

  final SimpleName selectReturnVariable() {
    return returnStatements == null || localVariables == null || localVariables.isEmpty() || haz.dollar(step.body(inner)) ? null
        : innerSelectReturnVariable();
  }
}

class Aggressive extends AbstractRenamePolicy {
  private static SimpleName bestCandidate(final List<SimpleName> ns, final List<ReturnStatement> ss) {
    final int bestScore = bestScore(ns, ss);
    if (bestScore > 0)
      for (final SimpleName $ : ns)
        if (bestScore == score($, ss))
          return noRivals($, ns, ss) ? $ : null;
    return null;
  }

  private static int bestScore(final List<SimpleName> ns, final List<ReturnStatement> ss) {
    int $ = 0;
    for (final SimpleName ¢ : ns)
      $ = Math.max($, score(¢, ss));
    return $;
  }

  private static boolean noRivals(final SimpleName candidate, final List<SimpleName> ns, final List<ReturnStatement> ss) {
    return ns.stream().allMatch(rival -> rival == candidate || score(rival, ss) < score(candidate, ss));
  }

  @SuppressWarnings("boxing") private static int score(final SimpleName n, final List<ReturnStatement> ss) {
    return ss.stream().map(¢ -> Collect.BOTH_LEXICAL.of(n).in(¢).size()).reduce((x, y) -> x + y).get();
  }

  public Aggressive(final MethodDeclaration inner) {
    super(inner);
  }

  @Override SimpleName innerSelectReturnVariable() {
    return bestCandidate(localVariables, returnStatements);
  }
}

class Conservative extends AbstractRenamePolicy {
  public Conservative(final MethodDeclaration inner) {
    super(inner);
  }

  @Override SimpleName innerSelectReturnVariable() {
    for (final Iterator<SimpleName> $ = localVariables.iterator(); $.hasNext();)
      if (unused($.next()))
        $.remove();
    return !localVariables.isEmpty() ? first(localVariables)
        : parameters.stream().filter(¢ -> !unused(¢.getName())).map(¢ -> ¢.getName()).findFirst().orElse(null);
  }

  private boolean unused(final SimpleName n) {
    return returnStatements.stream().allMatch(¢ -> !(analyze.dependencies(¢).contains(n + "")));
  }
}
