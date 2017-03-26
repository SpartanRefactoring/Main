package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replaces name of variables named "_" into "__"
 * @author Ori Roth
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 * @since 2016/05/08 */
public final class FragmentRenameUnderscoreToDoubleUnderscore<N extends VariableDeclaration> //
    extends AbstractVariableDeclarationChangeName<N>//
    implements TipperCategory.Annonimization {
  private static final long serialVersionUID = 0x28D6776DBEFFA844L;

  @Override  public String description(final N ¢) {
    return "Use double underscore instead of " + trivia.gist(¢);
  }

  @Override protected boolean change( final N ¢) {
    return "_".equals(¢.getName() + "");
  }

  @Override protected SimpleName replacement( final N ¢) {
    return ¢.getAST().newSimpleName("__");
  }
}
