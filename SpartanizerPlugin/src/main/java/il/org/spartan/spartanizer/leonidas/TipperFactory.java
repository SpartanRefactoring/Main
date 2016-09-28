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
      @Override protected boolean prerequisite(ASTNode ¢) {
        return matches(pattern, ¢);
      }

      @Override public String description(@SuppressWarnings("unused") ASTNode __) {
        return description;
      }

      @Override public Tip tip(final ASTNode n) {
        return new Tip(description(n), n) {
          @Override public void go(ASTRewrite r, TextEditGroup g) {
            Map<String, ASTNode> enviroment = collectEnviroment(n);
            ASTNode $ = duplicate.of(replacement);
            $.accept(new ASTVisitor() {
              @Override public void preVisit(final ASTNode ¢) {
                if (!iz.name(¢))
                  return;
                String id = ((Name) ¢).getFullyQualifiedName();
                if (id.startsWith("$"))
                  wizard.replace(¢, enviroment.get(id));
              }
            });
            r.replace(n, $, g);
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

  Map<String, ASTNode> collectEnviroment(ASTNode n) {
    return collectEnviroment(pattern, n, new HashMap<>());
  }

  private Map<String, ASTNode> collectEnviroment(ASTNode p, ASTNode n, Map<String, ASTNode> enviroment) {
    if (iz.name(p)) {
      String id = ((Name) p).getFullyQualifiedName();
      if (id.startsWith("$"))
        enviroment.put(id, n);
    } else {
      List<? extends ASTNode> nChildren = Recurser.children(n);
      List<? extends ASTNode> pChildren = Recurser.children(p);
      for (int i = 0; i < pChildren.size(); ++i)
        collectEnviroment(pChildren.get(i), nChildren.get(i), enviroment);
    }
    return enviroment;
  }
}

class Matcher {
  Map<String, ArrayList<ASTNode>> ids = new HashMap<>();

  public Matcher() {
  }

  public boolean matches(ASTNode p, ASTNode n) {
    if (iz.name(p))
      return sameName(p, n);
    if (n.getNodeType() != p.getNodeType())
      return false;
    if (iz.literal(p))
      return p.toString().equals(n.toString());
    if (iz.containsOperator(p) && !sameOperator(p, n))
      return false;
    List<? extends ASTNode> nChildren = Recurser.children(n);
    List<? extends ASTNode> pChildren = Recurser.children(p);
    if (nChildren.size() != pChildren.size())
      return false;
    for (int i = 0; i < pChildren.size(); ++i)
      if (!matches(pChildren.get(i), nChildren.get(i)))
        return false;
    return true;
  }

  private boolean sameName(ASTNode p, ASTNode n) {
    String id = ((Name) p).getFullyQualifiedName();
    if (id.startsWith("$")) {
      if (id.startsWith("$X"))
        return (n instanceof Expression) && consistent(n, id);
      if (id.startsWith("$M"))
        return (n instanceof MethodInvocation) && consistent(n, id);
    }
    return (n instanceof Name) && id.equals(((Name) p).getFullyQualifiedName());
  }

  private static boolean sameOperator(ASTNode p, ASTNode n) {
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

  private boolean consistent(ASTNode n, String id) {
    if (!ids.containsKey(id))
      ids.put(id, new ArrayList<>());
    ids.get(id).add(n);
    for (ASTNode other : ids.get(id))
      if (!n.toString().equals(other.toString()))
        return false;
    return true;
  }
}
