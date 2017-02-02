package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Consolidate identical catch blocks : <br>
 * <br>
 * convert {@code try{}catch(Exception1 e){Block}catch(Exception2 e){SameBlock}
 * } to : {@code try{}catch(Exception1 | Exception2 e){Block} }
 * @author Dor Ma'ayan
 * @since 20-11-2016 */
public class MergeCatches extends ReplaceCurrentNode<TryStatement>//
    implements TipperCategory.Unite {
  @Override @SuppressWarnings({ "unchecked" }) @Nullable public ASTNode replacement(@NotNull final TryStatement s) {
    final List<CatchClause> cs = step.catchClauses(s);
    for (int i = 0; i < cs.size(); ++i)
      for (int j = i + 1; j < cs.size(); ++j)
        if (wizard.same(cs.get(i).getBody(), cs.get(j).getBody())) {
          final TryStatement $ = copy.of(s);
          final CatchClause mergedCatch = copy.of(cs.get(i));
          $.catchClauses().remove(i);
          $.catchClauses().remove(j - 1);
          final UnionType ut = s.getAST().newUnionType();
          ut.types().add(0, copy.of(cs.get(i).getException().getType()));
          ut.types().add(0, copy.of(cs.get(j).getException().getType()));
          mergedCatch.getException().setType(ut);
          $.catchClauses().add(j - 1, mergedCatch);
          return $;
        }
    return null;
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final TryStatement ¢) {
    return "Consolidate identical catch blocks";
  }
}
