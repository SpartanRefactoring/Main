package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalVariableInitializedStatement extends LocalVariableInitialized {
  private static final long serialVersionUID = 0x1B70489B1D1340L;

  public LocalVariableInitializedStatement() {
    andAlso(Proposition.of("Followed by statement", //
        () -> (nextStatement = extract.nextStatement(statement())) != null))//
            .andAlso(Proposition.not("Not used in subsequent initalizers", //
                () -> collect.usesOf(name).in(youngerSiblings()).isEmpty()));
  }

  @Override public abstract String description(VariableDeclarationFragment f);

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }

  protected Statement nextStatement;
}