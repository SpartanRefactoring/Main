package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

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
    implements TipperCategory.Unite {
  private static final long serialVersionUID = 3971327019011226138L;

  @Override @Nullable protected ASTRewrite go(@NotNull final ASTRewrite $, @Nullable final ForStatement s, @Nullable final Statement nextStatement,
      final TextEditGroup g) {
    if (s == null || nextStatement == null || !iz.returnStatement(nextStatement) || !iz.emptyStatement(body(s)))
      return null;
    final ForStatement f = copy.of(s);
    @NotNull final Expression expression = expression(f);
    if (expression == null)
      return null;
    $.replace(s, f, g);
    f.setBody(subject.pair(nextStatement, null).toIf(make.notOf(expression)));
    f.setExpression(null);
    $.remove(nextStatement, g);
    return $;
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "combine the for and return statements to a single statement";
  }
}
