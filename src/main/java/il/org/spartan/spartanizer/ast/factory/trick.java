package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.utils.FileUtils.*;

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
import il.org.spartan.utils.*;

/** A number of utility functions common to all tippers.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-17 */
public enum trick {
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

  /** Adds method m to the first type in file.
   * @param fileName
   * @param m */
  public static void addMethodToFile(final String fileName, final MethodDeclaration m) {
    try {
      final String str = readFromFile(fileName);
      final IDocument d = new Document(str);
      final AbstractTypeDeclaration t = findFirst.abstractTypeDeclaration(makeAST.COMPILATION_UNIT.from(d));
      final ASTRewrite r = ASTRewrite.create(t.getAST());
      addMethodToType(t, m, r, null);
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

  public static boolean anyFurtherUsage(final Statement originalStatement, final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    final ASTNode parent = parent(nextStatement);
    parent.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(nextStatement)//
            && !¢.equals(originalStatement)//
            && iz.statement(¢)//
            && !find.occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  public static Expression eliminateLiteral(final InfixExpression x, final boolean b) {
    final List<Expression> $ = extract.allOperands(x);
    trick.removeAll(b, $);
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

  public static SimpleName peelIdentifier(final Statement s, final String id) {
    final List<SimpleName> $ = find.occurencesOf(s, id);
    return $.size() != 1 ? null : first($);
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
    final int $ = wizard.sequencerRank(hop.lastStatement(then(s))), rankElse = wizard.sequencerRank(hop.lastStatement(elze(s)));
    return rankElse > $ || $ == rankElse && !trick.thenIsShorter(s);
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
    final IfStatement $ = make.invert(s);
    return positivePrefixLength($) >= positivePrefixLength(make.invert($));
  }

  static int positivePrefixLength(final IfStatement $) {
    return metrics.length($.getExpression(), then($));
  }

  public static ListRewrite statementsRewriter(final ASTRewrite r, final Statement s) {
    return parent(s) instanceof SwitchStatement ? r.getListRewrite(parent(s), SwitchStatement.STATEMENTS_PROPERTY)
        : parent(s) instanceof Block ? r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY) //
            : monitor.bug("Weird type", s, parent(s));
  }
}
