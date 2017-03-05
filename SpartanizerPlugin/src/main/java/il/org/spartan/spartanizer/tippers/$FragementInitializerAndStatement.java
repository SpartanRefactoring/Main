package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-03-03 */
public abstract class $FragementInitializerAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {
  private static final long serialVersionUID = -4292061740281133237L;
  protected VariableDeclarationFragment fragment;
  protected Statement nextStatement;
  protected ASTRewrite rewrite;
  protected TextEditGroup editGroup;

  @Override public abstract String description(VariableDeclarationFragment f);

  abstract ASTRewrite go();

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement s, final TextEditGroup g) {
    nextStatement = s;
    fragment = f;
    rewrite = r;
    editGroup = g;
    return go();
  }

  @Override public boolean prerequisite(@SuppressWarnings("unused") final VariableDeclarationFragment __) {
    return true;
  }
}
