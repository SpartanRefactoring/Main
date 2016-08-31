package il.org.spartan.refactoring.utils;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.eclipse.jdt.core.dom.rewrite.*;

public interface wizard {
  @SuppressWarnings("serial") Map<Operator, Operator> conjugate = new HashMap<Operator, Operator>() {
    {
      put(GREATER, LESS);
      put(LESS, GREATER);
      put(GREATER_EQUALS, LESS_EQUALS);
      put(LESS_EQUALS, GREATER_EQUALS);
    }
  };

  PrefixExpression.Operator MINUS1 = PrefixExpression.Operator.MINUS;

  PrefixExpression.Operator PLUS1 = PrefixExpression.Operator.PLUS;

  InfixExpression.Operator MINUS2 = InfixExpression.Operator.MINUS;

  InfixExpression.Operator PLUS2 = InfixExpression.Operator.PLUS;
  
  InfixExpression.Operator TIMES = InfixExpression.Operator.TIMES;
  
  InfixExpression.Operator DIVIDE = InfixExpression.Operator.DIVIDE;

  PostfixExpression.Operator DECREMENT_POST = PostfixExpression.Operator.DECREMENT;

  PostfixExpression.Operator INCREMENT_POST = PostfixExpression.Operator.INCREMENT;

  PrefixExpression.Operator DECREMENT_PRE = PrefixExpression.Operator.DECREMENT;

  PrefixExpression.Operator INCREMENT_PRE = PrefixExpression.Operator.INCREMENT;

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String asString(final ASTNode ¢) {
    return removeWhites(wizard.body(¢));
  }

  static String body(final ASTNode ¢) {
    return tide.clean(¢.toString());
  }

  /** the function checks if all the given assignments have the same left hand
   * side(variable) and operator
   * @param base The assignment to compare all others to
   * @param as The assignments to compare
   * @return true if all assignments has the same left hand side and operator as
   *         the first one or false otherwise */
  static boolean compatible(final Assignment base, final Assignment... as) {
    if (hasNull(base, as))
      return false;
    for (final Assignment a : as)
      if (wizard.incompatible(base, a))
        return false;
    return true;
  }

  /** @param cmpTo the assignment operator to compare all to
   * @param os A unknown number of assignments operators
   * @return true if all the operator are the same or false otherwise */
  static boolean compatibleOps(final Assignment.Operator cmpTo, final Assignment.Operator... os) {
    if (hasNull(cmpTo, os))
      return false;
    for (final Assignment.Operator ¢ : os)
      if (¢ == null || ¢ != cmpTo)
        return false;
    return true;
  }

  /** Compute the "de Morgan" conjugate of the operator present on an
   * {@link InfixExpression}.
   * @param e an expression whose operator is either
   *        {@link Operator#CONDITIONAL_AND} or {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the operator present on the
   *         parameter is {@link Operator#CONDITIONAL_OR}, or
   *         {@link Operator#CONDITIONAL_OR} if this operator is
   *         {@link Operator#CONDITIONAL_AND}
   * @see Restructure#deMorgan(Operator) */
  static Operator deMorgan(final InfixExpression e) {
    return wizard.deMorgan(e.getOperator());
  }

  /** Makes an opposite operator from a given one, which keeps its logical
   * operation after the node swapping. ¢.¢. "&" is commutative, therefore no
   * change needed. "<" isn'¢ commutative, but it has its opposite: ">=".
   * @param ¢ The operator to flip
   * @return correspond operator - ¢.¢. "<=" will become ">", "+" will stay
   *         "+". */
  static Operator conjugate(final Operator ¢) {
    return !wizard.conjugate.containsKey(¢) ? ¢ : wizard.conjugate.get(¢);
  }
  /** Compute the "de Morgan" conjugate of an operator.
   * @param o must be either {@link Operator#CONDITIONAL_AND} or
   *        {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the parameter is
   *         {@link Operator#CONDITIONAL_OR}, or {@link Operator#CONDITIONAL_OR}
   *         if the parameter is {@link Operator#CONDITIONAL_AND}
   * @see wizard#deMorgan(InfixExpression) */
  public static Operator deMorgan(final Operator o) {
    assert iz.deMorgan(o);
    return o.equals(CONDITIONAL_AND) ? CONDITIONAL_OR : CONDITIONAL_AND;
  }
  /** @param ns unknown number of nodes to check
   * @return true if one of the nodes is an Expression Statement of type Post or
   *         Pre Expression with ++ or -- operator. false if none of them are or
   *         if the given parameter is null. */
  static boolean containIncOrDecExp(final ASTNode... ns) {
    if (ns == null)
      return false;
    for (final ASTNode ¢ : ns)
      if (¢ != null && iz.isIncrementOrDecrement(¢))
        return true;
    return false;
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") static <¢ extends ASTNode> ¢ duplicate(final ¢ ¢) {
    return (¢) copySubtree(¢.getAST(), ¢);
  }

  /** Find the first matching expression to the given boolean (b).
   * @param b JD,
   * @param es JD
   * @return first expression from the given list (es) whose boolean value
   *         matches to the given boolean (b). */
  static Expression find(final boolean b, final List<Expression> es) {
    for (final Expression $ : es)
      if (iz.booleanLiteral($) && b == az.booleanLiteral($).booleanValue())
        return $;
    return null;
  }

  static VariableDeclarationFragment findDefinition(final VariableDeclarationStatement s, final SimpleName n) {
    return findVariableDeclarationFragment(step.fragments(s), n);
  }

  static VariableDeclarationFragment findVariableDeclarationFragment(final List<VariableDeclarationFragment> fs, final SimpleName ¢) {
    for (final VariableDeclarationFragment $ : fs)
      if (same(¢, $.getName()))
        return $;
    return null;
  }
  /** @param n the node from which to extract the proper fragment
   * @param e the name by which to look for the fragment
   * @return fragment if such with the given name exists or null otherwise (or
   *         if ¢ or name are null) */
  // TODO this seems a bug
  static VariableDeclarationFragment getDefinition(final ASTNode n, final Expression e) {
    return hasNull(n, e) || n.getNodeType() != VARIABLE_DECLARATION_STATEMENT || e.getNodeType() != SIMPLE_NAME ? null
        : findDefinition((VariableDeclarationStatement) n, (SimpleName) e);
  }
  static boolean incompatible(final Assignment a1, final Assignment a2) {
    return hasNull(a1, a2) || !compatibleOps(a1.getOperator(), a2.getOperator()) || !wizard.same(step.left(a1), step.left(a2));
  }
  @SuppressWarnings("unchecked") static ASTParser parser(final int kind) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    $.setKind(kind);
    $.setResolveBindings(false);
    final Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8); // or newer
    // version
    $.setCompilerOptions(options);
    return $;
  }
  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type.
   * @see ASTNode#copySubtree
   * @see ASTRewrite */
  @SuppressWarnings("unchecked") static <N extends ASTNode> N rebase(final N n, final AST t) {
    return (N) copySubtree(t, n);
  }
  /** As {@link step#elze(ConditionalExpression)} but returns the last else
   * statement in "if - else if - ... - else" statement
   * @param ¢ JD
   * @return last nested else statement */
  static Statement recursiveElze(final IfStatement ¢) {
    Statement $ = ¢.getElseStatement();
    while ($ instanceof IfStatement)
      $ = ((IfStatement) $).getElseStatement();
    return $;
  }
  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}¢
   * @param ¢ JD
   * @param es JD */
  static void removeAll(final boolean b, final List<Expression> es) {
    for (;;) {
      final Expression ¢ = find(b, es);
      if (¢ == null)
        return;
      es.remove(¢);
    }
  }
  /** Determine whether two nodes are the same, in the sense that their textual
   * representations is identical.
   * <p>
   * Each of the parameters may be <code><b>null</b></code>; a
   * <code><b>null</b></code> is only equal to< code><b>null</b></code>
   * @param n1 JD
   * @param n2 JD
   * @return <code><b>true</b></code> if the parameters are the same. */
  static boolean same(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType() && wizard.body(n1).equals(wizard.body(n2));
  }
  /** String wise comparison of all the given SimpleNames
   * @param ¢ string to compare all names to
   * @param es SimplesNames to compare by their string value to cmpTo
   * @return true if all names are the same (string wise) or false otherwise */
  static boolean same(final Expression e, final Expression... es) {
    for (final Expression ¢ : es)
      if (!same(¢, e))
        return false;
    return true;
  }
  /** Determine whether two lists of nodes are the same, in the sense that their
   * textual representations is identical.
   * @param ns1 first list to compare
   * @param ns2 second list to compare
   * @return are the lists equal string-wise */
  static <¢ extends ASTNode> boolean same(final List<¢> ns1, final List<¢> ns2) {
    if (ns1 == ns2)
      return true;
    if (ns1.size() != ns2.size())
      return false;
    for (int ¢ = 0; ¢ < ns1.size(); ++¢)
      if (!same(ns1.get(¢), ns2.get(¢)))
        return false;
    return true;
  }
}
