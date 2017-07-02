package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.utils.FileUtils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** A number of utility functions common to all tippers.
 * @author Yossi Gil
 * @since 2015-07-17 */
public enum misc {
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
  /** Adds method m to the first __ in file.
   * @param fileName
   * @param m */
  public static void addMethodToFile(final String fileName, final MethodDeclaration m) {
    try {
      final String s = readFromFile(fileName);
      final IDocument d = new Document(s);
      final AbstractTypeDeclaration t = findFirst.abstractTypeDeclaration(makeAST.COMPILATION_UNIT.from(d));
      final ASTRewrite r = ASTRewrite.create(t.getAST());
      misc.addMethodToType(t, m, r, null);
      r.rewriteAST(d, null).apply(d);
      writeToFile(fileName, d.get());
    } catch (final IOException ¢) {
      note.io(¢);
    } catch (final BadLocationException | IllegalArgumentException | MalformedTreeException ¢) {
      note.bug(¢);
    }
  }
  /** @param d JD
   * @param m JD
   * @param r rewriter
   * @param g edit group, usually null */
  public static void addMethodToType(final AbstractTypeDeclaration d, final MethodDeclaration m, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(d, d.getBodyDeclarationsProperty()).insertLast(copy.of(m), g);
  }
  /** @param d JD
   * @param s JD
   * @param r rewriter
   * @param g edit group, usually null */
  public static void addStatement(final MethodDeclaration d, final ReturnStatement s, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(step.body(d), Block.STATEMENTS_PROPERTY).insertLast(s, g);
  }
  public static IfStatement blockIfNeeded(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    if (!iz.blockRequired(s))
      return s;
    final Block $ = subject.statement(s).toBlock();
    r.replace(s, $, g);
    return (IfStatement) the.firstOf(statements($));
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
  public static boolean leftSide(final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    // noinspection SameReturnValue
    nextStatement.accept(new ASTVisitor(true) {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢))//
            && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }
  public static IfStatement makeShorterIf(final IfStatement s) {
    final List<Statement> then = extract.statements(then(s)), elze = extract.statements(elze(s));
    final IfStatement $ = misc.invert(s);
    if (then.isEmpty())
      return $;
    final IfStatement main = copy.of(s);
    if (elze.isEmpty())
      return main;
    final int rankThen = misc.sequencerRank(the.lastOf(then)), rankElse = misc.sequencerRank(the.lastOf(elze));
    return rankElse > rankThen || rankThen == rankElse && !misc.thenIsShorter(s) ? $ : main;
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
  /** Parenthesize an expression (if necessary).
   * @param x JD
   * @return a {@link copy#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis. */
  public static Expression parenthesize(final Expression ¢) {
    return iz.noParenthesisRequired(¢) ? copy.of(¢) : make.parethesized(¢);
  }
  public static SimpleName peelIdentifier(final Statement s, final String id) {
    final List<SimpleName> $ = find.occurencesOf(s, id);
    return $.size() != 1 ? null : the.firstOf($);
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
  public static void rename(final SimpleName oldName, final SimpleName newName, final ASTNode where, final ASTRewrite r, final TextEditGroup g) {
    new Inliner(oldName, r, g).byValue(newName).inlineInto(collect.usesOf(oldName).in(where).toArray(new SimpleName[0]));
  }
  public static ASTRewrite replaceTwoStatements(final ASTRewrite ret, final Statement what, final Statement by, final TextEditGroup g) {
    final Block parent = az.block(what.getParent());
    final List<Statement> siblings = extract.statements(parent);
    final int i = siblings.indexOf(what);
    siblings.remove(i);
    siblings.remove(i);
    siblings.add(i, by);
    final Block $ = parent.getAST().newBlock();
    copy.into(siblings, statements($));
    ret.replace(parent, $, g);
    return ret;
  }
  public static ListRewrite statementRewriter(final ASTRewrite r, final Statement s) {
    return parent(s) instanceof SwitchStatement ? r.getListRewrite(parent(s), SwitchStatement.STATEMENTS_PROPERTY)
        : parent(s) instanceof Block ? r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY) //
            : note.bug("Weird __ of %s under %s", s, parent(s));
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
    final IfStatement $ = misc.invert(s);
    return positivePrefixLength($) >= positivePrefixLength(misc.invert($));
  }
  static int positivePrefixLength(final IfStatement $) {
    return metrics.length($.getExpression(), then($));
  }
  static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? wizard.goInfix(copy.of(az.infixExpression(from)), s)
        : iz.assignment(from) ? LocalInitializedStatementToForInitializers.handleAssignmentCondition(az.assignment(from), s)
            : iz.parenthesizedExpression(from)
                ? LocalInitializedStatementToForInitializers.handleParenthesizedCondition(az.parenthesizedExpression(from), s) : from;
  }
  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned __.
   * @see ASTNode#copySubtree
   * @see ASTRewrite */
  @SuppressWarnings("unchecked") static <N extends ASTNode> N rebase(final N n, final AST t) {
    return (N) copySubtree(t, n);
  }
  private static int sequencerRank(final ASTNode ¢) {
    return the.index(¢.getNodeType(), BREAK_STATEMENT, CONTINUE_STATEMENT, RETURN_STATEMENT, THROW_STATEMENT);
  }
}
