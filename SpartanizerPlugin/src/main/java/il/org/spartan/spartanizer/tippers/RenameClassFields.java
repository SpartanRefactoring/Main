package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Rename class field to the Type name in the following cases: - The type of
 * the field is unique in the class - The field is private and or the class is
 * private
 * @author Dor Ma'ayan
 * @since 2017-05-14 */
public final class RenameClassFields extends EagerTipper<FieldDeclaration>//
    implements TipperCategory.Centification {
  private static final long serialVersionUID = 0x5583F2C8E00B4000L;

  @Override public String description(@SuppressWarnings("unused") final FieldDeclaration ¢) {
    return "rename class field";
  }
  @Override @SuppressWarnings("boxing") public Tip tip(final FieldDeclaration d) {
    assert d != null;
    TypeDeclaration wrapper = az.typeDeclaration(d.getParent());
    if (!d.modifiers().contains(Modifier.PRIVATE) || wrapper == null || wrapper.modifiers().contains(Modifier.PRIVATE) || d.fragments().size() != 1)
      return null;
    FieldDeclaration[] fields = wrapper.getFields();
    Type t = d.getType();
    int count = 0;
    for (int ¢ = 0; ¢ < fields.length; ++¢)
      if (t.equals(fields[¢].getType()))
        ++count;
    if (count != 1)
      return null;
    final SimpleName $ = az.variableDeclrationFragment((ASTNode) d.fragments().get(0)).getName();
    assert $ != null;
    final SimpleName ¢ = make.newCent($);
    return new Tip("Rename paraemter " + $ + " to  " + ¢, getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, d, r, g);
      }
    }.spanning(d);
  }
}
