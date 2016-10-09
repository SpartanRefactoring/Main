package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** ???
 * @author Yossi Gil
 * @since 2016 */
public interface dig {
  static List<String> stringLiterals(final ASTNode ¢) {
    List<String> $ = new ArrayList<>();
    if(¢ instanceof StringLiteral)
      $.add(((StringLiteral)¢).getLiteralValue());
    return $;
  }
}
