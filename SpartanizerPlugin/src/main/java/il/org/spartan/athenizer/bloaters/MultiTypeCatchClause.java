package il.org.spartan.athenizer.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Issue #970 Expand : {@code catch(Type1 | Type2 e){block} } To:
 * {@code catch(Type1 e){block}catch(Type2 e){block} } Tested in
 * {@link Issue0970}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-12-25 */
public class MultiTypeCatchClause extends ReplaceCurrentNode<TryStatement>//
    implements BloaterCategory.Splitting {
  private static final long serialVersionUID = -0xDFD08BA4C208C2FL;

  @Override public ASTNode replacement(final TryStatement s) {
    final List<CatchClause> catches = step.catchClauses(s);
    CatchClause multiTypeCatch = null;
    int i = 0;
    // TODO Ori Roth, this is a perfect example for extract method, which would
    // simpify the code
    for (; i < catches.size(); ++i) // Tough
      if (iz.unionType(catches.get(i).getException().getType())) {
        multiTypeCatch = copy.of(catches.get(i));
        break;
      }
    if (multiTypeCatch == null)
      return null;
    final List<Type> types = step.types(az.UnionType(multiTypeCatch.getException().getType()));
    final Block commonBody = step.catchClauses(s).get(i).getBody();
    final SimpleName commonName = multiTypeCatch.getException().getName();
    final TryStatement $ = copy.of(s);
    step.catchClauses($).remove(i);
    for (final Type t : types) {
      final CatchClause c = s.getAST().newCatchClause();
      c.setBody(copy.of(commonBody));
      final SingleVariableDeclaration e = s.getAST().newSingleVariableDeclaration();
      e.setName(copy.of(commonName));
      e.setType(copy.of(t));
      c.setException(e);
      step.catchClauses($).add(i, c);
      ++i;
    }
    return $;
  }
  @Override public String description(@SuppressWarnings("unused") final TryStatement __) {
    return null;
  }
}
