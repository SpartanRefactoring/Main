package il.org.spartan.spartanizer.research;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

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
 * <li><code>$Xi, $Mi, $Ni, $Li</code>: Should be of type {@link Name} within
 * the pattern. Corresponds<br>
 * to: {@link Expression}, {@link MethodInvokation}, {@link Name},
 * {@link Literal}.</li>
 * <li><code>$Bi();</code> : Corresponds to {@link Block} and {@link Statement}
 * </li>
 * <li><code>$Ai();</code> : Should be an argument within
 * {@link MethodInvocation}. <br>
 * Corresponds to arguments of {@link MethodInvocation}</li>
 * </ul>
 * @author Ori Marcovitch
 * @since 2016 */
public class Matcher {
  final Supplier<ASTNode> patternSupplier;
  ASTNode _pattern;
  final String replacement;
  final Option[] options;

  ASTNode pattern() {
    return _pattern != null ? _pattern : (_pattern = patternSupplier.get());
  }

  public static Matcher patternMatcher(final String p, final String s) {
    return patternMatcher(p, s, new Option[0]);
  }

  public static Matcher patternMatcher(final String p, final String s, Option[] _options) {
    return new Matcher(() -> extractStatementIfOne(wizard.ast(reformat(p))), s, _options);
  }

  public static Matcher blockMatcher(final String p, final String s) {
    return patternMatcher(p, s, new Option[0]);
  }

  public static Matcher blockMatcher(final String p, final String s, Option[] _options) {
    return new Matcher(() -> wrapStatementIfOne(wizard.ast(reformat(p))), s, _options);
  }

  /** @param pattern
   * @param replacement2
   * @param options */
  private Matcher(Supplier<ASTNode> _patternSupplier, String r, Option[] _options) {
    patternSupplier = _patternSupplier;
    replacement = reformat(r);
    options = _options;
  }

  public boolean blockMatches(final Block ¢) {
    return blockMatches(wrapStatementIfOne(pattern()), ¢) && conditions(¢);
  }

  private boolean conditions(final Block ¢) {
    return (!containsOption(Option.LAST) || lastInBlock(¢))//
        && (!containsOption(Option.FIRST) || firstInBlock(¢));
  }

  /** @param pattern
   * @return */
  private static Block wrapStatementIfOne(ASTNode pattern) {
    return az.block(iz.block(pattern) ? pattern : wizard.ast("{" + pattern + "}"));
  }

  /** @param pattern
   * @param ¢
   * @return */
  private boolean lastInBlock(Block ¢) {
    ASTNode[] ns = getMatchedNodes(¢);
    return ns[ns.length - 1].equals(last(statements(¢)));
  }

  /** @param pattern
   * @param ¢
   * @return */
  private boolean firstInBlock(Block ¢) {
    return getMatchedNodes(¢)[0].equals(first(statements(¢)));
  }

  /** @param o
   * @param o
   * @return */
  private boolean containsOption(Option o) {
    for (Option ¢ : options)
      if (¢.equals(o))
        return true;
    return false;
  }

  private boolean blockMatches(final ASTNode p, final Block n) {
    if (!iz.block(p))
      return false;
    final List<Statement> sp = statements(az.block(p));
    final List<Statement> sn = statements(n);
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

  @SuppressWarnings("boxing") public Pair<Integer, Integer> getBlockMatching(final Block p, final Block n) {
    final List<Statement> $ = statements(p);
    final List<Statement> sn = statements(n);
    for (int ¢ = 0; ¢ <= sn.size() - $.size(); ++¢)
      if (statementsMatch($, sn.subList(¢, ¢ + $.size())))
        return new Pair<>(¢, ¢ + $.size());
    return null;
  }

  /** @param sp
   * @param subList
   * @return */
  private boolean statementsMatch(final List<Statement> sp, final List<Statement> subList) {
    for (int ¢ = 0; ¢ < sp.size(); ++¢)
      if (!matchesAux(sp.get(¢), subList.get(¢), new HashMap<>()))
        return false;
    return true;
  }

  private static boolean sameOperator(final ASTNode p, final ASTNode n) {
    return (p + "").equals(n + "");
  }

  /** Validates that matched variables are the same in all matching places. */
  private static boolean consistent(final Map<String, String> ids, final String id, final String s) {
    ids.putIfAbsent(id, s);
    return ids.get(id).equals(s);
  }

  private boolean matchesAux(final ASTNode $, final ASTNode n, final Map<String, String> ids) {
    if ($ == null || n == null)
      return false;
    if (is$X($))
      return iz.expression(n) && consistent(ids, $ + "", n + "");
    if (is$T($))
      return iz.type(n) && consistent(ids, $ + "", n + "");
    if (iz.name($))
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
    if (iz.anyOperator($) && !sameOperator($, n))
      return false;
    final List<ASTNode> pChildren = gatherChildren($, $);
    final List<ASTNode> nChildren = gatherChildren(n, $);
    if (nChildren.size() != pChildren.size())
      return false;
    for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
      if (!matchesAux(pChildren.get(¢), nChildren.get(¢), ids))
        return false;
    return true;
  }

  @SuppressWarnings("unchecked") private static List<ASTNode> gatherChildren(final ASTNode ¢, final ASTNode p) {
    final List<ASTNode> $ = (List<ASTNode>) Recurser.children(¢);
    if (iz.methodInvocation(¢)) {
      if (!isMethodInvocationAndHas$AArgument(p))
        $.addAll(az.methodInvocation(¢).arguments());
      if (haz.expression(az.methodInvocation(¢)))
        $.add(step.expression(az.methodInvocation(¢)));
    }
    if (iz.forStatement(¢)) {
      $.addAll(step.initializers(az.forStatement(¢)));
      $.add(step.condition(az.forStatement(¢)));
      $.addAll(step.updaters(az.forStatement(¢)));
    }
    if (iz.variableDeclarationExpression(¢))
      $.addAll(step.fragments(az.variableDeclarationExpression(¢)));
    return $;
  }

  /** @param p
   * @return */
  private static boolean is$T(final ASTNode p) {
    return iz.type(p) && matches$T(p + "");
  }

  /** @param p
   * @return */
  private static boolean is$X(final ASTNode p) {
    return iz.methodInvocation(p) && matches$X(p + "");
  }

  private static boolean matches$X(final String p) {
    return p.matches("\\$X\\d*\\(\\)");
  }

  private static boolean matches$T(final String p) {
    return p.matches("\\$T\\d*");
  }

  /** @param n
   * @return */
  private static boolean isMethodInvocationAndConsistentWith$AArgument(final ASTNode p, final ASTNode n, final Map<String, String> ids) {
    return iz.methodInvocation(n) && sameName(az.methodInvocation(p).getName(), az.methodInvocation(n).getName(), ids)
        && consistent(ids, az.methodInvocation(p).arguments().get(0) + "", az.methodInvocation(n).arguments() + "");
  }

  /** @param p
   * @return */
  private static boolean isMethodInvocationAndHas$AArgument(final ASTNode p) {
    return iz.methodInvocation(p) && az.methodInvocation(p).arguments().size() == 1
        && (az.methodInvocation(p).arguments().get(0) + "").startsWith("$A");
  }

  /** @param n
   * @return */
  private boolean isClassInstanceCreationAndConsistentWith$AArgument(final ASTNode p, final ASTNode n) {
    return isClassInstanceCreationAndConsistentWith$AArgument(n, az.classInstanceCreation(p));
  }

  public static boolean isClassInstanceCreationAndConsistentWith$AArgument(final ASTNode n, final ClassInstanceCreation c,
      final Map<String, String> ids) {
    return iz.classInstanceCreation(n) && sameName(c.getType(), az.classInstanceCreation(n).getType(), ids)
        && consistent(ids, c.arguments().get(0) + "", az.classInstanceCreation(n).arguments() + "");
  }

  /** @param p
   * @return */
  private static boolean isClassInstanceCreationAndHas$AArgument(final ASTNode p) {
    return iz.classInstanceCreation(p) && az.classInstanceCreation(p).arguments().size() == 1
        && (az.classInstanceCreation(p).arguments().get(0) + "").startsWith("$A");
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

  private static String blockVariableName(final ASTNode p) {
    return az.methodInvocation(az.expressionStatement(step.statements(az.block(p)).get(0)).getExpression()).getName().getFullyQualifiedName();
  }

  private static boolean isBlockVariable(final ASTNode p) {
    if (!iz.block(p) || statements(az.block(p)).size() != 1)
      return false;
    final Statement $ = statements(az.block(p)).get(0);
    return iz.expressionStatement($) && iz.methodInvocation(az.expressionStatement($).getExpression()) && blockVariableName(p).startsWith("$B");
  }

  /** Checks if node is a block or statement
   * @param ¢
   * @return */
  private static boolean matchesBlock(final ASTNode ¢) {
    return iz.block(¢) || iz.statement(¢);
  }

  private static boolean sameName(final ASTNode p, final ASTNode n, final Map<String, String> ids) {
    final String $ = p + "";
    if ($.startsWith("$")) {
      if ($.startsWith("$M"))
        return iz.methodInvocation(n) && consistent(ids, $, n + "");
      if ($.startsWith("$N"))
        return iz.name(n) && consistent(ids, $, n + "");
      if ($.startsWith("$L"))
        return iz.literal(n) && consistent(ids, $, n + "");
    }
    return iz.name(n) && $.equals(step.identifier(az.name(n)));
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
  public Map<String, String> collectEnviroment(final ASTNode n, final Map<String, String> enviroment) {
    return collectEnviroment(pattern(), n, enviroment);
  }

  /** [[SuppressWarningsSpartan]] */
  private static Map<String, String> collectEnviroment(final ASTNode p, final ASTNode n, final Map<String, String> enviroment) {
    if (startsWith$notBlock(p))
      enviroment.put(p + "", n + "");
    else if (isBlockVariable(p))
      enviroment.put(blockVariableName(p) + "();", n + "");
    else {
      if (isMethodInvocationAndHas$AArgument(p))
        enviroment.put(argumentsId(p), arguments(n) + "");
      final List<ASTNode> pChildren = gatherChildren(p, p);
      final List<ASTNode> nChildren = gatherChildren(n, p);
      for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
        collectEnviroment(pChildren.get(¢), nChildren.get(¢), enviroment);
    }
    return enviroment;
  }

  private static boolean startsWith$notBlock(final ASTNode p) {
    return is$X(p) || iz.name(p) && ((p + "").startsWith("$M") || (p + "").startsWith("$N") || (p + "").startsWith("$L")) || is$T(p);
  }

  public Map<String, ASTNode> collectEnviromentNodes(final ASTNode n, final Map<String, ASTNode> enviroment) {
    return collectEnviromentNodes(pattern(), n, enviroment);
  }

  /** [[SuppressWarningsSpartan]] */
  private static Map<String, ASTNode> collectEnviromentNodes(final ASTNode p, final ASTNode n, final Map<String, ASTNode> enviroment) {
    if (is$X(p))
      enviroment.put(step.name(az.methodInvocation(p)) + "", n);
    else if (startsWith$notBlock(p))
      enviroment.put(p + "", n);
    else if (isBlockVariable(p))
      enviroment.put(blockVariableName(p), n);
    else {
      final List<ASTNode> pChildren = gatherChildren(p, p);
      final List<ASTNode> nChildren = gatherChildren(n, p);
      for (int ¢ = 0; ¢ < pChildren.size(); ++¢)
        collectEnviromentNodes(pChildren.get(¢), nChildren.get(¢), enviroment);
    }
    return enviroment;
  }

  /** @param p
   * @return */
  private static String argumentsId(final ASTNode p) {
    return az.methodInvocation(p).arguments().get(0) + "";
  }

  /** @param ¢
   * @return */
  private static String arguments(final ASTNode ¢) {
    final String $ = az.methodInvocation(¢).arguments() + "";
    return $.substring(1, $.length() - 1);
  }

  static String reformat(final String ¢) {
    return ¢.replaceAll("\\$B\\d*", "{$0\\(\\);}").replaceAll("\\$X\\d*", "$0\\(\\)");
  }

  static ASTNode extractStatementIfOne(final ASTNode ¢) {
    return !iz.block(¢) || statements(az.block(¢)).size() != 1 ? ¢ : statements(az.block(¢)).get(0);
  }

  <N extends ASTNode> ASTNode replacement(final N n) {
    final Map<String, String> enviroment = collectEnviroment(n, new HashMap<>());
    final Wrapper<String> $ = new Wrapper<>();
    $.set(replacement);
    for (final String ¢ : enviroment.keySet())
      if (needsSpecialReplacement(¢))
        $.set($.get().replace(¢, enviroment.get(¢) + ""));
    wizard.ast(replacement).accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (iz.name(¢) && enviroment.containsKey(¢ + ""))
          $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "").replace("\\", "\\\\").replace("$", "\\$") + ""));
        return true;
      }
    });
    return extractStatementIfOne(wizard.ast($.get()));
  }

  /** @param b
   * @param idxs
   * @return */
  @SuppressWarnings("boxing") public ASTNode[] getMatchedNodes(final Block b) {
    final Pair<Integer, Integer> idxs = getBlockMatching(wrapStatementIfOne(pattern()), b);
    final ASTNode[] $ = new ASTNode[idxs.second - idxs.first];
    for (int ¢ = idxs.first; ¢ < idxs.second; ++¢)
      $[¢ - idxs.first] = statements(b).get(idxs.first);
    return $;
  }

  ASTNode blockReplacement(final Block n) {
    final Pair<Integer, Integer> p = getBlockMatching(wrapStatementIfOne(pattern()), az.block(n));
    final String matching = stringifySubBlock(n, Unbox.it(p.first), Unbox.it(p.second));
    final Map<String, String> enviroment = collectEnviroment(wizard.ast(matching), new HashMap<>());
    final Wrapper<String> $ = new Wrapper<>(replacement);
    for (final String ¢ : enviroment.keySet())
      if (needsSpecialReplacement(¢))
        $.set($.get().replace(¢, enviroment.get(¢) + ""));
    wizard.ast(replacement).accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (iz.name(¢) && enviroment.containsKey(¢ + ""))
          $.set($.get().replaceFirst((¢ + "").replace("$", "\\$"), enviroment.get(¢ + "").replace("\\", "\\\\").replace("$", "\\$") + ""));
        return true;
      }
    });
    return wizard.ast(stringifySubBlock(n, 0, p.first.intValue()) + $.get() + stringifySubBlock(n, p.second.intValue()));
  }

  private static boolean needsSpecialReplacement(final String ¢) {
    return ¢.startsWith("$B") || matches$X(¢);
  }

  private static <N extends ASTNode> String stringifySubBlock(final N n, final int start) {
    final int $ = statements(az.block(n)).size();
    return start >= $ ? "" : stringifySubBlock(n, start, $);
  }

  private static <N extends ASTNode> String stringifySubBlock(final N n, final int start, final int end) {
    return start >= end ? "" : statements(az.block(n)).subList(start, end).stream().map(x -> x + "").reduce("", (x, y) -> x + y);
  }

  /** @param n
   * @param s
   * @return */
  public ASTNode getMatching(final ASTNode n, final String s) {
    return collectEnviromentNodes(n, new HashMap<>()).get(s);
  }

  public enum Option {
    LAST, FIRST;
  }
}
