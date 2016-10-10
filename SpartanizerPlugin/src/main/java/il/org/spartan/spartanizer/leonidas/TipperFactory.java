package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @since 2016 */
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

public class TipperFactory {
  public static <N extends ASTNode> UserDefinedTipper<N> tipper(final String _pattern, final String _replacement, final String description) {
    return new UserDefinedTipper<N>() {
      final ASTNode pattern = wizard.ast(reformat$Bs(_pattern));
      final String replacement = reformat$Bs(_replacement);

      @Override public String description(@SuppressWarnings("unused") final N __) {
        return description;
      }

      @Override public Tip tip(final N n) {
        return new Tip(description(n), n, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            final Map<String, ASTNode> enviroment = collectEnviroment(n);
            final Wrapper<String> $ = new Wrapper<>();
            $.set(replacement);
            for (final String ¢ : enviroment.keySet())
              if (¢.startsWith("$B"))
                $.set($.get().replace(¢, enviroment.get(¢) + ""));
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

      @SuppressWarnings("unchecked") Map<String, ASTNode> collectEnviroment(final ASTNode p, final ASTNode n, final Map<String, ASTNode> enviroment) {
        if (iz.name(p)) {
          final String id = az.name(p).getFullyQualifiedName();
          if (id.startsWith("$X") || id.startsWith("$M"))
            enviroment.put(id, n);
        } else if (isBlockVariable(p))
          enviroment.put(blockName(p) + "();", n);
        else {
          final List<? extends ASTNode> nChildren = Recurser.children(n);
          final List<? extends ASTNode> pChildren = Recurser.children(p);
          if (iz.methodInvocation(p)) {
            nChildren.addAll(az.methodInvocation(n).arguments());
            pChildren.addAll(az.methodInvocation(p).arguments());
          }
          for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
            collectEnviroment(pChildren.get(¢), nChildren.get(¢), enviroment);
        }
        return enviroment;
      }
    };
  }

  static boolean isBlockVariable(final ASTNode p) {
    return iz.expressionStatement(p) && iz.methodInvocation(az.expressionStatement(p).getExpression()) && blockName(p).startsWith("$B");
  }

  static String blockName(final ASTNode p) {
    return az.methodInvocation(az.expressionStatement(p).getExpression()).getName().getFullyQualifiedName();
  }

  static String reformat$Bs(final String ¢) {
    return ¢.replaceAll("\\$B\\d*", "$0\\(\\);");
  }
}
