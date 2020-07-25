package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-29 */
public enum remove {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @param f
   * @param r
   * @param g */
  public static void deadFragment(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = fragments(parent).stream().filter(λ->λ!=f).map(copy::of).collect(Collectors.toList());
    if (live.isEmpty()) {
      r.remove(parent, g);
      return;
    }
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent, newParent, g);
  }
  public static Expression literal(final InfixExpression x, final boolean b) {
    final List<Expression> $ = extract.allOperands(x);
    remove.removeAll(b, $);
    switch ($.size()) {
      case 1:
        return copy.of(the.firstOf($));
      case 0:
        return x.getAST().newBooleanLiteral(b);
      default:
        return subject.operands($).to(x.getOperator());
    }
  }
  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param f
   * @param r
   * @param g */
  public static void local(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    r.remove(parent.fragments().size() > 1 ? f : parent, g);
  }
  public static ASTRewrite statement(final Statement s, final ASTRewrite r, final TextEditGroup g) {
    misc.statementRewriter(r, s).remove(s, g);
    return r;
  }
  private static List<Statement> breakSequencer(final Iterable<Statement> ss) {
    final List<Statement> $ = an.empty.list();
    for (final Statement ¢ : ss) {
      final Statement s = remove.breakSequencer(¢);
      if (s != null)
        $.add(s);
    }
    return $;
  }
  private static Statement breakSequencer(final Statement s) {
    if (s == null)
      return null;
    if (!iz.sequencerComplex(s, ASTNode.BREAK_STATEMENT))
      return copy.of(s);
    final AST a = s.getAST();
    Statement $ = null;
    if (iz.ifStatement(s)) {
      final IfStatement t = az.ifStatement(s);
      $ = subject.pair(breakSequencer(then(t)), breakSequencer(elze(t))).toIf(copy.of(expression(t)));
    } else if (!iz.block(s)) {
      if (iz.breakStatement(s) && iz.block(s.getParent()))
        $ = a.newEmptyStatement();
    } else {
      final Block b = subject.ss(breakSequencer(statements(az.block(s)))).toBlock();
      statements(b).addAll(breakSequencer(statements(az.block(s))));
      $ = b;
    }
    return $;
  }
  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}¢
   * <p>
   * @param ¢ JD
   * @param xs JD */
  private static void removeAll(final boolean ¢, final List<Expression> xs) {
    // noinspection ForLoopReplaceableByWhile
    for (;;) {
      final Expression x = wizard.find(¢, xs);
      if (x == null)
        return;
      xs.remove(x);
    }
  }
  private static List<Statement> removeBreakSequencer(final Iterable<Statement> ss) {
    final List<Statement> $ = an.empty.list();
    for (final Statement ¢ : ss) {
      final Statement s = removeBreakSequencer(¢);
      if (s != null)
        $.add(s);
    }
    return $;
  }
  private static Statement removeBreakSequencer(final Statement s) {
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
      final Block b = subject.ss(remove.removeBreakSequencer(statements(az.block(s)))).toBlock();
      statements(b).addAll(remove.removeBreakSequencer(statements(az.block(s))));
      $ = b;
    }
    return $;
  }
}
