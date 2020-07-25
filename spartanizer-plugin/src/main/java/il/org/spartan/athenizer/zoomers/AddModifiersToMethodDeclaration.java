package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Add as much redundant modifiers as possible The opposite bloater to the
 * ModifierRedundant Tipper
 * @author Dor Ma'ayan
 * @since 2017-05-23 */
public class AddModifiersToMethodDeclaration extends CarefulTipper<MethodDeclaration> implements Category.Bloater {
  static final long serialVersionUID = 1;

  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration __) {
    return "Add as much modifiers as possible to the type declaration";
  }
  @Override @SuppressWarnings({ "boxing", "unchecked" }) public Tip tip(final MethodDeclaration d) {
    final BodyDeclaration wrap = az.bodyDeclaration(d.getParent());
    if (wrap == null)
      return null;
    final MethodDeclaration $ = copy.of(d);
    if (iz.interface¢(wrap)) {
      if (!wrap.modifiers().contains(Modifier.ABSTRACT))
        $.modifiers().add(Modifier.ABSTRACT);
      if (!wrap.modifiers().contains(Modifier.PUBLIC))
        $.modifiers().add(Modifier.PUBLIC);
    } else {
      if (iz.constructor(d) && iz.typeDeclaration(wrap) && !wrap.modifiers().contains(Modifier.PUBLIC) && !d.modifiers().contains(Modifier.PUBLIC))
        $.modifiers().add(Modifier.PUBLIC);
      if (iz.final¢(wrap) && !d.modifiers().contains(Modifier.FINAL) && !iz.constructor(d))
        $.modifiers().add($.getAST().newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
    }
    return new Tip(description(d), getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        if (d.modifiers().size() != $.modifiers().size())
          r.replace(d, $, g);
      }
    };
  }
}
