package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.nullCheck;
import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.nullCheckees;
import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.returnsDefault;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.lazy;
import fluent.ly.separate;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** @nano if(X = null) return; <br>
 *       if(X = null) return null;
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public class NotNullAssumed extends NanoPatternTipper<IfStatement> {
  private static final long serialVersionUID = 0x36553BED8BDBAFF3L;
  private static final String description = "replace with azzert.NonNull(X)";
  private static final lazy<NonNullRequired> rival = lazy.get(NonNullRequired::new);

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && returnsDefault(then(¢)) //
        && rival.get().cantTip(¢)//
    ;
  }
  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, extract.singleStatement(make.ast("azzert.NonNull(" + separate.these(nullCheckees(¢)).by(",") + ");")), g);
      }
    };
  }
  @Override public String description() {
    return description;
  }
  @Override public String tipperName() {
    return "NotNullAssumed";
  }
}
