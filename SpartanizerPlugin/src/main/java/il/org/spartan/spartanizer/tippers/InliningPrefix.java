package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PostfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert :
 *
 * <pre>
 * array[i];
 * ++i;
 * </pre>
 *
 * to :
 *
 * <pre>
 * array[i++];
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 25-11-2016 */
public final class InliningPrefix extends EagerTipper<ArrayAccess> implements TipperCategory.Inlining {
  @Override public String description(@SuppressWarnings("unused") final ArrayAccess ¢) {
    return "Iniline the prefix expression after the access to the array";
  }

  @Override public Tip tip(final ArrayAccess a) {
    return extract.nextPrefix(a) == null || !wizard.same(extract.nextPrefix(a).getOperand(), a.getIndex()) ? null
        : new Tip(description(a), a, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final PostfixExpression newpost = a.getAST().newPostfixExpression();
            newpost.setOperand(duplicate.of(a.getIndex()));
            newpost.setOperator(Operator.INCREMENT);
            r.replace(a.getIndex(), newpost, g);
            r.remove(extract.nextStatement(a), g);
          }
        };
  }
}
