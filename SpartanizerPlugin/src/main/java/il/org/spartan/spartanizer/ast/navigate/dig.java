package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016-10-07 */
public interface dig {
  @NotNull
  static List<String> stringLiterals(@Nullable final ASTNode n) {
    final List<String> $ = new ArrayList<>();
    if (n == null)
      return $;
    n.accept(new ASTVisitor() {
      @Override public boolean visit(@NotNull final StringLiteral ¢) {
        $.add(¢.getLiteralValue());
        return true;
      }
    });
    return $;
  }
}
