package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-29 */
public enum remove {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static List<Statement> breakSequencer(final Iterable<Statement> ss) {
    final List<Statement> $ = new ArrayList<>();
    for (final Statement ¢ : ss) {
      final Statement s = remove.breakSequencer(¢);
      if (s != null)
        $.add(s);
    }
    return $;
  }

  public static Statement breakSequencer(final Statement s) {
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

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @param f
   * @param r
   * @param g */
  public static void deadFragment(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = wizard.live(f, fragments(parent));
    if (live.isEmpty()) {
      r.remove(parent, g);
      return;
    }
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent, newParent, g);
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

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  public static Expression initializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? wizard.goInfix(az.infixExpression(from), s)
        : iz.assignment(from) ? LocalVariableIntializedStatementToForInitializers.handleAssignmentCondition(az.assignment(from), s) : from;
  }

  public static void statement(final Statement s, final ASTRewrite r, final TextEditGroup g) {
    trick.statementRewriter(r, s).remove(s, g);
  }
}
