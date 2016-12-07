package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()))
 *  ;
 * return $ + ""
 * </code> to <code>
 * for (String line = r.readLine();; line = r.readLine(), $.append(line).append(System.lineSeparator()))
 *  if ( line == null)
 *    return $ + "";
 * </code>
 * @author Raviv Rachmiel
 * @since 25-11-2016 */
public class ForAndReturnToFor extends ReplaceToNextStatement<ForStatement> implements TipperCategory.Collapse {
  @Override protected ASTRewrite go(final ASTRewrite r, final ForStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (s == null || r == null || nextStatement == null || !(nextStatement instanceof ReturnStatement) || !(s.getBody() instanceof EmptyStatement))
      return null;
    final ForStatement f = duplicate.of(s);
    final IfStatement ifBody = f.getBody().getAST().newIfStatement();
    ifBody.setExpression(make.notOf(duplicate.of(f.getExpression())));
    ifBody.setThenStatement(duplicate.of(nextStatement));
    f.setBody(duplicate.of(ifBody));
    f.setExpression(null);
    r.replace(s, f, g);
    r.replace(nextStatement, null, g);
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "combine the for and return statements to a single statement";
  }
}
