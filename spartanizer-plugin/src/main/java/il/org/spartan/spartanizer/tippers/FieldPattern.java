package il.org.spartan.spartanizer.tippers;

import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-15 */
public abstract class FieldPattern extends FragmentAmongFragments {
  private static final long serialVersionUID = 0x54EEEFC48BF86611L;
  protected FieldDeclaration declaration;

  public FieldPattern() {
    andAlso("Must be field variable", () -> (declaration = az.fieldDeclaration(parent)) != null);
  }
  @Override protected final List<VariableDeclarationFragment> siblings() {
    return step.fragments(declaration);
  }
  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param r
   * @param g */
  void remove(final ASTRewrite r, final TextEditGroup g) {
    r.remove(declaration.fragments().size() > 1 ? current() : declaration, g);
  }
}