package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
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
public class ForAndReturnToFor extends ReplaceToNextStatement<ForStatement>//
    implements TipperCategory.Unite {
  @Override protected ASTRewrite go(final ASTRewrite $, final ForStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (s == null || $ == null || nextStatement == null || !iz.returnStatement(nextStatement) || !iz.emptyStatement(body(s)))
      return null;
    final ForStatement f = copy.of(s);
    final IfStatement ifBody = f.getAST().newIfStatement();
    // TODO: Raviv Rachmiel please use class subject --yg
    ifBody.setExpression(make.notOf(copy.of(expression(f))));
    ifBody.setThenStatement(copy.of(nextStatement));
    f.setBody(copy.of(ifBody));
    f.setExpression(null);
    $.replace(s, f, g);
    $.replace(nextStatement, null, g);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "combine the for and return statements to a single statement";
  }
}
