package il.org.spartan.spartanizer.leonidas;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Matcher {
  public static boolean matches(final ASTNode p, final ASTNode n) {
    if (!iz.block(p))
      return new Matcher().matchesAux(p, n);
    if (!iz.block(n))
      return false;
    @SuppressWarnings("unchecked") final List<Statement> sp = az.block(p).statements();
    @SuppressWarnings("unchecked") final List<Statement> sn = az.block(n).statements();
    if (sp == null || sn == null || sp.size() > sn.size())
      return false;
    for (int ¢ = 0; ¢ <= sn.size() - sp.size(); ++¢)
      if (new Matcher().statementsMatch(sp, sn.subList(¢, ¢ + sp.size())))
        return true;
    return false;
  }

  @SuppressWarnings("boxing") public static Pair<Integer, Integer> getBlockMatching(final Block p, final Block n) {
    @SuppressWarnings("unchecked") final List<Statement> sp = p.statements();
    @SuppressWarnings("unchecked") final List<Statement> sn = n.statements();
    for (int ¢ = 0; ¢ <= sn.size() - sp.size(); ++¢)
      if (new Matcher().statementsMatch(sp, sn.subList(¢, ¢ + sp.size())))
        return new Pair<>(¢, ¢ + sp.size());
    return null;
  }

  /** @param sp
   * @param subList
   * @return */
  private boolean statementsMatch(final List<Statement> sp, final List<Statement> subList) {
    for (int ¢ = 0; ¢ < sp.size(); ++¢)
      if (!matchesAux(sp.get(¢), subList.get(¢)))
        return false;
    return true;
  }

  private static boolean sameOperator(final ASTNode p, final ASTNode n) {
    // I really hope these are the only options for operators (-Ori)
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

  /** Validates that matched variables are the same in all matching places. */
  private boolean consistent(final ASTNode n, final String id) {
    if (!ids.containsKey(id))
      ids.put(id, new ArrayList<>());
    ids.get(id).add(n);
    for (final ASTNode other : ids.get(id))
      if (!(n + "").equals(other + ""))
        return false;
    return true;
  }

  @SuppressWarnings("unchecked") private boolean matchesAux(final ASTNode p, final ASTNode n) {
    if (p == null || n == null)
      return false;
    if (iz.name(p))
      return sameName(p, n);
    if (iz.literal(p))
      return sameLiteral(p, n);
    if (isBlockVariable(p))
      return matchesBlock(n) && consistent(n, blockName(p));
    if (differentTypes(p, n))
      return false;
    if (iz.literal(p))
      return (p + "").equals(n + "");
    if (iz.containsOperator(p) && !sameOperator(p, n))
      return false;
    final List<? extends ASTNode> nChildren = Recurser.children(n);
    final List<? extends ASTNode> pChildren = Recurser.children(p);
    if (iz.methodInvocation(p)) {
      pChildren.addAll(az.methodInvocation(p).arguments());
      nChildren.addAll(az.methodInvocation(n).arguments());
    }
    if (nChildren.size() != pChildren.size())
      return false;
    for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
      if (!matchesAux(pChildren.get(¢), nChildren.get(¢)))
        return false;
    return true;
  }

  /** @param p
   * @param n
   * @return */
  private static boolean sameLiteral(final ASTNode p, final ASTNode n) {
    return iz.literal(n) && (p + "").equals(n + "");
  }

  private static boolean differentTypes(final ASTNode p, final ASTNode n) {
    return n.getNodeType() != p.getNodeType();
  }

  private static String blockName(final ASTNode p) {
    return az.methodInvocation(az.expressionStatement(p).getExpression()).getName().getFullyQualifiedName();
  }

  private static boolean isBlockVariable(final ASTNode p) {
    return iz.expressionStatement(p) && iz.methodInvocation(az.expressionStatement(p).getExpression()) && blockName(p).startsWith("$B");
  }

  /** Checks if node is a block or statement
   * @param ¢
   * @return */
  private static boolean matchesBlock(final ASTNode ¢) {
    return iz.block(¢) || iz.statement(¢);
  }

  private boolean sameName(final ASTNode p, final ASTNode n) {
    final String id = ((Name) p).getFullyQualifiedName();
    if (id.startsWith("$")) {
      if (id.startsWith("$X"))
        return n instanceof Expression && consistent(n, id);
      if (id.startsWith("$M"))
        return n instanceof MethodInvocation && consistent(n, id);
    }
    return n instanceof Name && id.equals(((Name) n).getFullyQualifiedName());
  }
}
