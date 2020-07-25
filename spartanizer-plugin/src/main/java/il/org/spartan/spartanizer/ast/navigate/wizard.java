package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.lisp.chop;
import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.operator;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jdt.core.dom.ASTNode.BLOCK;
import static org.eclipse.jdt.core.dom.ASTNode.BREAK_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.CONTINUE_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ENHANCED_FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.IF_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.LABELED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.RETURN_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.THROW_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.WHILE_STATEMENT;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import fluent.ly.English;
import fluent.ly.as;
import fluent.ly.note;
import fluent.ly.range;
import fluent.ly.the;
import fluent.ly.zero;
import il.org.spartan.tide;
import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.flatten;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.ast.safety.iz.literal;
import il.org.spartan.spartanizer.engine.parse;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.java.precedence;
import il.org.spartan.spartanizer.plugin.Wizard;
import il.org.spartan.spartanizer.tippers.FieldInitializedSerialVersionUIDToHexadecimal;
import il.org.spartan.utils.Bool;
import il.org.spartan.utils.Int;
import il.org.spartan.utils.fault;

/** Collection of definitions and functions that capture some of the quirks of
 * the {@link ASTNode} hierarchy.
 * @author Yossi Gil
 * @since 2014 */
@SuppressWarnings("OverlyComplexClass")
public interface wizard {
  @SuppressWarnings({ "unchecked", "serial" }) Map<Class<? extends ASTNode>, Integer> //
  classToNodeType = new LinkedHashMap<>() {
    {
      for (int nodeType = 1;; ++nodeType)
        try {
          put(ASTNode.nodeClassForType(nodeType), Integer.valueOf(nodeType));
        } catch (@SuppressWarnings("unused") final IllegalArgumentException ¢) {
          // We must suffer this exception; no other way to find the first
          // unused node __
          break;
        } catch (final Exception ¢) {
          note.bug(this, ¢);
          break;
        }
    }
  };
  String[] keywords = { //
      "synchronized", //
      "instanceof", //
      "implements", //
      "protected", //
      "interface", //
      "transient", //
      "abstract", //
      "volatile", //
      "strictfp", //
      "continue", //
      "boolean", //
      "package", //
      "private", //
      "extends", //
      "finally", //
      "default", //
      "double", //
      "return", //
      "native", //
      "public", //
      "static", //
      "throw", //
      "switch", //
      "import", //
      "throws", //
      "assert", //
      "const", //
      "catch", //
      "class", //
      "false", //
      "while", //
      "float", //
      "final", //
      "super", //
      "break", //
      "short", //
      "byte", //
      "case", //
      "long", //
      "null", //
      "goto", //
      "this", //
      "true", //
      "void", //
      "char", //
      "else", //
      "enum", //
      "new", //
      "int", //
      "try", //
      "for", //
      "do", //
      "if", //
  };
  List<Integer> loopTypes = as.ilist(WHILE_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, DO_STATEMENT);
  Class<?>[] np = { InfixExpression.class };
  IProgressMonitor nullProgressMonitor = new NullProgressMonitor();
  Bool resolveBinding = Bool.valueOf(false);


  static String getPackageNameFromSource(final ASTNode ¢) {
    return findFirst.instanceOf(PackageDeclaration.class).in(¢).getName() +"";
  }
  static String getPackageNameFromSource(final String source) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    $.setSource(source.toCharArray());
    return wizard.getPackageNameFromSource($.createAST(null));
  }
  static Expression addParenthesisIfNeeded(final Expression ¢) {
    return !isParethesisNeeded(¢) ? ¢ : subject.operand(¢).parenthesis();
  }
  static Expression applyDeMorgan(final InfixExpression $) {
    return subject.operands(hop.operands(flatten.of($)).stream().map(cons::not).collect(toList())).to(op.negate(operator($)));
  }
  /** Converts a string into an AST, depending on it's form, as determined
   * by @link{GuessedContext.find}.
   * @param javaSnippet string to convert
   * @return AST, if string is not a valid AST according to any form, then
   *         null */
  static ASTNode ast(final String javaSnippet) {
    switch (GuessedContext.find(javaSnippet)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return parse.cu(javaSnippet);
      case EXPRESSION_LOOK_ALIKE:
        return parse.e(javaSnippet);
      case METHOD_LOOK_ALIKE:
        return parse.m(javaSnippet);
      case OUTER_TYPE_LOOKALIKE:
        return parse.t(javaSnippet);
      case STATEMENTS_LOOK_ALIKE:
        return parse.s(javaSnippet);
      case BLOCK_LOOK_ALIKE:
        return az.astNode(the.firstOf(statements(az.block(parse.s(javaSnippet)))));
      default:
        for (final int guess : as.intArray(ASTParser.K_EXPRESSION, ASTParser.K_STATEMENTS, ASTParser.K_CLASS_BODY_DECLARATIONS,
            ASTParser.K_COMPILATION_UNIT)) {
          final ASTParser p = wizard.parser(guess);
          p.setSource(javaSnippet.toCharArray());
          final ASTNode $ = p.createAST(wizard.nullProgressMonitor);
          if (valid($))
            return $;
        }
        assert fault.unreachable() : fault.specifically("Snippet cannot be parsed", javaSnippet);
        return null;
    }
  }
  static ASTNode commonAncestor(final ASTNode n1, final ASTNode n2) {
    final List<ASTNode> ret = ancestors.path(n1), ns2 = ancestors.path(n2);
    for (int $ = 0; $ < Math.min(ret.size(), ns2.size()); ++$)
      if (ret.get($) == ns2.get($))
        return ret.get($);
    return null;
  }
  static CompilationUnit compilationUnitWithBinding(final File ¢) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(¢).createAST(null);
  }
  static CompilationUnit compilationUnitWithBinding(final String ¢) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(¢).createAST(null);
  }
  static <T> String completionIndex(final List<T> ts, final T t) {
    final String $ = ts.size() + "";
    String i = ts.indexOf(t) + 1 + "";
    while (i.length() < $.length())
      i = " " + i;
    return i + "/" + $;
  }
  /** @param ns unknown number of nodes to check
   * @return whether one of the nodes is an Expression Statement of __ Post or
   *         Pre Expression with ++ or -- operator. false if none of them are or
   *         if the given parameter is null. */
  static boolean containIncOrDecExp(final ASTNode... ns) {
    return ns != null && Stream.of(ns).anyMatch(λ -> λ != null && iz.updating(λ));
  }
  /** Determines if we can be certain that a {@link Statement} ends with a
   * sequencer ({@link ReturnStatement}, {@link ThrowStatement},
   * {@link BreakStatement}, {@link ContinueStatement}).
   * @param ¢ JD
   * @return true <b>iff</b> the Statement can be verified to end with a
   *         sequencer. */
  static boolean endsWithSequencer(final Statement ¢) {
    if (¢ == null)
      return false;
    final Statement $ = hop.lastStatement(¢);
    if ($ == null)
      return false;
    switch ($.getNodeType()) {
      case BLOCK:
        return endsWithSequencer(the.lastOf(statements((Block) $)));
      case BREAK_STATEMENT:
      case CONTINUE_STATEMENT:
      case RETURN_STATEMENT:
      case THROW_STATEMENT:
        return true;
      case DO_STATEMENT:
        return endsWithSequencer(((DoStatement) $).getBody());
      case LABELED_STATEMENT:
        return endsWithSequencer(((LabeledStatement) $).getBody());
      case IF_STATEMENT:
        return endsWithSequencer(then((IfStatement) $)) && endsWithSequencer(elze((IfStatement) $));
      default:
        return false;
    }
  }
  /** Determine whether two nodes are the same, in the sense that their textual
   * representations is identical.
   * <p>
   * Each of the parameters may be {@code null; a {@code null} is only equal
   * to{@code null
   * @param n1 JD
   * @param n2 JD
   * @return {@code true} if the parameters are the same. */
  static boolean eq(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType() && Trivia.cleanForm(n1).equals(Trivia.cleanForm(n2));
  }
  /** String wise comparison of all the given SimpleNames
   * @param ¢ string to compare all names to
   * @param xs SimplesNames to compare by their string value to cmpTo
   * @return whether all names are the same (string wise) or false otherwise */
  static boolean eq(final Expression x, final Expression... xs) {
    return Stream.of(xs).allMatch(λ -> eq(λ, x));
  }
  /** Determine whether two lists of nodes are the same, in the sense that their
   * textual representations is identical.
   * @param ns1 first list to compare
   * @param ns2 second list to compare
   * @return are the lists equal string-wise */
  @SuppressWarnings("boxing") static <N extends ASTNode> boolean eq(final List<N> ns1, final List<N> ns2) {
    return ns1 == ns2 || ns1.size() == ns2.size() && range.from(0).to(ns1.size()).stream().allMatch(λ -> eq(ns1.get(λ), ns2.get(λ)));
  }
  /** Works like same, but it applies {@ link tide.clean} to remove spaces
   * Determine whether two nodes are the same, in the sense that their textual
   * representations is identical.
   * <p>
   * Each of the parameters may be {@code null; a {@code null} is only equal
   * to{@code null
   * @param n1 JD
   * @param n2 JD
   * @return {@code true} if the parameters are the same.
   * @author matteo
   * @since 15/3/2017 */
  static boolean eq2(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType()
        && tide.clean(Trivia.cleanForm(n1) + "").equals(tide.clean(Trivia.cleanForm(n2) + ""));
  }
  /** Find the first matching expression to the given boolean (b).
   * @param b JD,
   * @param xs JD
   * @return first expression from the given list (es) whose boolean value
   *         matches to the given boolean (b). */
  static Expression find(final boolean b, final List<Expression> xs) {
    return xs.stream().filter(λ -> iz.booleanLiteral(λ) && b == az.booleanLiteral(λ).booleanValue()).findFirst().orElse(null);
  }
  static VariableDeclarationFragment findFragment(final FieldDeclaration ¢) {
    return fragments(¢).stream().filter(λ -> (λ.getName() + "").equals(FieldInitializedSerialVersionUIDToHexadecimal.SERIAL_VERSION_UID)).findFirst()
        .orElse(null);
  }
  /** Gets two lists of expressions and returns the idx of the only expression
   * which is different between them. If the lists differ with other then one
   * element, -1 is returned.
   * @param es1
   * @param es2
   */
  @SuppressWarnings("boxing") static int findSingleDifference(final List<? extends ASTNode> es1, final List<? extends ASTNode> es2) {
    int $ = -1;
    for (final Integer ¢ : range.from(0).to(es1.size()))
      if (!wizard.eq(es1.get(¢), es2.get(¢))) {
        if ($ >= 0)
          return -1;
        $ = ¢;
      }
    return $;
  }
  static boolean forbiddenOpOnPrimitive(final VariableDeclarationFragment f, final Statement nextStatement) {
    if (!iz.literal(f.getInitializer()) || !iz.expressionStatement(nextStatement))
      return false;
    final ExpressionStatement x = (ExpressionStatement) nextStatement;
    if (iz.methodInvocation(x.getExpression())) {
      final Expression $ = core(expression(x.getExpression()));
      return iz.simpleName($) && ((SimpleName) $).getIdentifier().equals(f.getName().getIdentifier());
    }
    if (!iz.fieldAccess(x.getExpression()))
      return false;
    final Expression e = core(((FieldAccess) x.getExpression()).getExpression());
    return iz.simpleName(e) && ((SimpleName) e).getIdentifier().equals(f.getName().getIdentifier());
  }
  @SuppressWarnings("unchecked") static List<MethodDeclaration> getMethodsSorted(final ASTNode n) {
    final Collection<MethodDeclaration> $ = an.empty.list();
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        $.add(¢);
        return false;
      }
    });
    return (List<MethodDeclaration>) $.stream().sorted((x, y) -> Metrics.countStatements(x) > Metrics.countStatements(y)
        || Metrics.countStatements(x) == Metrics.countStatements(y) && x.parameters().size() > y.parameters().size() ? -1 : 1);
  }
  static Message[] getProblems(final ASTNode $) {
    return !($ instanceof CompilationUnit) ? null : ((CompilationUnit) $).getMessages();
  }
  static Expression goInfix(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> $ = hop.operands(from);
    $.stream().filter(λ -> iz.parenthesizedExpression(λ) && iz.assignment(extract.core(λ))).forEachOrdered(x -> {
      final Assignment a = az.assignment(extract.core(x));
      final SimpleName var = az.simpleName(left(a));
      fragments(s).stream().filter(λ -> (name(λ) + "").equals(var + "")).forEach(λ -> {
        λ.setInitializer(copy.of(right(a)));
        $.set($.indexOf(x), x.getAST().newSimpleName(var + ""));
      });
    });
    return Wizard.append(subject.pair(the.firstOf($), $.get(1)).to(from.getOperator()), chop(chop($)));
  }
  static String intToClassName(final int $) {
    try {
      return ASTNode.nodeClassForType($).getSimpleName();
    } catch (final IllegalArgumentException ret) {
      return note.bug(ret);
    }
  }
  /** Checks if an expression need parenthesis in order to be interpreted
   * correctly @param x an Expression
   * @return whether or not this expression need parenthesis when put together
   *         with other expressions in infix expression. There could be non
   *         explicit parenthesis if the expression is located in an arguments
   *         list, so making it a part of infix expression require additional
   *         parenthesis */
  static boolean isParethesisNeeded(final Expression x) {
    return Stream.of(np).anyMatch(λ -> λ.isInstance(x));
  }
  static List<Statement> listMe(final Expression ¢) {
    return a.singleton.list(¢.getAST().newExpressionStatement(copy.of(¢)));
  }
  static List<VariableDeclarationFragment> live(final VariableDeclarationFragment f, final Collection<VariableDeclarationFragment> fs) {
    final List<VariableDeclarationFragment> $ = an.empty.list();
    fs.stream().filter(λ -> λ != f && λ.getInitializer() != null).forEach(λ -> $.add(copy.of(λ)));
    return $;
  }
  static MethodDeclaration methodWithBinding(final String m) {
    return findFirst.instanceOf(MethodDeclaration.class).in(makeAST.CLASS_BODY_DECLARATIONS.makeParserWithBinding(m).createAST(null));
  }
  static String nodeName(final ASTNode ¢) {
    return ¢ == null ? "???" : nodeName(¢.getClass());
  }
  static String nodeName(final Class<? extends ASTNode> ¢) {
    return English.name(¢);
  }
  static <N extends ASTNode> int nodeType(final Class<N> ¢) {
    final Integer $ = il.org.spartan.spartanizer.ast.navigate.wizard.classToNodeType.get(¢);
    return $ != null ? $.intValue()
        : zero.forgetting(note.bug(fault.dump() + //
            "\n c = " + ¢ + //
            "\n c.getSimpleName() = " + ¢.getSimpleName() + //
            "\n classForNodeType.keySet() = " + il.org.spartan.spartanizer.ast.navigate.wizard.classToNodeType.keySet() + //
            "\n classForNodeType = " + il.org.spartan.spartanizer.ast.navigate.wizard.classToNodeType + //
            fault.done()));
  }
  static int nodeTypesCount() {
    return il.org.spartan.spartanizer.ast.navigate.wizard.classToNodeType.size() + 2;
  }
  static boolean notDefaultLiteral(final Expression ¢) {
    return !iz.nullLiteral(¢) && !iz.literal0(¢) && !literal.false¢(¢) && !iz.literal(¢, 0.0) && !iz.literal(¢, 0L);
  }
  static boolean parenthesisRequiredIn(final Expression in, final ASTNode out) {
    return precedence.greater(out, in) || precedence.equal(out, in) && !op.nonAssociative(out);
  }
  /** Parenthesize an expression (if necessary).
   * @param x JD
   * @return a {@link copy#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis. */
  static Expression parenthesize(final Expression ¢) {
    return iz.noParenthesisRequired(¢) ? copy.of(¢) : subject.operand(¢).parenthesis();
  }
  static ASTParser parser(final int kind) {
    final ASTParser $ = ASTParser.newParser(AST.JLS14);
    setBinding($);
    $.setKind(kind);
    final Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
    $.setCompilerOptions(options);
    return $;
  }
  static int positivePrefixLength(final IfStatement $) {
    return Metrics.length($.getExpression(), then($));
  }
  static String problems(final ASTNode ¢) {
    return !(¢ instanceof CompilationUnit) ? "???" : problems((CompilationUnit) ¢);
  }
  static String problems(final CompilationUnit u) {
    final IProblem[] ret = u.getProblems();
    if (ret.length == 0)
      return "???";
    final Int $ = new Int();
    return Stream.of(ret).map(λ -> "\n\t\t\t" + ++$.inner + ": " + λ.getMessage()).reduce((x, y) -> x + y).get();
  }
  /** replaces an ASTNode with another
   * @param n
   * @param with */
  static <N extends ASTNode> void replace(final N n, final N with, final ASTRewrite r) {
    r.replace(n, with, null);
  }
  static int sequencerRank(final ASTNode ¢) {
    return the.index(¢.getNodeType(), BREAK_STATEMENT, CONTINUE_STATEMENT, RETURN_STATEMENT, THROW_STATEMENT);
  }
  static void setBinding(final ASTParser $) {
    $.setResolveBindings(resolveBinding.inner);
    if (resolveBinding.inner)
      $.setEnvironment(null, null, null, true);
  }
  static void setParserResolveBindings() {
    resolveBinding.inner = true;
  }
  static boolean shoudlInvert(final IfStatement s) {
    final int $ = wizard.sequencerRank(hop.lastStatement(then(s))), rankElse = wizard.sequencerRank(hop.lastStatement(elze(s)));
    return rankElse > $ || $ == rankElse && !thenIsShorter(s);
  }
  static String signAdjust(final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(as.bit(token.startsWith("+")));
  }
  static boolean thenIsShorter(final IfStatement s) {
    final Statement then = then(s), elze = elze(s);
    if (elze == null)
      return true;
    final int s1 = countOf.lines(then), s2 = countOf.lines(elze);
    if (s1 < s2)
      return true;
    if (s1 > s2)
      return false;
    assert s1 == s2;
    final int n2 = extract.statements(elze).size(), n1 = extract.statements(then).size();
    if (n1 < n2)
      return true;
    if (n1 > n2)
      return false;
    assert n1 == n2;
    final IfStatement $ = cons.invert(s);
    return wizard.positivePrefixLength($) >= wizard.positivePrefixLength(cons.invert($));
  }
  static boolean valid(final ASTNode ¢) {
    final CompilationUnit $ = az.compilationUnit(¢.getRoot());
    return $ == null || $.getProblems().length == 0;
  }
  static SimpleName newLowerCamelCase(final SimpleName old, final String s) {
    return make.from(old).identifier(s.substring(0, 1).toLowerCase() + s.substring(1));
  }
  static SimpleName newLowerCamelCase(final SimpleName old, final Type t) {
    return make.from(old).identifier((t + "").split("<")[0].substring(0, 1).toLowerCase() + (t + "").substring(1));
  }
}
