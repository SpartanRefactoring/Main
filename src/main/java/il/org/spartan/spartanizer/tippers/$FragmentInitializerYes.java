package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-03 */
public abstract class $FragmentInitializerYes extends $Fragment {
  private static final long serialVersionUID = 4670996089257708020L;
  protected VariableDeclarationFragment fragment;
  protected Statement nextStatement;
  protected ASTRewrite rewrite;
  protected TextEditGroup editGroup;

  @Override public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && initializer() != null;
  }

  @Override @NotNull public abstract String description(VariableDeclarationFragment f);
}
