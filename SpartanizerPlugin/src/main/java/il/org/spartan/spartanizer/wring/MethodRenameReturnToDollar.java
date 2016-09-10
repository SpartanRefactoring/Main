package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.Wrings.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Artium Nihamkin (original)
 * @author Boris van Sosin <tt><boris.van.sosin [at] gmail.com></tt> (v2)
 * @author Yossi Gil (v3)
 * @since 2013/01/01 */
public final class MethodRenameReturnToDollar extends Wring<MethodDeclaration> implements Kind.Dollarization {
  @Override String description(final MethodDeclaration d) {
    return d.getName() + "";
  }

  @Override Rewrite make(final MethodDeclaration d, final ExclusionManager exclude) {
    final Type t = d.getReturnType2();
    if (t instanceof PrimitiveType && ((PrimitiveType) t).getPrimitiveTypeCode() == PrimitiveType.VOID)
      return null;
    final SimpleName n = new Conservative(d).selectReturnVariable();
    if (n == null)
      return null;
    if (exclude != null)
      exclude.exclude(d);
    return new Rewrite("Rename variable " + n + " to $ (main variable returned by " + description(d) + ")", d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        rename(n, $(), d, r, g);
      }

      SimpleName $() {
        return d.getAST().newSimpleName("$");
      }
    };
  }
}

abstract class AbstractRenamePolicy {
  private static boolean hasDollar(final List<SimpleName> ns) {
    for (final SimpleName n : ns)
      if ("$".equals(n.getIdentifier()))
        return true;
    return false;
  }

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

  final MethodDeclaration inner;
  final List<SimpleName> localVariables;
  final List<ReturnStatement> returnStatements;

  public AbstractRenamePolicy(final MethodDeclaration inner) {
    final MethodExplorer explorer = new MethodExplorer(this.inner = inner);
    localVariables = explorer.localVariables();
    returnStatements = prune(explorer.returnStatements());
  }

  abstract SimpleName innerSelectReturnVariable();

  final SimpleName selectReturnVariable() {
    return returnStatements == null || localVariables == null || localVariables.isEmpty() || hasDollar(localVariables) ? null
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
    for (final SimpleName n : ns)
      $ = Math.max($, score(n, ss));
    return $;
  }

  private static boolean noRivals(final SimpleName candidate, final List<SimpleName> ns, final List<ReturnStatement> ss) {
    for (final SimpleName rival : ns)
      if (rival != candidate && score(rival, ss) >= score(candidate, ss))
        return false;
    return true;
  }

  private static int score(final SimpleName n, final List<ReturnStatement> ss) {
    int $ = 0;
    for (final ReturnStatement r : ss)
      $ += Collect.BOTH__LEXICAL.of(n).in(r).size();
    return $;
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
    for (final Iterator<SimpleName> i = localVariables.iterator(); i.hasNext();)
      if (unused(i.next()))
        i.remove();
    return lisp.first(localVariables);
  }

  private boolean unused(final SimpleName n) {
    for (final ReturnStatement s : returnStatements)
      if (wizard.same(n, s.getExpression()))
        return false;
    return true;
  }
}
