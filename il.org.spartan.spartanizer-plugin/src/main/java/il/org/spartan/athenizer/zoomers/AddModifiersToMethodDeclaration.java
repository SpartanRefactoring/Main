package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

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
