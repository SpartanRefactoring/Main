package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

public class TipperFactory {
  public static UserDefinedTipper<ASTNode> tipper(final String _pattern, final String _replacement, final String description) {
    return new UserDefinedTipper<ASTNode>() {
      final ASTNode pattern = wizard.ast(_pattern);
      final ASTNode replacement = wizard.ast(_replacement);

      @Override public String description(@SuppressWarnings("unused") final ASTNode __) {
        return description;
      }

      @Override public Tip tip(final ASTNode n) {
        return new Tip(description(n), n) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final Map<String, ASTNode> enviroment = collectEnviroment(n);
            final ASTNode $ = duplicate.of(replacement);
            $.accept(new ASTVisitor() {
              @Override public void preVisit(final ASTNode ¢) {
                if (!iz.name(¢))
                  return;
                final String id = ((Name) ¢).getFullyQualifiedName();
                if (enviroment.containsKey(id))
                  wizard.replace(¢, enviroment.get(id));
              }
            });
            r.replace(n, $, g);
          }
        };
      }

      @Override protected boolean prerequisite(final ASTNode ¢) {
        return Matcher.matches(pattern, ¢);
      }

      Map<String, ASTNode> collectEnviroment(final ASTNode ¢) {
        return collectEnviroment(pattern, ¢, new HashMap<>());
      }

      Map<String, ASTNode> collectEnviroment(final ASTNode p, final ASTNode n, final Map<String, ASTNode> enviroment) {
        if (iz.name(p)) {
          final String id = ((Name) p).getFullyQualifiedName();
          if (id.startsWith("$X") || id.startsWith("$M"))
            enviroment.put(id, n);
        } else {
          final List<? extends ASTNode> nChildren = Recurser.children(n);
          final List<? extends ASTNode> pChildren = Recurser.children(p);
          for (int ¢ = 0; ¢ < pChildren.size(); ++¢, collectEnviroment(pChildren.get(¢), nChildren.get(¢), enviroment))
            ;
        }
        return enviroment;
      }
    };
  }
}
