package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()))
 *  ;
 * return $ + ""
 * </code> to <code>
 * for (String line = r.readLine();; line = r.readLine(), $.append(line).append(System.lineSeparator()))
 *  if ( line != null)
 *    return $ + "";
 * </code>
 * @author Raviv Rachmiel
 * @since 25-11-2016 
 */
public class ForAndReturnToFor extends ReplaceToNextStatement<ForStatement> implements TipperCategory.Collapse {
  
  @Override protected ASTRewrite go(ASTRewrite r, ForStatement s, Statement nextStatement, TextEditGroup g) {
    System.out.println("HEEYYYY");
    if (s == null || r == null || nextStatement == null || !(nextStatement instanceof ReturnStatement))
      return null;
    System.out.println("HEEYYYY2");
    ForStatement f = duplicate.of(s);
    //f.setBody(duplicate.of(s));
    f.setBody(duplicate.of(nextStatement));
    f.setExpression(null);
    r.replace(s, f, g);
    r.replace(nextStatement, null, g);
    System.out.println("===========WYF");
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") ForStatement __) {
    return "combine the for and return statements to a single statement"; 
  }
  
 
}
