package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Consolidate identical catch blocks : <br>
 * <br>
 * convert
 *
 * <pre>
 * try{}catch(Exception1 e){Block}catch(Exception2 e){SameBlock}
 * </pre>
 *
 * to :
 *
 * <pre>
 * try{}catch(Exception1 | Exception2 e){Block}
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 20-11-2016 */
public class MergeCatches extends ReplaceCurrentNode<TryStatement> implements TipperCategory.Collapse {
  @Override @SuppressWarnings({ "unchecked" }) public ASTNode replacement(final TryStatement s) {
    final List<CatchClause> cs = step.catchClauses(s);
    for (int i = 0; i < cs.size(); ++i)
      for (int j = i + 1; j < cs.size(); ++j)
        if (wizard.same(cs.get(i).getBody(), cs.get(j).getBody())) {
          final TryStatement $ = duplicate.of(s);
          final CatchClause mergedCatch = duplicate.of(cs.get(i));
          $.catchClauses().remove(i);
          $.catchClauses().remove(j - 1);
          final UnionType ut = s.getAST().newUnionType();
          ut.types().add(0, duplicate.of(cs.get(i).getException().getType()));
          ut.types().add(0, duplicate.of(cs.get(j).getException().getType()));
          mergedCatch.getException().setType(ut);
          $.catchClauses().add(j - 1, mergedCatch);
          return $;
        }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final TryStatement ¢) {
    return "Consolidate identical catch blocks";
  }
}
