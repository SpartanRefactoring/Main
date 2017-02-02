package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class ForAndReturnToFor extends ReplaceToNextStatement<ForStatement>//
    implements TipperCategory.Unite {
  @Override @Nullable protected ASTRewrite go(@Nullable final ASTRewrite $, @Nullable final ForStatement s, @Nullable final Statement nextStatement,
      final TextEditGroup g) {
    if (s == null || nextStatement == null || !iz.returnStatement(nextStatement) || !iz.emptyStatement(body(s)))
      return null;
    final ForStatement f = copy.of(s);
    f.setBody(copy.of(subject.pair(copy.of(nextStatement), null).toIf(make.notOf(copy.of(expression(f))))));
    f.setExpression(null);
    $.replace(s, f, g);
    $.replace(nextStatement, null, g);
    return $;
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final ForStatement __) {
    return "combine the for and return statements to a single statement";
  }
}
