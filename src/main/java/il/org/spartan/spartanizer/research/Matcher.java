package il.org.spartan.spartanizer.research;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Performs matching and pairing operations between <b>patterns</b> and
 * <b>ASTNodes</b>.<br>
 * <b>Patterns</b> are ASTNodes that can contain pattern variables (which are
 * also <b>ASTNodes</b>)<br>
 * as their descendants. each pattern variable matches a family of ASTNodes.<br>
 * To specify that a given ASTNode is a pattern variable, it should be of the
 * appropriate<br>
 * type, and its identifier should start with a special prefix and might
 * continue with a sequence of digits.<br>
 * <br>
 * The currently available pattern variables ('i' denotes a sequence of digits):
 * <ul>
 * <li>{@code $Xi, $Mi, $Ni, $Li}: Should be of type {@link Name} within the
 * pattern. Corresponds<br>
 * to: {@link Expression}, {@link MethodInvokation}, {@link Name},
 * {@link Literal}.</li>
 * <li>{@code $Bi();} : Corresponds to {@link Block} and {@link Statement}</li>
 * <li>{@code $Ai();} : Should be an argument within {@link MethodInvocation}.
 * <br>
 * Corresponds to arguments of {@link MethodInvocation}</li>
 * </ul>
 * @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @since 2016 */
public final class Matcher {
  private static final String $X_pattern = "\\$X\\d*\\(\\)"; // Expression
  private static final String $T_pattern = "\\$T\\d*"; // Type
  private static final String $A = "$A"; // Arguments
  private static final String $B = "$B"; // Block
  private static final String $D = "$D"; // Default value (null, 0 or false)
  private static final String $L = "$L"; // Literal
  private static final String $M = "$M"; // MethodInvocation
  private static final String $N = "$N"; // Name
  private static final String $SN = "$SN"; // SimpleName
  // private static final String $ST = "$ST"; // Single Statement (non block)
  final Supplier<ASTNode> patternSupplier;
  ASTNode _pattern;
  final String replacement;
  final Option[] options;

  ASTNode pattern() {
    return _pattern != null ? _pattern : (_pattern = patternSupplier.get());
  }

  @NotNull public static Matcher patternMatcher(@NotNull final String p, @NotNull final String s) {
    return patternMatcher(p, s, new Option[0]);
  }

  @NotNull public static Matcher patternMatcher(@NotNull final String p, @NotNull final String s, final Option[] _options) {
    return new Matcher(() -> extractStatementIfOne(ast(reformat(p))), s, _options);
  }

  @NotNull public static Matcher blockMatcher(@NotNull final String p, @NotNull final String s) {
    return blockMatcher(p, s, new Option[0]);
  }

  @NotNull public static Matcher blockMatcher(@NotNull final String p, @NotNull final String s, final Option[] _options) {
    return new Matcher(() -> wrapStatementIfOne(ast(reformat(p))), s, _options);
  }

  private Matcher(final Supplier<ASTNode> _patternSupplier, @NotNull final String r, final Option[] _options) {
    patternSupplier = _patternSupplier;
    replacement = reformat(r);
    options = _options;
  }

  public boolean blockMatches(final Block ¢) {
    return blockMatches(wrapStatementIfOne(pattern()), ¢) && conditions(¢);
  }

  private boolean conditions(final Block ¢) {
    return (!containsOption(Option.LAST_IN_BLOCK) || lastInBlock(¢))//
        && (!containsOption(Option.FIRST_IN_BLOCK) || firstInBlock(¢));
  }

  @Nullable private static Block wrapStatementIfOne(final ASTNode pattern) {
    return az.block(iz.block(pattern) ? pattern : ast("{" + pattern + "}"));
  }

  private boolean lastInBlock(final Block ¢) {
    final ASTNode[] $ = getMatchedNodes(¢);
    return $[$.length - 1].equals(last(statements(¢)));
  }

  private boolean firstInBlock(final Block ¢) {
    return getMatchedNodes(¢)[0].equals(first(statements(¢)));
  }

  private boolean containsOption(final Option o) {
    return Stream.of(options).anyMatch(λ -> λ.equals(o));
  }

  private static boolean blockMatches(final ASTNode p, final Block n) {
    if (!iz.block(p))
      return false;
    final List<Statement> sp = statements(az.block(p)), sn = statements(n);
    if (sp.size() > sn.size())
      return false;
    for (int ¢ = 0; ¢ <= sn.size() - sp.size(); ++¢)
      if (statementsMatch(sp, sn.subList(¢, ¢ + sp.size())))
        return true;
    return false;
  }

  //
  /** Tries to match a pattern <b>p</b> to a given ASTNode <b>n</b>, using<br>
   * the matching rules. For more info about these rules, see {@link Matcher}.
   * @param p pattern to match against.
   * @param ¢ ASTNode
   * @return True iff <b>n</b> matches the pattern <b>p</b>. */
  public boolean matches(final ASTNode ¢) {
    return matchesAux(pattern(), ¢, new HashMap<>());
  }

  @SuppressWarnings("boxing") public static Pair<Integer, Integer> getBlockMatching(final Block p, final Block n) {
    final List<Statement> $ = statements(p), sn = statements(n);
    for (int ¢ = 0; ¢ <= sn.size() - $.size(); ++¢)
      if (statementsMatch($, sn.subList(¢, ¢ + $.size())))
        return new Pair<>(¢, ¢ + $.size());
    return null;
  }

  private static boolean statementsMatch(@NotNull final List<Statement> sp, @NotNull final List<Statement> subList) {
    for (int ¢ = 0; ¢ < sp.size(); ++¢)
      if (!matchesAux(sp.get(¢), subList.get(¢), new HashMap<>()))
        return false;
    return true;
  }

  /** Validates that matched variables are the same in all matching places. */
  private static boolean consistent(@NotNull final Map<String, String> ids, final String id, final String s) {
    ids.putIfAbsent(id, s);
    return ids.get(id).equals(s);
  }

  private static boolean consistent(@NotNull final Map<String, String> ids, final String id, final ASTNode n) {
    return consistent(ids, id, n + "");
  }

  private static boolean matchesAux(@Nullable final ASTNode $, @Nullable final ASTNode n, @NotNull final Map<String, String> ids) {
    if ($ == null || n == null)
      return false;
    if (is$X($))
      return iz.expression(n) && consistent(ids, $ + "", n + "");
    if (is$T($))
      return iz.type(n) && consistent(ids, $ + "", n + "");
    if (iz.simpleName($) || iz.name($) && ($ + "").startsWith($N))
      return sameName($, n, ids);
    if (iz.literal($))
      return sameLiteral($, n);
    if (isBlockVariable($))
      return matchesBlock(n) && consistent(ids, blockVariableName($), n + "");
    if (isMethodInvocationAndHas$AArgument($) && !isMethodInvocationAndConsistentWith$AArgument($, n, ids))
      return false;
    if (isClassInstanceCreationAndHas$AArgument($))
      return isClassInstanceCreationAndConsistentWith$AArgument($, n) && Recurser.children(n).size() == Recurser.children($).size();
    if (differentTypes($, n))
      return false;
    if (iz.literal($))
      return ($ + "").equals(n + "");
    if (assignmentWithDifferentOperators($, n))
      return false;
    if (iz.infixExpression($) && !iz.parenthesizedExpression($))
      return sameOperator($, n) && sameOperands($, n, ids);
    final List<ASTNode> pChildren = allChildren($, $), nChildren = allChildren(n, $);
    if (nChildren.size() != pChildren.size())
      return false;
    for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
      if (!matchesAux(pChildren.get(¢), nChildren.get(¢), ids))
        return false;
    return true;
  }

  private static boolean sameOperands(final ASTNode $, final ASTNode n, @NotNull final Map<String, String> ids) {
    final List<Expression> $Operands = extract.allOperands(az.infixExpression($)), nOperands = extract.allOperands(az.infixExpression(n));
    if ($Operands.size() != nOperands.size())
      return false;
    for (int ¢ = 0; ¢ < $Operands.size(); ++¢)
      if (!matchesAux($Operands.get(¢), nOperands.get(¢), ids))
        return false;
    return true;
  }

  private static boolean sameOperator(final ASTNode $, final ASTNode n) {
    return az.infixExpression($).getOperator().equals(az.infixExpression(n).getOperator());
  }

  private static boolean assignmentWithDifferentOperators(final ASTNode $, final ASTNode n) {
    return iz.assignment($) && !operator(az.assignment($)).equals(operator(az.assignment(n)));
  }

  @SuppressWarnings("unchecked") @Nullable private static List<ASTNode> allChildren(final ASTNode ¢, final ASTNode p) {
    final List<ASTNode> $ = (List<ASTNode>) Recurser.children(¢);
    if (iz.methodInvocation(¢)) {
      if (!isMethodInvocationAndHas$AArgument(p))
        $.addAll(arguments(az.methodInvocation(¢)));
      if (haz.expression(az.methodInvocation(¢)))
        $.add(expression(az.methodInvocation(¢)));
    }
    if (iz.forStatement(¢)) {
      $.addAll(initializers(az.forStatement(¢)));
      $.add(condition(az.forStatement(¢)));
      $.addAll(updaters(az.forStatement(¢)));
    }
    if (iz.tryStatement(¢))
      $.addAll(az.tryStatement(¢).catchClauses());
    if (iz.variableDeclarationExpression(¢))
      $.addAll(fragments(az.variableDeclarationExpression(¢)));
    if (iz.variableDeclarationStatement(¢))
      $.addAll(fragments(az.variableDeclarationStatement(¢)));
    return $;
  }

  private static boolean is$T(final ASTNode p) {
    return iz.type(p) && matches$T(p + "");
  }

  private static boolean is$X(final ASTNode p) {
    return iz.methodInvocation(p) && matches$X(p + "");
  }

  private static boolean matches$X(@NotNull final String p) {
    return p.matches($X_pattern);
  }

  private static boolean matches$T(@NotNull final String p) {
    return p.matches($T_pattern);
  }

  private static boolean isMethodInvocationAndConsistentWith$AArgument(final ASTNode p, final ASTNode n, @NotNull final Map<String, String> ids) {
    return iz.methodInvocation(n) && sameName(az.methodInvocation(p).getName(), az.methodInvocation(n).getName(), ids)
        && consistent(ids, first(arguments(az.methodInvocation(p))) + "", az.methodInvocation(n).arguments() + "");
  }

  private static boolean isMethodInvocationAndHas$AArgument(final ASTNode p) {
    return iz.methodInvocation(p) && az.methodInvocation(p).arguments().size() == 1 && (first(arguments(az.methodInvocation(p))) + "").startsWith($A);
  }

  private static boolean isClassInstanceCreationAndConsistentWith$AArgument(final ASTNode p, final ASTNode n) {
    return isClassInstanceCreationAndConsistentWith$AArgument(n, az.classInstanceCreation(p));
  }

  public static boolean isClassInstanceCreationAndConsistentWith$AArgument(final ASTNode n, @NotNull final ClassInstanceCreation c,
      @NotNull final Map<String, String> ids) {
    return iz.classInstanceCreation(n) && sameName(c.getType(), az.classInstanceCreation(n).getType(), ids)
        && consistent(ids, first(arguments(c)) + "", az.classInstanceCreation(n).arguments() + "");
  }

  private static boolean isClassInstanceCreationAndHas$AArgument(final ASTNode p) {
    return iz.classInstanceCreation(p) && az.classInstanceCreation(p).arguments().size() == 1
        && (first(arguments(az.classInstanceCreation(p))) + "").startsWith($A);
  }

  private static boolean sameLiteral(final ASTNode p, final ASTNode n) {
    return iz.literal(n) && (p + "").equals(n + "");
  }

  private static boolean differentTypes(@NotNull final ASTNode p, @NotNull final ASTNode n) {
    return n.getNodeType() != p.getNodeType();
  }

  private static String blockVariableName(final ASTNode p) {
    return az.methodInvocation(az.expressionStatement(first(statements(az.block(p)))).getExpression()).getName().getFullyQualifiedName();
  }

  private static boolean isBlockVariable(final ASTNode p) {
    if (!iz.block(p) || statements(az.block(p)).size() != 1)
      return false;
    final Statement $ = first(statements(az.block(p)));
    return iz.expressionStatement($) && iz.methodInvocation(az.expressionStatement($).getExpression()) && blockVariableName(p).startsWith($B);
  }

  /** Checks if node is a block or statement
   * @param ¢
   * @return */
  private static boolean matchesBlock(final ASTNode ¢) {
    return iz.block(¢) || iz.statement(¢);
  }

  private static boolean sameName(final ASTNode p, final ASTNode n, @NotNull final Map<String, String> ids) {
    final String $ = p + "";
    if ($.startsWith("$")) {
      if ($.startsWith($M))
        return iz.methodInvocation(n) && consistent(ids, $, n);
      if ($.startsWith($SN))
        return iz.simpleName(n) && consistent(ids, $, n);
      if ($.startsWith($N))
        return iz.name(n) && consistent(ids, $, n);
      if ($.startsWith($L))
        return iz.literal(n) && consistent(ids, $, n);
      if ($.startsWith($D))
        return iz.defaultLiteral(n) && consistent(ids, $, n);
    }
    return iz.name(n) && $.equals(identifier(az.name(n)));
  }

  /** Pairs variables from a pattern <b>p</b> with their corresponding
   * ASTNodes<br>
   * from <b>n</b> (as strings), using the matching rules. For more info about
   * these rules, see {@link Matcher}.<br>
   * This method doesn't verify that <b>n</b> indeed matches <b>p</b>.<br>
   * Examples:<br>
   * <table border='1'>
   * <tr>
   * <td>Pattern</td>
   * <td>n</td>
   * <td>Resulting mapping</td>
   * </tr>
   * <td>$X ? y == 17 : $X2</td>
   * <td>x == 7 ? y == 17 : 9</td>
   * <td>{'$X':'x == 7', '$X2':'9'}</td>
   * </tr>
   * </tr>
   * <td>if(true) $B();</td>
   * <td>if(true) foo();</td>
   * <td>{'$B();':'foo();'}</td>
   * </tr>
   * </tr>
   * <td>if($X) $N($A);</td>
   * <td>if (o == null) print(8);</td>
   * <td>{'$X':'o == null', '$N':'print', '$A':'8'}</td>
   * </tr>
   * </table>
   * @param p Pattern to match against
   * @param n ASTNode
   * @param enviroment
   * @return Mapping between variables and their corresponding elements (both as
   *         strings). */
  @NotNull public Map<String, String> collectEnviroment(final ASTNode n, @NotNull final Map<String, String> enviroment) {
    return collectEnviroment(pattern(), n, enviroment);
  }

  @NotNull private static Map<String, String> collectEnviroment(final ASTNode p, final ASTNode n, @NotNull final Map<String, String> $) {
    if (startsWith$notBlock(p))
      $.put(p + "", n + "");
    else if (isBlockVariable(p))
      $.put(blockVariableName(p) + "();", n + "");
    else {
      if (isMethodInvocationAndHas$AArgument(p))
        $.put(argumentsId(p), matchingArguments(n) + "");
      final List<ASTNode> pChildren = !iz.infixExpression(p) ? allChildren(p, p) : infixExpressionOperands(p);
      for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
        collectEnviroment(pChildren.get(¢), (!iz.infixExpression(p) ? allChildren(n, p) : infixExpressionOperands(n)).get(¢), $);
    }
    return $;
  }

  @SuppressWarnings("unchecked") @Nullable private static List<ASTNode> infixExpressionOperands(final ASTNode p) {
    return (List<ASTNode>) (List<?>) extract.allOperands(az.infixExpression(p));
  }

  private static boolean startsWith$notBlock(final ASTNode p) {
    return is$X(p)//
        || is$T(p)
        || iz.name(p) //
            && ((p + "").startsWith($M) //
                || (p + "").startsWith($SN) //
                || (p + "").startsWith($N) //
                || (p + "").startsWith($L) //
                || (p + "").startsWith($D)) //
    ;
  }

  @NotNull public Map<String, ASTNode> collectEnviromentNodes(final ASTNode n, @NotNull final Map<String, ASTNode> enviroment) {
    return collectEnviromentNodes(pattern(), n, enviroment);
  }

  @NotNull private static Map<String, ASTNode> collectEnviromentNodes(final ASTNode p, final ASTNode n, @NotNull final Map<String, ASTNode> $) {
    if (is$X(p))
      $.put(name(az.methodInvocation(p)) + "", n);
    else if (startsWith$notBlock(p))
      $.put(p + "", n);
    else if (isBlockVariable(p))
      $.put(blockVariableName(p), n);
    else {
      final List<ASTNode> pChildren = allChildren(p, p);
      for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
        collectEnviromentNodes(pChildren.get(¢), allChildren(n, p).get(¢), $);
    }
    return $;
  }

  @NotNull private static String argumentsId(final ASTNode p) {
    return first(arguments(az.methodInvocation(p))) + "";
  }

  private static String matchingArguments(final ASTNode ¢) {
    final String $ = arguments(az.methodInvocation(¢)) + "";
    return $.substring(1, $.length() - 1);
  }

  static String reformat(@NotNull final String ¢) {
    return ¢.replaceAll("\\$B\\d*", "{$0\\(\\);}").replaceAll("\\$X\\d*", "$0\\(\\)");
  }

  static ASTNode extractStatementIfOne(final ASTNode ¢) {
    return !iz.block(¢) || statements(az.block(¢)).size() != 1 ? ¢ : first(statements(az.block(¢)));
  }

  <N extends ASTNode> ASTNode replacement(final N n) {
    final Map<String, String> enviroment = collectEnviroment(n, new HashMap<>());
    final Wrapper<String> $ = new Wrapper<>();
    $.set(replacement);
    enviroment.keySet().stream().filter(Matcher::needsSpecialReplacement).forEach(λ -> $.set($.get().replace(λ, enviroment.get(λ) + "")));
    ast(replacement).accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (iz.name(¢) && enviroment.containsKey(¢ + ""))
          $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "").replace("\\", "\\\\").replace("$", "\\$") + ""));
        return true;
      }
    });
    return extractStatementIfOne(ast($.get()));
  }

  @NotNull @SuppressWarnings("boxing") public ASTNode[] getMatchedNodes(final Block b) {
    final Pair<Integer, Integer> idxs = getBlockMatching(wrapStatementIfOne(pattern()), b);
    final ASTNode[] $ = new ASTNode[idxs.second - idxs.first];
    for (int ¢ = idxs.first; ¢ < idxs.second; ++¢)
      $[¢ - idxs.first] = statements(b).get(idxs.first);
    return $;
  }

  @Nullable ASTNode blockReplacement(final Block n) {
    final Pair<Integer, Integer> p = getBlockMatching(wrapStatementIfOne(pattern()), az.block(n));
    final String matching = stringifySubBlock(n, Unbox.it(p.first), Unbox.it(p.second));
    final Map<String, String> enviroment = collectEnviroment(ast(matching), new HashMap<>());
    final Wrapper<String> $ = new Wrapper<>(replacement);
    enviroment.keySet().stream().filter(Matcher::needsSpecialReplacement).forEach(λ -> $.set($.get().replace(λ, enviroment.get(λ) + "")));
    ast(replacement).accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (iz.name(¢) && enviroment.containsKey(¢ + ""))
          $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "").replace("\\", "\\\\").replace("$", "\\$") + ""));
        return true;
      }
    });
    return ast(stringifySubBlock(n, 0, p.first.intValue()) + $.get() + stringifySubBlock(n, p.second.intValue()));
  }

  private static boolean needsSpecialReplacement(@NotNull final String ¢) {
    return ¢.startsWith($B) || matches$X(¢);
  }

  @NotNull private static <N extends ASTNode> String stringifySubBlock(final N n, final int start) {
    final int $ = statements(az.block(n)).size();
    return start >= $ ? "" : stringifySubBlock(n, start, $);
  }

  @NotNull private static <N extends ASTNode> String stringifySubBlock(final N n, final int start, final int end) {
    return start >= end ? "" : statements(az.block(n)).subList(start, end).stream().map(λ -> λ + "").reduce("", (x, y) -> x + y);
  }

  public ASTNode getMatching(final ASTNode n, final String s) {
    return collectEnviromentNodes(n, new HashMap<>()).get(s);
  }

  public enum Option {
    LAST_IN_BLOCK, FIRST_IN_BLOCK
  }
}
