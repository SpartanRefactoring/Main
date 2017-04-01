package il.org.spartan.bloater.bloaters;

import static il.org.spartan.utils.Example.*;

import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** examples in {@link #examples} test case is {@link Issue1159}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-31 */
public class SwitchAddDefault extends SwitchZtatement implements TipperCategory.Bloater {
  private static final long serialVersionUID = 3859494576633437003L;

  @Override public Example[] examples() {
    return new Example[] { convert("switch(x) { case 1: y=2; break; }").to("switch(x) { case 1: y=2; break; default: }"),
        ignores("switch(x) { case 1: y=2; break; default: }") };
  }

  public SwitchAddDefault() {
    andAlso(Proposition.of("Does not have default case", () -> cases().stream().filter(λ -> λ.isDefault()).collect(Collectors.toList()).isEmpty()));
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final ListRewrite l = $.getListRewrite(current, SwitchStatement.STATEMENTS_PROPERTY);
    final SwitchCase ss = current.getAST().newSwitchCase();
    ss.setExpression(null);
    l.insertLast(ss, g);
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Add default case to switch statement";
  }
}
