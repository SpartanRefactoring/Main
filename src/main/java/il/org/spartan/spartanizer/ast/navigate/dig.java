package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-10-07 */
public interface dig {
  @NotNull static List<String> stringLiterals(@Nullable final ASTNode n) {
    @NotNull final List<String> $ = new ArrayList<>();
    if (n == null)
      return $;
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(@NotNull final StringLiteral ¢) {
        $.add(¢.getLiteralValue());
        return true;
      }
    });
    return $;
  }
}
