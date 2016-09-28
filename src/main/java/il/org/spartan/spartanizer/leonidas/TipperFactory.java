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
    return new Matcher().matches(p, n);
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

class Matcher {
  private static boolean sameOperator(final ASTNode p, final ASTNode n) {
    switch (p.getNodeType()) {
      case ASTNode.PREFIX_EXPRESSION:
        if (!step.operator((PrefixExpression) p).equals(step.operator((PrefixExpression) n)))
          return false;
        break;
      case ASTNode.INFIX_EXPRESSION:
        if (!step.operator((InfixExpression) p).equals(step.operator((InfixExpression) n)))
          return false;
        break;
      case ASTNode.POSTFIX_EXPRESSION:
        if (!step.operator((PostfixExpression) p).equals(step.operator((PostfixExpression) n)))
          return false;
        break;
      case ASTNode.ASSIGNMENT:
        if (!step.operator((Assignment) p).equals(step.operator((Assignment) n)))
          return false;
        break;
      default:
        return true;
    }
    return true;
  }

  Map<String, ArrayList<ASTNode>> ids = new HashMap<>();

  public Matcher() {
  }

  public boolean matches(final ASTNode p, final ASTNode n) {
    if (iz.name(p))
      return sameName(p, n);
    if (n.getNodeType() != p.getNodeType())
      return false;
    if (iz.literal(p))
      return (p + "").equals(n + "");
    if (iz.containsOperator(p) && !sameOperator(p, n))
      return false;
    final List<? extends ASTNode> nChildren = Recurser.children(n);
    final List<? extends ASTNode> pChildren = Recurser.children(p);
    if (nChildren.size() != pChildren.size())
      return false;
    for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
      if (!matches(pChildren.get(¢), nChildren.get(¢)))
        return false;
    return true;
  }

  private boolean consistent(final ASTNode n, final String id) {
    if (!ids.containsKey(id))
      ids.put(id, new ArrayList<>());
    ids.get(id).add(n);
    for (final ASTNode other : ids.get(id))
      if (!(n + "").equals(other + ""))
        return false;
    return true;
  }

  private boolean sameName(final ASTNode p, final ASTNode n) {
    final String id = ((Name) p).getFullyQualifiedName();
    if (id.startsWith("$")) {
      if (id.startsWith("$X"))
        return n instanceof Expression && consistent(n, id);
      if (id.startsWith("$M"))
        return n instanceof MethodInvocation && consistent(n, id);
    }
    return n instanceof Name && id.equals(((Name) p).getFullyQualifiedName());
  }
}
