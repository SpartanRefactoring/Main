package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.GoToNextStatement;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()))
 *  ;
 * return $ + ""
 * } to {@code
 * for (String line = r.readLine();; line = r.readLine(), $.append(line).append(System.lineSeparator()))
 *  if ( line == null)
 *    return $ + "";
 * }
 * @author Raviv Rachmiel
 * @since 25-11-2016 */
public class ForAndReturnToFor extends GoToNextStatement<ForStatement>//
    implements Category.Loops {
  private static final long serialVersionUID = 0x371CFCE1FF133A1AL;

  @Override protected ASTRewrite go(final ASTRewrite $, final ForStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (s == null || nextStatement == null || !iz.returnStatement(nextStatement) || !iz.emptyStatement(body(s)))
      return null;
    final ForStatement f = copy.of(s);
    final Expression expression = expression(f);
    if (expression == null)
      return null;
    $.replace(s, f, g);
    f.setBody(subject.pair(nextStatement, null).toIf(cons.not(expression)));
    f.setExpression(null);
    $.remove(nextStatement, g);
    return $;
  }
  @Override public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "combine the for and return statements to a single statement";
  }
}
