package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Convert Finite loops with return sideEffects to shorter ones : toList
 * Convert {@code for (..) { does(something); return XX; } return XX; } to :
 * {@code for (..) { does(something); break; } return XX; }
 * @author Dor Ma'ayan
 * @since 2016-09-07 */
public final class WhileFiniteReturnToBreak extends NodeMatcher<WhileStatement>//
    implements Category.Loops {
  private static final long serialVersionUID = -0x70481BF1FE1E5DFBL;

  public WhileFiniteReturnToBreak() {
    andAlso("Loop must be finite", //
        () -> iz.finiteLoop(current));
    andAlso("Loop must be followed by return", //
        () -> not.nil(nextReturn = //
            az.returnStatement(nextStatement)));
    andAlso("Loop a return that can be converted to break", //
        () -> not.nil(convertToBreak = //
            compute//
                .returns(current.getBody())//
                .stream().filter(λ -> wizard.eq(λ, nextReturn))//
                .findFirst()//
                .orElse(null)//
        ));
  }
  @Override public String description() {
    return "Convert the return inside the loop to break";
  }
  @Override public Examples examples() {
    return null;
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(convertToBreak, current.getAST().newBreakStatement(), g);
    return r;
  }
  @Override protected ASTNode highlight() {
    return convertToBreak;
  }

  private ReturnStatement convertToBreak;
  private ReturnStatement nextReturn;
}
