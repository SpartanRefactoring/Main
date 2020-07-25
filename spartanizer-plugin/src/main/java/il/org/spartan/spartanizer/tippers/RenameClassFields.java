package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Nominal;

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
    final TypeDeclaration ret = az.typeDeclaration(d.getParent());
    if (ret == null || !iz.private¢(d) && !iz.private¢(ret) || d.fragments().size() != 1 || iz.primitiveType(d.getType()))
      return null;
    final FieldDeclaration[] fields = ret.getFields();
    final Type t = d.getType();
    int count = 0;
    for (final FieldDeclaration ¢ : fields)
      if (t.equals(¢.getType()))
        ++count;
    if (count != 1)
      return null;
    final SimpleName $ = az.variableDeclrationFragment((ASTNode) d.fragments().get(0)).getName();
    assert $ != null;
    final SimpleName ¢ = wizard.newLowerCamelCase($, (d.getType() + "").split("<")[0]);
    return ¢.getIdentifier().equals($.getIdentifier()) ? null : new Tip("Rename paraemter " + $ + " to  " + ¢, getClass(), $) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        misc.rename($, ¢, ret, r, g);
      }
    }.spanning(d);
  }
}
