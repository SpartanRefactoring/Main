package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.utils.FileUtils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** A number of utility functions common to all tippers.
 * @author Yossi Gil
 * @since 2015-07-17 */
public enum action {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static void addAllReplacing(final Collection<Statement> to, final Iterable<Statement> from, final Statement substitute, final Statement by1,
      final Iterable<Statement> by2) {
    for (final Statement ¢ : from)
      if (¢ != substitute)
        copy.into(¢, to);
      else {
        copy.into(by1, to);
        copy.into(by2, to);
      }
  }

  public static IfStatement blockIfNeeded(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    if (!iz.blockRequired(s))
      return s;
    final Block $ = subject.statement(s).toBlock();
    r.replace(s, $, g);
    return (IfStatement) first(statements($));
  }

  public static Expression eliminateLiteral(final InfixExpression x, final boolean b) {
    final List<Expression> $ = extract.allOperands(x);
    action.removeAll(b, $);
    switch ($.size()) {
      case 1:
        return copy.of(first($));
      case 0:
        return x.getAST().newBooleanLiteral(b);
      default:
        return subject.operands($).to(x.getOperator());
    }
  }

  public static ListRewrite insertAfter(final Statement where, final List<Statement> what, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(where.getParent(), Block.STATEMENTS_PROPERTY);
    for (int ¢ = what.size() - 1;; $.insertAfter(what.get(¢--), where, g))
      if (¢ < 0)
        return $;
  }

  public static ListRewrite insertBefore(final Statement where, final Iterable<Statement> what, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(parent(where), Block.STATEMENTS_PROPERTY);
    what.forEach(λ -> $.insertBefore(λ, where, g));
    return $;
  }

  public static IfStatement invert(final IfStatement ¢) {
    return subject.pair(elze(¢), then(¢)).toNot(¢.getExpression());
  }

  public static IfStatement makeShorterIf(final IfStatement s) {
    final List<Statement> then = extract.statements(then(s)), elze = extract.statements(elze(s));
    final IfStatement $ = action.invert(s);
    if (then.isEmpty())
      return $;
    final IfStatement main = copy.of(s);
    if (elze.isEmpty())
      return main;
    final int rankThen = action.sequencerRank(last(then)), rankElse = action.sequencerRank(last(elze));
    return rankElse > rankThen || rankThen == rankElse && !action.thenIsShorter(s) ? $ : main;
  }

  public static boolean mixedLiteralKind(final Collection<Expression> xs) {
    if (xs.size() <= 2)
      return false;
    int previousKind = -1;
    for (final Expression x : xs)
      if (x instanceof NumberLiteral || x instanceof CharacterLiteral) {
        final int currentKind = new NumericLiteralClassifier(x + "").type().ordinal();
        assert currentKind >= 0;
        if (previousKind == -1)
          previousKind = currentKind;
        else if (previousKind != currentKind)
          return true;
      }
    return false;
  }

  /** As {@link elze(ConditionalExpression)} but returns the last else statement
   * in "if - else if - ... - else" statement
   * @param ¢ JD
   * @return last nested else statement */
  public static Statement recursiveElse(final IfStatement ¢) {
    for (Statement $ = ¢.getElseStatement();; $ = ((IfStatement) $).getElseStatement())
      if (!($ instanceof IfStatement))
        return $;
  }

  public static void remove(final ASTRewrite r, final Statement s, final TextEditGroup g) {
    r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY).remove(s, g);
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param f
   * @param r
   * @param g */
  public static void remove(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    r.remove(parent.fragments().size() > 1 ? f : parent, g);
  }

  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}¢
   * <p>
   * @param ¢ JD
   * @param xs JD */
  public static void removeAll(final boolean ¢, final List<Expression> xs) {
    // noinspection ForLoopReplaceableByWhile
    for (;;) {
      final Expression x = wizard.find(¢, xs);
      if (x == null)
        return;
      xs.remove(x);
    }
  }

  public static List<Statement> removeBreakSequencer(final Iterable<Statement> ss) {
    final List<Statement> $ = new ArrayList<>();
    for (final Statement ¢ : ss) {
      final Statement s = action.removeBreakSequencer(¢);
      if (s != null)
        $.add(s);
    }
    return $;
  }

  public static Statement removeBreakSequencer(final Statement s) {
    if (s == null)
      return null;
    if (!iz.sequencerComplex(s, ASTNode.BREAK_STATEMENT))
      return copy.of(s);
    final AST a = s.getAST();
    Statement $ = null;
    if (iz.ifStatement(s)) {
      final IfStatement t = az.ifStatement(s);
      $ = subject.pair(removeBreakSequencer(then(t)), removeBreakSequencer(elze(t))).toIf(copy.of(expression(t)));
    } else if (!iz.block(s)) {
      if (iz.breakStatement(s) && iz.block(s.getParent()))
        $ = a.newEmptyStatement();
    } else {
      final Block b = subject.ss(removeBreakSequencer(statements(az.block(s)))).toBlock();
      statements(b).addAll(removeBreakSequencer(statements(az.block(s))));
      $ = b;
    }
    return $;
  }

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  public static Expression removeInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? wizard.goInfix(az.infixExpression(from), s)
        : iz.assignment(from) ? LocalVariableIntializedStatementToForInitializers.handleAssignmentCondition(az.assignment(from), s) : from;
  }

  public static void rename(final SimpleName oldName, final SimpleName newName, final ASTNode where, final ASTRewrite r, final TextEditGroup g) {
    new Inliner(oldName, r, g).byValue(newName).inlineInto(collect.usesOf(oldName).in(where).toArray(new SimpleName[0]));
  }

  public static ASTRewrite replaceTwoStatements(final ASTRewrite r, final Statement what, final Statement by, final TextEditGroup g) {
    final Block parent = az.block(what.getParent());
    final List<Statement> siblings = extract.statements(parent);
    final int i = siblings.indexOf(what);
    siblings.remove(i);
    siblings.remove(i);
    siblings.add(i, by);
    final Block $ = parent.getAST().newBlock();
    copy.into(siblings, statements($));
    r.replace(parent, $, g);
    return r;
  }

  public static boolean shoudlInvert(final IfStatement s) {
    final int $ = sequencerRank(hop.lastStatement(then(s))), rankElse = sequencerRank(hop.lastStatement(elze(s)));
    return rankElse > $ || $ == rankElse && !action.thenIsShorter(s);
  }

  public static boolean thenIsShorter(final IfStatement s) {
    final Statement then = then(s), elze = elze(s);
    if (elze == null)
      return true;
    final int s1 = count.lines(then), s2 = count.lines(elze);
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
    final IfStatement $ = action.invert(s);
    return positivePrefixLength($) >= positivePrefixLength(action.invert($));
  }

  private static int positivePrefixLength(final IfStatement $) {
    return metrics.length($.getExpression(), then($));
  }

  public static int sequencerRank(final ASTNode ¢) {
    return lisp2.index(¢.getNodeType(), BREAK_STATEMENT, CONTINUE_STATEMENT, RETURN_STATEMENT, THROW_STATEMENT);
  }

  public static void addImport(final CompilationUnit u, final ASTRewrite r, final ImportDeclaration d) {
    r.getListRewrite(u, CompilationUnit.IMPORTS_PROPERTY).insertLast(d, null);
  }

  public static <N extends MethodDeclaration> void addJavaDoc(final N n, final ASTRewrite r, final TextEditGroup g, final String addedJavadoc) {
    final Javadoc j = n.getJavadoc();
    if (j == null)
      r.replace(n,
          r.createGroupNode(new ASTNode[] { r.createStringPlaceholder("/**\n" + addedJavadoc + "\n*/\n", ASTNode.JAVADOC), r.createCopyTarget(n) }),
          g);
    else
      r.replace(j,
          r.createStringPlaceholder(
              (j + "").replaceFirst("\\*\\/$", ((j + "").matches("(?s).*\n\\s*\\*\\/$") ? "" : "\n ") + "* " + addedJavadoc + "\n */"),
              ASTNode.JAVADOC),
          g);
  }

  /** Adds method m to the first type in file.
   * @param fileName
   * @param m */
  public static void addMethodToFile(final String fileName, final MethodDeclaration m) {
    try {
      final String str = readFromFile(fileName);
      final IDocument d = new Document(str);
      final AbstractTypeDeclaration t = findFirst.abstractTypeDeclaration(makeAST.COMPILATION_UNIT.from(d));
      final ASTRewrite r = ASTRewrite.create(t.getAST());
      action.addMethodToType(t, m, r, null);
      r.rewriteAST(d, null).apply(d);
      writeToFile(fileName, d.get());
    } catch (IOException | MalformedTreeException | IllegalArgumentException | BadLocationException x2) {
      x2.printStackTrace();
    }
  }

  /** @param d JD
   * @param m JD
   * @param r rewriter
   * @param g edit group, usually null */
  public static void addMethodToType(final AbstractTypeDeclaration d, final MethodDeclaration m, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(d, d.getBodyDeclarationsProperty()).insertLast(ASTNode.copySubtree(d.getAST(), m), g);
  }

  /** @param d JD
   * @param s JD
   * @param r rewriter
   * @param g edit group, usually null */
  public static void addStatement(final MethodDeclaration d, final ReturnStatement s, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(step.body(d), Block.STATEMENTS_PROPERTY).insertLast(s, g);
  }

  public static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? wizard.goInfix(copy.of(az.infixExpression(from)), s)
        : iz.assignment(from) ? LocalVariableIntializedStatementToForInitializers.handleAssignmentCondition(az.assignment(from), s)
            : iz.parenthesizedExpression(from)
                ? LocalVariableIntializedStatementToForInitializers.handleParenthesizedCondition(az.parenthesizedExpression(from), s) : from;
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type.
   * @see ASTNode#copySubtree
   * @see ASTRewrite */
  @SuppressWarnings("unchecked") public static <N extends ASTNode> N rebase(final N n, final AST t) {
    return (N) copySubtree(t, n);
  }

  /** Parenthesize an expression (if necessary).
   * @param x JD
   * @return a {@link copy#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis. */
  public static Expression parenthesize(final Expression ¢) {
    return iz.noParenthesisRequired(¢) ? copy.of(¢) : make.parethesized(¢);
  }

  /** replaces an ASTNode with another
   * @param n
   * @param with */
  public static <N extends ASTNode> void replace(final N n, final N with, final ASTRewrite r) {
    r.replace(n, with, null);
  }
}
