package il.org.spartan.spartanizer.leonidas;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Ori Marcovitch
 * @year 2016 */
public class Matcher {
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

  private Matcher() {
  }

  public static boolean matches(final ASTNode p, final ASTNode n) {
    return new Matcher().matchesAux(p, n);
  }

  private boolean matchesAux(final ASTNode p, final ASTNode n) {
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
      if (!matchesAux(pChildren.get(¢), nChildren.get(¢)))
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
