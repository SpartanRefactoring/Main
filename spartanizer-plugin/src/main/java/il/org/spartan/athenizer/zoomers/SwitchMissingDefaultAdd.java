package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue1159;
import il.org.spartan.spartanizer.tippers.Switch;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** examples in {@link #examples} test case is {@link Issue1159}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31 */
public class SwitchMissingDefaultAdd extends Switch implements Category.Bloater {
  private static final long serialVersionUID = 0x358FADDE74C85B4BL;

  @Override public Examples examples() {
    return //
    convert("switch(x) { case 1: y=2; break; }")//
        .to("switch(x) { case 1: y=2; break; default: }").//
        ignores("switch(x) { case 1: y=2; break; default: }")//
    ;
  }
  public SwitchMissingDefaultAdd() {
    andAlso("Does not have default case", //
        () -> cases().stream().noneMatch(SwitchCase::isDefault));
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final ListRewrite l = $.getListRewrite(current, SwitchStatement.STATEMENTS_PROPERTY);
    final SwitchCase ss = current.getAST().newSwitchCase();
    ss.setExpression(null);
    l.insertLast(ss, g);
    return $;
  }
  @Override public String description() {
    return "Add default case to switch statement";
  }
}
