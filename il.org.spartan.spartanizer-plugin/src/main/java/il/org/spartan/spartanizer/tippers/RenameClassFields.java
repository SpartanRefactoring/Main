package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Rename class field to the Type name in the following cases: - The type of
 * the field is unique in the class - The field is private and or the class is
 * private Issue #1176
 * @author Dor Ma'ayan
 * @since 2017-05-14 */
public final class RenameClassFields extends EagerTipper<FieldDeclaration>//
    implements Nominal.Trivialization {
  private static final long serialVersionUID = 0x5583F2CE00B4000L;

  @Override public String description(@SuppressWarnings("unused") final FieldDeclaration ¢) {
    return "rename class field";
  }
  @Override public Tip tip(final FieldDeclaration d) {
    assert d != null;
    final TypeDeclaration wrapper = az.typeDeclaration(d.getParent());
    if (wrapper == null || !iz.private¢(d) && !iz.private¢(wrapper) || d.fragments().size() != 1 || iz.primitiveType(d.getType()))
      return null;
    final FieldDeclaration[] fields = wrapper.getFields();
    final Type t = d.getType();
    int count = 0;
    for (final FieldDeclaration ¢ : fields)
      if (t.equals(¢.getType()))
        ++count;
    if (count != 1)
      return null;
    final SimpleName $ = az.variableDeclrationFragment((ASTNode) d.fragments().get(0)).getName();
    assert $ != null;
    final SimpleName ¢ = make.newLowerCamelCase($, (d.getType() + "").split("<")[0]);
    return ¢.getIdentifier().equals($.getIdentifier()) ? null : new Tip("Rename paraemter " + $ + " to  " + ¢, getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, wrapper, r, g);
      }
    }.spanning(d);
  }
}
