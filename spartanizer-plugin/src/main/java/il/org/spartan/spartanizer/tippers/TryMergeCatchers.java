package il.org.spartan.spartanizer.tippers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.UnionType;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Consolidate identical catch blocks : <br>
 * <br>
 * convert {@code try{}catch(Exception1 e){Block}catch(Exception2 e){SameBlock}
 * } to : {@code try{}catch(Exception1 | Exception2 e){Block} }
 * @author Dor Ma'ayan
 * @since 20-11-2016 */
public class TryMergeCatchers extends ReplaceCurrentNode<TryStatement>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x75F125528ECF8007L;

  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final TryStatement s) {
    final List<CatchClause> cs = step.catchClauses(s);
    for (int i = 0; i < cs.size(); ++i)
      for (int j = i + 1; j < cs.size(); ++j)
        if (wizard.eq(cs.get(i).getBody(), cs.get(j).getBody())
            && !"Throwable".equals(cs.get(i).getException().getType() + "") && !"Throwable".equals(cs.get(j).getException().getType() + "")) {
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
  @Override public String description(@SuppressWarnings("unused") final TryStatement ¢) {
    return "Consolidate identical catch blocks";
  }
}
