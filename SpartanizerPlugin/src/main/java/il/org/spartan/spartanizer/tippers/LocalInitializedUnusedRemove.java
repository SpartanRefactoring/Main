package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** Remove unused variable
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-23 */
public final class LocalInitializedUnusedRemove extends LocalVariableInitialized implements TipperCategory.Deadcode {
  private static final long serialVersionUID = -855471283048149285L;

  public LocalInitializedUnusedRemove() {
    andAlso("Local variable is unused", () -> collect.usesOf(name).in(scope.of(name)).isEmpty());
  }

  @Override public String description() {
    return "Remove unused, uninitialized variable";
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove unused variable: " + trivia.gist(¢);
  }

  /** [[SuppressWarningsSpartan]] */
  @Override public Example[] examples() {
    return new Example[] { //
        convert("" //
            + "int print() {\n" //
            + "  int number = 1;\n" //
            + "  System.out.println(\"number\");\n" //
            + "}")
                .to("" //
                    + "int print() {\n" //
                    + "  System.out.println(\"number\");\n" //
                    + "}"), //
        ignores("" //
            + "int print() {\n" //
            + "  int number = 1;\n" //
            + "  System.out.println(number);\n" //
            + "}"), //
    };
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final Block b = az.block(declaration.getParent());
    if (b == null)
      return $;
    final ListRewrite l = $.getListRewrite(b, Block.STATEMENTS_PROPERTY);
    for (final Statement ¢ : wizard.decompose(initializer()))
      l.insertBefore(copy.of(¢), declaration, g);
    il.org.spartan.spartanizer.ast.factory.remove.deadFragment(current(), $, g);
    return $;
  }
}
