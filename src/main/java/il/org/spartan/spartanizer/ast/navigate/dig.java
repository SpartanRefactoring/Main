package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** ???
 * @author Yossi Gil
 * @since 2016 */
public interface dig {
  static List<String> stringLiterals(final ASTNode ¢) {
    List<String> $ = new ArrayList<>();
    if(¢ == null)
      return $;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("hiding") StringLiteral ¢){
        $.add(¢.getLiteralValue());
        return true;
      }
    });
    return $;
  }
}
