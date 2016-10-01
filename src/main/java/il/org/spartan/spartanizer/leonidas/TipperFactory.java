package il.org.spartan.spartanizer.leonidas;

import java.util.*;

/** @author Ori Marcovitch
 * @since 2016 */
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

public class TipperFactory {
  public static <N extends ASTNode> UserDefinedTipper<N> tipper(final String _pattern, final String _replacement, final String description) {
    return new UserDefinedTipper<N>() {
      final ASTNode pattern = wizard.ast(_pattern);
      final String replacement = _replacement;

      @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @Override public Tip tip(final N n) {
        return new Tip(description(n), n) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final Map<String, ASTNode> enviroment = collectEnviroment(n);
            final Wrapper<String> $ = new Wrapper<>();
            $.set(replacement);
            wizard.ast(replacement).accept(new ASTVisitor() {
              @Override public boolean preVisit2(final ASTNode ¢) {
                if (iz.name(¢) && enviroment.containsKey(¢ + ""))
                  $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "") + ""));
                return true;
              }
            });
            r.replace(n, wizard.ast($.get()), g);
          }
        };
      }

      @Override protected boolean prerequisite(final N ¢) {
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
          for (int ¢ = 0; ¢ < pChildren.size(); collectEnviroment(pChildren.get(¢), nChildren.get(¢), enviroment), ++¢)
            ;
        }
        return enviroment;
      }
    };
  }
}
