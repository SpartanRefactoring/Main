package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-25 */
public class MultiTypeCatchClause extends ReplaceCurrentNode<TryStatement> implements TipperCategory.InVain {
  @Override public ASTNode replacement(TryStatement s) {
    List<CatchClause> catches = step.catchClauses(s);
    CatchClause multiTypeCatch = null;
    int i = 0;
    for (; i < catches.size(); ++i)
      if (iz.unionType(catches.get(i).getException().getType())) {
        multiTypeCatch = duplicate.of(catches.get(i));
        break;
      }
    if (multiTypeCatch == null)
      return null;
    List<Type> types = step.types(az.UnionType(multiTypeCatch.getException().getType()));
    Block commonBody = step.catchClauses(s).get(i).getBody();
    SimpleName commonName = multiTypeCatch.getException().getName();
    TryStatement $ = duplicate.of(s);
    step.catchClauses($).remove(i);
    for (Type t : types) {
      CatchClause c = s.getAST().newCatchClause();
      c.setBody(duplicate.of(commonBody));
      SingleVariableDeclaration e = s.getAST().newSingleVariableDeclaration();
      e.setName(duplicate.of(commonName));
      e.setType(duplicate.of(t));
      c.setException(e);
      step.catchClauses($).add(c);
    }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") TryStatement __) {
    return null;
  }
}
