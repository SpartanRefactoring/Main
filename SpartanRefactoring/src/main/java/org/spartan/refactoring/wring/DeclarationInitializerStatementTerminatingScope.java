package org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.spartan.refactoring.utils.Funcs.asBlock;
import static org.spartan.refactoring.utils.Funcs.duplicate;
import static org.spartan.utils.Utils.intIsIn;
import static org.spartan.utils.Utils.lastIn;
import static org.spartan.utils.Utils.penultimateIn;

import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.*;
import org.spartan.refactoring.wring.LocalInliner.LocalInlineWithValue;

/**
 * A {@link Wring} to convert <code>int a = 3;
 * b = a;</code> into <code>b = a</code>
 *
 * @author Yossi Gil
 * @since 2015-08-07
 */
public final class DeclarationInitializerStatementTerminatingScope extends Wring.VariableDeclarationFragementAndStatement {
  @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer, final Statement nextStatement,
      final TextEditGroup g) {
    if (initializer == null)
      return null;
    if (hasAnnotation(f))
      return null;
    final Statement s = Extract.statement(f);
    if (s == null)
      return null;
    final Block parent = asBlock(s.getParent());
    if (parent == null)
      return null;
    final List<Statement> ss = parent.statements();
    if (!lastIn(nextStatement, ss) || !penultimateIn(s, ss) || !Collect.definitionsOf(n).in(nextStatement).isEmpty())
      return null;
    final List<SimpleName> uses = Collect.usesOf(f.getName()).in(nextStatement);
    if (!Is.sideEffectFree(initializer)) {
      if (uses.size() > 1)
        return null;
      for (final SimpleName use : uses)
        if (forbidden(use, nextStatement))
          return null;
    }
    for (final SimpleName use : uses)
      if (never(use, nextStatement))
        return null;
    final LocalInlineWithValue i = new LocalInliner(n, r, g).byValue(initializer);
    final Statement newStatement = duplicate(nextStatement);
    final int addedSize = i.addedSize(newStatement);
    final int removalSaving = removalSaving(f);
    if (addedSize - removalSaving > 0)
      return null;
    r.replace(nextStatement, newStatement, g);
    i.inlineInto(newStatement);
    remove(f, r, g);
    return r;
  }
  private static boolean never(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : AncestorSearch.until(s).ancestors(n))
      if (intIsIn(ancestor.getNodeType(), TRY_STATEMENT, SYNCHRONIZED_STATEMENT))
        return true;
    return false;
  }
  private static boolean forbidden(final SimpleName n, final Statement s) {
    ASTNode child = null;
    for (final ASTNode ancestor : AncestorSearch.until(s).ancestors(n)) {
      switch (ancestor.getNodeType()) {
        case WHILE_STATEMENT:
        case DO_STATEMENT:
          return true;
        case FOR_STATEMENT:
          if (((ForStatement) ancestor).initializers().indexOf(child) != -1)
            break;
          return true;
        case ENHANCED_FOR_STATEMENT:
          if (((EnhancedForStatement) ancestor).getExpression() != child)
            return true;
          break;
        default:
          break;
      }
      child = ancestor;
    }
    return false;
  }
  @Override String description(final VariableDeclarationFragment f) {
    return "Inline local " + f.getName() + " into subsequent statement";
  }
}