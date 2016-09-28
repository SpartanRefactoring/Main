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
  ASTNode pattern;
  ASTNode replacement;
  String description;

  public TipperFactory(final String pattern, final String replacement, final String description) {
    this.pattern = toAST(pattern);
    this.replacement = toAST(replacement);
    this.description = description;
  }

  /** @param p string to convert
   * @return AST */
  static ASTNode toAST(String p) {
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

  public UserDefinedTipper<ASTNode> get() {
    return new UserDefinedTipper<ASTNode>() {
      Map<String, ASTNode> enviroment = new HashMap<>();

      @Override protected boolean prerequisite(ASTNode ¢) {
        return matches(pattern, ¢);
      }

      @Override public String description(@SuppressWarnings("unused") ASTNode __) {
        return description;
      }

      @Override public Tip tip(final ASTNode e) {
        return new Tip(description(e), e) {
          @Override public void go(ASTRewrite r, TextEditGroup g) {
            ASTNode $ = duplicate.of(replacement);
            $.accept(new ASTVisitor() {
              @Override public void preVisit(final ASTNode ¢) {
                if(iz.name(¢)){
                  String id = ((Name) ¢).getFullyQualifiedName();
                  if (id.startsWith("$")) {
//                    wizard.rebase(¢, enviroment.get(id));
                  }
                }
              }
            });
//            r.replace(e, ,g);
            
          }
        };
      }
    };
  }

  public boolean matches(ASTNode n) {
    return matches(pattern, n);
  }

  public static boolean matches(ASTNode p, ASTNode n) {
    return new Matcher().matches(p, n);
  }

//  private ASTNode replacement(ASTNode n) {
//    ASTNode $ = duplicate.of(replacement);
//    $.accept(new ASTVisitor() {
//      @Override public void preVisit(final ASTNode ¢) {
//      }
//    });
//    return $;
//  }
}