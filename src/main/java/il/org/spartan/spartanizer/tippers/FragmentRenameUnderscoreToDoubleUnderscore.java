package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

import static il.org.spartan.utils.Example.*;

/** Replaces name of variables named "_" into "__"
 * @author Ori Roth
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 * @since 2016/05/08 */
public final class FragmentRenameUnderscoreToDoubleUnderscore<N extends VariableDeclaration> //
    extends AbstractVariableDeclarationChangeName<N>//
    implements TipperCategory.Annonimization {
  private static final long serialVersionUID = 0x28D6776DBEFFA844L;

  @Override public String description(final N ¢) {
    return "Use double underscore instead of " + trivia.gist(¢);
  }

  @Override public String description() {
    return "Use \"__\" instead of \"_\"";
  }

  @Override protected boolean change(final N ¢) {
    return "_".equals(¢.getName() + "");
  }

  /** [[SuppressWarningsSpartan]] */
  @Override public Example[] examples() {
    return new Example[] {
        convert("" //
            + "void f(int _) {\n" //
            + "}")
                .to("" //
                    + "void f(int __) {\n" //
                    + "}"), //
        convert("" //
            + "void f() {\n" //
            + "  int _ = 1;\n" + "}")
                .to("" //
                    + "void f() {\n" //
                    + "  int __ = 1;\n" //
                    + "}"), //
    };
  }

  @Override protected SimpleName replacement(final N ¢) {
    return ¢.getAST().newSimpleName("__");
  }
}
