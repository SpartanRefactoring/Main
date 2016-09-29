package il.org.spartan.spartanizer.leonidas;

/** @author Ori Marcovitch
 * @year 2016 */
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;

public class TipperFactory {
  public static boolean matches(final ASTNode p, final ASTNode n) {
    return Matcher.matches(p, n);
  }

  /** @param p string to convert
   * @return AST */
  static ASTNode toAST(final String p) {
    switch (GuessedContext.find(p)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return into.cu(p);
      case EXPRESSION_LOOK_ALIKE:
        return into.e(p);
      case OUTER_TYPE_LOOKALIKE:
        return into.t(p);
      case STATEMENTS_LOOK_ALIKE:
        return into.s(p);
      default:
        break;
    }
    return null;
  }

  ASTNode pattern;
  ASTNode replacement;
  String description;

  public TipperFactory(final String pattern, final String replacement, final String description) {
    this.pattern = toAST(pattern);
    this.replacement = toAST(replacement);
    this.description = description;
  }

  public UserDefinedTipper<ASTNode> get() {
    return new UserDefinedTipper<ASTNode>() {
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
                if (id.startsWith("$"))
                  wizard.replace(¢, enviroment.get(id));
              }
            });
            r.replace(n, $, g);
          }
        };
      }

      @Override protected boolean prerequisite(final ASTNode ¢) {
        return matches(pattern, ¢);
      }
    };
  }

  public boolean matches(final ASTNode ¢) {
    return matches(pattern, ¢);
  }

  Map<String, ASTNode> collectEnviroment(final ASTNode ¢) {
    return collectEnviroment(pattern, ¢, new HashMap<>());
  }

  private Map<String, ASTNode> collectEnviroment(final ASTNode p, final ASTNode n, final Map<String, ASTNode> enviroment) {
    if (iz.name(p)) {
      final String id = ((Name) p).getFullyQualifiedName();
      if (id.startsWith("$"))
        enviroment.put(id, n);
    } else {
      final List<? extends ASTNode> nChildren = Recurser.children(n);
      final List<? extends ASTNode> pChildren = Recurser.children(p);
      for (int ¢ = 0; ¢ < pChildren.size(); ++¢, collectEnviroment(pChildren.get(¢), nChildren.get(¢), enviroment))
        ;
    }
    return enviroment;
  }
}

