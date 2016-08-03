package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.LocalInliner.*;

/** A {@link Wring} to convert
 * 
 * <pre>
 * int a = 3;
 * b = a;
 * </pre>
 * 
 * into
 * 
 * <pre>
 * b = a
 * </pre>
 * 
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationInitializerStatementTerminatingScope extends Wring.VariableDeclarationFragementAndStatement {
  @SuppressWarnings("unchecked") @Override ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n,
      final Expression initializer, final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || hasAnnotation(f) || initializer instanceof ArrayInitializer)
      return null;
    for (final IExtendedModifier m : expose.modifiers((VariableDeclarationStatement) f.getParent()))
      if (m.isModifier() && ((Modifier) m).isFinal())
        return null;
    final Statement s = extract.statement(f);
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
      if (Funcs.intIsIn(ancestor.getNodeType(), TRY_STATEMENT, SYNCHRONIZED_STATEMENT))
        return true;
    return false;
  }
  private static boolean forbidden(final SimpleName n, final Statement s) {
    ASTNode child = null;
    for (final ASTNode ancestor : AncestorSearch.until(s).ancestors(n)) {
      switch (ancestor.getNodeType()) {
        case WHILE_STATEMENT:
        case DO_STATEMENT:
        case ANONYMOUS_CLASS_DECLARATION:
          return true;
        case FOR_STATEMENT:
          if (expose.initializers((ForStatement) ancestor).indexOf(child) != -1)
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
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}