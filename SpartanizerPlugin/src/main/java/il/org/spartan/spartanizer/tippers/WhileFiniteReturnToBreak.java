package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** Convert Finite loops with return sideEffects to shorter ones : toList
 * Convert {@code for (..) { does(something); return XX; } return XX; } to :
 * {@code for (..) { does(something); break; } return XX; }
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public final class WhileFiniteReturnToBreak extends AbstractPattern<WhileStatement>//
    implements TipperCategory.CommnonFactoring {
  private ReturnStatement nextReturn;
  private ReturnStatement convertToBreak;

  public WhileFiniteReturnToBreak() {
    andAlso("Loop must be finite", //
        () -> iz.finiteLoop(current));
    andAlso("Loop must be followed by return", //
        () -> not.null¢(nextReturn = //
            az.returnStatement(nextStatement)));
    andAlso("Loop a return that can be converted to break", //
        () -> not.null¢(convertToBreak = //
            compute//
                .returns(current.getBody())//
                .stream().filter(λ -> wizard.eq(λ, nextReturn))//
                .findFirst()//
                .orElse(null)//
        ));
  }

  private static final long serialVersionUID = -0x70481BF1FE1E5DFBL;

  private static Statement handleBlock(final Block b, final ReturnStatement nextReturn) {
    Statement $ = null;
    for (final Statement ¢ : statements(b)) {
      if (iz.ifStatement(¢))
        $ = handleIf(¢, nextReturn);
      if (wizard.eq(nextReturn, expression(az.returnStatement(¢))))
        return ¢;
    }
    return $;
  }

  private static Statement handleIf(final IfStatement s, final ReturnStatement nextReturn) {
    return s == null ? null : handleIf(then(s), elze(s), nextReturn);
  }

  private static Statement handleIf(final Statement s, final ReturnStatement nextReturn) {
    return handleIf(az.ifStatement(s), nextReturn);
  }

  private static Statement handleIf(final Statement then, final Statement elze, final ReturnStatement nextReturn) {
    if (then == null)
      return null;
    if (wizard.eq(az.returnStatement(then), nextReturn.getExpression()))
      return then;
    if (iz.block(then)) {
      final Statement $ = handleBlock((Block) then, nextReturn);
      if ($ != null)
        return $;
    }
    if (iz.ifStatement(then))
      return handleIf(then, nextReturn);
    if (elze == null)
      return null;
    if (wizard.eq(az.returnStatement(elze), nextReturn))
      return elze;
    if (!iz.block(elze))
      return az.ifStatement(elze) == null ? null : handleIf(elze, nextReturn);
    final Statement $ = handleBlock((Block) elze, nextReturn);
    return $ != null ? $ : az.ifStatement(elze) == null ? null : handleIf(elze, nextReturn);
  }

  @Override public String description() {
    return "Convert the return inside the loop to break";
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    r.replace(convertToBreak, current.getAST().newBreakStatement(),g); 
    return r;
  }

  @Override public Examples examples() {
    return null;
  }
}
